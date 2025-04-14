package me.eclipcen.butterflyclient.event.render;

import me.eclipcen.butterflyclient.util.Globals;
import net.futureclient.eventbus.Event;
import net.minecraft.client.gui.ScaledResolution;

public class RenderEvent extends Event {
   private float partialTicks;

   public RenderEvent(float partialTicks) {
      this.partialTicks = partialTicks;
   }

   public float getPartialTicks() {
      return partialTicks;
   }

   public void setPartialTicks(float partialTicks) {
      this.partialTicks = partialTicks;
   }

   public static class Render2DEvent extends Event {
      private final ScaledResolution resolution;
      private final float partialTicks;

      public Render2DEvent(float partialTicks) {
         this.resolution = new ScaledResolution(Globals.MC);
         this.partialTicks = partialTicks;
      }

      public float getPartialTicks() {
         return partialTicks;
      }

      public double getScreenWidth() {
         return resolution.getScaledWidth_double();
      }

      public double getScreenHeight() {
         return resolution.getScaledHeight_double();
      }
   }
}
