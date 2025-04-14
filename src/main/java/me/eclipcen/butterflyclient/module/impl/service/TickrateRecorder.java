package me.eclipcen.butterflyclient.module.impl.service;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import me.eclipcen.butterflyclient.event.packet.PacketEvent.Receive.Pre;
import me.eclipcen.butterflyclient.event.world.WorldEvent.Load;
import me.eclipcen.butterflyclient.module.api.branches.ServiceMod;
import net.futureclient.eventbus.SubscribeEvent;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.util.math.MathHelper;

public class TickrateRecorder extends ServiceMod {
   public static final double MAX_TICKRATE = 20.0;
   public static final double MIN_TICKRATE = 0.0;
   public static final int MAXIMUM_SAMPLE_SIZE = 100;
   private static final TickrateRecorder INSTANCE = new TickrateRecorder();
   private static final TickrateRecorder.TickRateData TICK_DATA = new TickrateRecorder.TickRateData(100);
   private long timeLastTimeUpdate = -1L;

   public static TickrateRecorder getInstance() {
      return INSTANCE;
   }

   public static TickrateRecorder.TickRateData getTickData() {
      return TICK_DATA;
   }

   public TickrateRecorder() {
      super("TickRate");
   }

   @SubscribeEvent
   public void onWorldLoad(Load event) {
      timeLastTimeUpdate = -1L;
      TICK_DATA.onWorldLoaded();
   }

   @SubscribeEvent
   public void onPacketReceived(Pre event) {
      if (event.getPacket() instanceof SPacketTimeUpdate) {
         long currentTimeMillis = System.currentTimeMillis();
         if (timeLastTimeUpdate != -1L) {
            TICK_DATA.onTimePacketIncoming(currentTimeMillis - timeLastTimeUpdate);
         }

         timeLastTimeUpdate = currentTimeMillis;
      }
   }

   public static class TickRateData {
      private final TickrateRecorder.TickRateData.CalculationData EMPTY_DATA = new TickrateRecorder.TickRateData.CalculationData();
      private final Queue<Double> rates;
      private final List<TickrateRecorder.TickRateData.CalculationData> data = Lists.newArrayList();

      private TickRateData(int maxSampleSize) {
         rates = EvictingQueue.create(maxSampleSize);

         for (int i = 0; i < maxSampleSize; i++) {
            data.add(new TickrateRecorder.TickRateData.CalculationData());
         }
      }

      private void resetData() {
         for (TickrateRecorder.TickRateData.CalculationData d : data) {
            d.reset();
         }
      }

      private void recalculate() {
         resetData();
         int size = 0;
         double total = 0.0;
         List<Double> in = Lists.newArrayList(rates);
         Collections.reverse(in);

         for (Double rate : in) {
            size++;
            total += rate;

            TickrateRecorder.TickRateData.CalculationData d = data.get(size - 1);
            if (d != null) {
               d.average = MathHelper.clamp(total / size, 0.0, 20.0);
            }
         }
      }

      public TickrateRecorder.TickRateData.CalculationData getPoint(int point) {
         TickrateRecorder.TickRateData.CalculationData d = data
                 .get(Math.max(Math.min(getSampleSize() - 1, point - 1), 0));
         return d != null ? d : EMPTY_DATA;
      }

      public TickrateRecorder.TickRateData.CalculationData getPoint() {
         return getPoint(getSampleSize() - 1);
      }

      public int getSampleSize() {
         return rates.size();
      }

      private void onTimePacketIncoming(long difference) {
         double timeElapsed = difference / 1000.0;

         rates.offer(MathHelper.clamp(20.0 / timeElapsed, 0.0, 20.0));
         recalculate();
      }

      private void onWorldLoaded() {
         rates.clear();
         resetData();
      }

      public static class CalculationData {
         private double average = 0.0;

         public double getAverage() {
            return average;
         }

         public void reset() {
            average = 0.0;
         }
      }
   }
}
