package me.eclipcen.butterflyclient.module.impl.service;

import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.event.client.KeyPressedEvent;
import me.eclipcen.butterflyclient.module.api.Module;
import me.eclipcen.butterflyclient.module.api.branches.ServiceMod;
import me.eclipcen.butterflyclient.module.api.branches.ToggleMod;
import net.futureclient.eventbus.SubscribeEvent;

public class EventMod extends ServiceMod {
   public EventMod() {
      super("EventMod");
   }

   @SubscribeEvent
   public void onKeyPress(KeyPressedEvent event) {
      for (Module mod : Butterfly.getInstance().getModuleManager().getMods()) {
         if (mod instanceof ToggleMod && event.getKey() == mod.getBind() && mod.getBind() != 0) {
            ((ToggleMod) mod).toggle();
         }
      }
   }
}
