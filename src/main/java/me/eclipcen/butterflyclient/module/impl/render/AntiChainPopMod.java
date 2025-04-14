package me.eclipcen.butterflyclient.module.impl.render;

import me.eclipcen.butterflyclient.event.EventType;
import me.eclipcen.butterflyclient.event.player.PlayerUpdateEvent;
import me.eclipcen.butterflyclient.module.api.Category;
import me.eclipcen.butterflyclient.module.api.branches.ToggleMod;
import net.futureclient.eventbus.SubscribeEvent;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiScreen;

public class AntiChainPopMod extends ToggleMod {
   public AntiChainPopMod() {
      super(Category.RENDER, "AntiChainPop", "Removes fake deathscreen bug.");
   }

   @SubscribeEvent
   public void onUpdate(PlayerUpdateEvent event) {
      if (event.getType() == EventType.Type.PRE) {
         if (mc.player.getHealth() > 0.0F && mc.currentScreen instanceof GuiGameOver) {
            mc.displayGuiScreen(null);
         }
      }
   }
}
