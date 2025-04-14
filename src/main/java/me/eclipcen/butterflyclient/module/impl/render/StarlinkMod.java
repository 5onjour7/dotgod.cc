package me.eclipcen.butterflyclient.module.impl.render;

import java.io.IOException;
import java.util.Objects;
import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.event.client.TickEvent;
import me.eclipcen.butterflyclient.event.packet.PacketEvent;
import me.eclipcen.butterflyclient.event.world.WorldEvent;
import me.eclipcen.butterflyclient.module.api.Category;
import me.eclipcen.butterflyclient.module.api.branches.ToggleMod;
import me.eclipcen.butterflyclient.module.impl.combat.AutoCrystalMod;
import me.eclipcen.butterflyclient.util.settings.Setting;
import me.eclipcen.butterflyclient.wrapper.IPlayerControllerMP;
import net.futureclient.eventbus.SubscribeEvent;
import net.futureclient.interception.client.Interception;
import net.futureclient.interception.loader.wrapper.INetworkManager;
import net.futureclient.interception.shared.attribute.AttributeManager;
import net.futureclient.interception.shared.network.InterceptionProtocol;
import net.futureclient.interception.shared.network.packet.InterceptionPacket;
import net.futureclient.interception.shared.network.packet.impl.ServerKeepAliveTime;
import net.futureclient.interception.shared.network.packet.impl.ServerMessage;
import net.futureclient.interception.shared.network.packet.impl.SharedAttributeSync;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.SPacketCustomPayload;

public class StarlinkMod extends ToggleMod {
   private final Setting<StarlinkMod.Booster> booster = new Setting<>("Booster", "booster region", StarlinkMod.Booster.EU_DE_FF_LIN);
   private final Setting<Boolean> confirmPings = new Setting<>("ConfirmPings", "show power levels", false);
   private final Setting<Float> confirmDelay = new Setting<>("ConfirmDelay", "delay in ms between confirm pings", 0.0F, 0.0F, 1000.0F, 1.0F);
   private final Setting<StarlinkMod.Predict> predict = new Setting<>("Predict", "magic crystal ball", StarlinkMod.Predict.Always);
   private final Setting<Float> predictDelay = new Setting<>("PredictDelay", "delay in ms between prediction breaks", 0.0F, 0.0F, 1000.0F, 10.0F);
   private final AttributeManager clientAttributeManager = new AttributeManager();
   private final AttributeManager.Attribute<Boolean> confirmPingsAttribute = clientAttributeManager
           .register("confirm_pings", AttributeManager.Serializer.BOOLEAN_SERIALIZER, false);
   private final AttributeManager.Attribute<Boolean> predictAttribute = clientAttributeManager
           .register("predict", AttributeManager.Serializer.BOOLEAN_SERIALIZER, false);
   private final AttributeManager.Attribute<Long> confirmDelayAttribute = clientAttributeManager
           .register("confirm_delay", AttributeManager.Serializer.LONG_SERIALIZER, 0L);
   private final AttributeManager.Attribute<Long> predictDelayAttribute = clientAttributeManager
           .register("predict_delay", AttributeManager.Serializer.LONG_SERIALIZER, 0L);
   private long pingDiff;
   private StarlinkMod.Booster prevBooster;

   public StarlinkMod() {
      super(Category.RENDER, "Starlink", "Magic");
   }

   public String getHUDTag() {
      if (mc.player != null) {
         return pingDiff + "ms";
      }

      return super.getHUDTag();
   }

   @SubscribeEvent
   public void onPacketReceive(PacketEvent.Receive.Pre event) {
      if (event.getPacket() instanceof SPacketCustomPayload) {
         SPacketCustomPayload packet = event.getPacket();

         try {
            InterceptionPacket interceptionPacket = InterceptionProtocol.readClient(packet);
            if (interceptionPacket != null) {
               event.setCanceled(true);
               if (interceptionPacket instanceof ServerMessage) {
                  ServerMessage serverMessage = (ServerMessage)interceptionPacket;
                  if (mc.ingameGUI != null) {
                     mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(serverMessage.getComponent(), serverMessage.getType() == ServerMessage.Type.GENERIC ? -2147483647 : 0);
                  }
               } else if (interceptionPacket instanceof ServerKeepAliveTime) {
                  ServerKeepAliveTime serverKeepAliveTime = (ServerKeepAliveTime)interceptionPacket;
                  pingDiff = serverKeepAliveTime.getTime();
               }
            }
         } catch (IOException var5) {
            event.setCanceled(true);
            var5.printStackTrace();
         }
      }

   }

