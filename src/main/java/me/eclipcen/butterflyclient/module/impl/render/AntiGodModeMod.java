package me.eclipcen.butterflyclient.module.impl.render;

import me.eclipcen.butterflyclient.event.packet.PacketEvent;
import me.eclipcen.butterflyclient.module.api.Category;
import me.eclipcen.butterflyclient.module.api.branches.ToggleMod;
import net.futureclient.eventbus.SubscribeEvent;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;

public class AntiGodModeMod extends ToggleMod {
   public AntiGodModeMod() {
      super(Category.RENDER, "AntiGodMode", "Show where invis/godmode people are in your render.");
   }

   @SubscribeEvent
   public void onPacketReceive(PacketEvent.Receive.Pre event) {
      if (event.getPacket() instanceof SPacketSoundEffect) {
         SPacketSoundEffect packet = event.getPacket();

         if (packet.getSound() == SoundEvents.ENTITY_HORSE_GALLOP || packet.getSound() == SoundEvents.ENTITY_PIG_STEP) {
            printChatMessage("A rideable entity is walking at x= " + String.format("%.2f", packet.getX()) + " y=" + String.format("%.2f", packet.getY()) + " z=" + String.format("%.2f", packet.getZ()));
         }
      }

   }
}
