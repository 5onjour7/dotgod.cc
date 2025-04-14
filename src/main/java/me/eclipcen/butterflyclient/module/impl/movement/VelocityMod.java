package me.eclipcen.butterflyclient.module.impl.movement;

import me.eclipcen.butterflyclient.event.EventType;
import me.eclipcen.butterflyclient.event.packet.PacketEvent;
import me.eclipcen.butterflyclient.event.player.PlayerPushOutOfBlocksEvent;
import me.eclipcen.butterflyclient.event.player.PlayerUpdateWalkingEvent;
import me.eclipcen.butterflyclient.module.api.Category;
import me.eclipcen.butterflyclient.module.api.branches.ToggleMod;
import me.eclipcen.butterflyclient.wrapper.ISPacketExplosion;
import net.futureclient.eventbus.SubscribeEvent;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;

public class VelocityMod extends ToggleMod {
   public VelocityMod() {
      super(Category.MOVEMENT, "Velocity", "Cancels knockback applied to player");
   }

   @SubscribeEvent
   public void onUpdate(PlayerUpdateWalkingEvent event) {
      if (event.getType() == EventType.Type.PRE) {
         mc.player.entityCollisionReduction = 1.0F;
      }
   }

   @SubscribeEvent
   public void onPacket(PacketEvent.Receive.Pre event) {
      if (mc.player != null) {
         if (event.getPacket() instanceof SPacketEntityVelocity) {
            SPacketEntityVelocity packetVelocity = event.getPacket();

            if (packetVelocity.getEntityID() == mc.player.getEntityId()) {
               event.setCanceled(true);
            }
         } else if (event.getPacket() instanceof SPacketExplosion) {
            SPacketExplosion packetExplosion = event.getPacket();
            ISPacketExplosion wrapper = (ISPacketExplosion)packetExplosion;

            wrapper.setMotionX(0.0F);
            wrapper.setMotionY(0.0F);
            wrapper.setMotionZ(0.0F);
         }

      }
   }

   @SubscribeEvent
   public void onBlockPush(PlayerPushOutOfBlocksEvent event) {
      event.setCanceled(true);
   }
}