   @SubscribeEvent
   public void onWorldLoad(WorldEvent.Load event) {
      pingDiff = 0L;
      flush(true);
   }

   @SubscribeEvent
   public void onUpdate(TickEvent.Pre event) {
      StarlinkMod.Booster booster = this.booster.getValue();
      if (booster != prevBooster) {
         Interception.enable(booster.ip, booster.port);
      }

      prevBooster = booster;
      NetHandlerPlayClient proxyConnection = getProxyConnection();

      if (proxyConnection != null) {
         confirmPingsAttribute.setValue(confirmPings.getValue());
         confirmDelayAttribute.setValue(confirmDelay.getValue().longValue());
         StarlinkMod.Predict predict = this.predict.getValue();

         if (predict == StarlinkMod.Predict.Off) {
            predictAttribute.setValue(false);
         } else if (predict == StarlinkMod.Predict.AutoCrystal) {
            ToggleMod mod = Butterfly.getInstance().getModuleManager().getModule(AutoCrystalMod.class);
            predictAttribute.setValue(Objects.requireNonNull(mod).isEnabled());
         } else if (predict == StarlinkMod.Predict.Always) {
            predictAttribute.setValue(true);
         }

         predictDelayAttribute.setValue(predictDelay.getValue().longValue());
         if (clientAttributeManager.isDirty()) {
            flush(false);
         }
      }

   }

   protected void onEnable() {
      prevBooster = null;
      flush(true);
   }

   protected void onDisable() {
      confirmPingsAttribute.setValue(false);
      predictAttribute.setValue(false);

      flush(true);
      Interception.disable();

      prevBooster = null;
   }

   private void flush(boolean full) {
      NetHandlerPlayClient proxyConnection = getProxyConnection();
      if (proxyConnection != null) {
         try {
            proxyConnection.sendPacket(InterceptionProtocol.writeClient(new SharedAttributeSync(clientAttributeManager, full)));
         } catch (IOException var4) {
            var4.printStackTrace();
         }
      }

   }

   private NetHandlerPlayClient getProxyConnection() {
      PlayerControllerMP playerController = mc.playerController;

      if (playerController != null) {
         NetHandlerPlayClient connection = ((IPlayerControllerMP)playerController).getConnection();

         if (connection != null && ((INetworkManager)connection.getNetworkManager()).getSharedSecretKey() != null) {
            return connection;
         }
      }

      return null;
   }

   private enum Predict {
      Off,
      AutoCrystal,
      Always;
   }

   private enum Booster {
      // Wouldn't suggest connecting to these servers, unless you want your IP logged...
      NA_US_CF_LA("35.235.116.210", 50210),
      NA_US_NJ_DTO("67.205.135.82", 50210),
      NA_US_NJ_LIN("172.104.29.136", 50210),
      NA_CA_MO_GCP("34.152.53.54", 50210),
      NA_CA_MO_OVH("158.69.123.138", 50210),
      NA_CA_TO_LIN("172.105.16.87", 50210),
      NA_US_NY_DTO("161.35.104.64", 50210),
      EU_FR_RBX_OVH("auth.futureclient.net", 50210),
      EU_DE_FF_LIN("172.104.158.171", 50210),
      EU_DE_FF_DTO("46.101.113.245", 50210),
      AP_TW_TP_GCP("35.236.139.164", 50210),
      AP_JP_TYO_GCP("35.187.214.41", 50210),
      AP_AUS_SYD_GCP("35.244.110.177", 50210),
      LOCAL("localhost", 50210);

      private final String ip;
      private final int port;

      Booster(String ip, int port) {
         this.ip = ip;
         this.port = port;
      }
   }
}
