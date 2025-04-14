package me.eclipcen.butterflyclient.event.client;

import me.eclipcen.butterflyclient.module.api.Module;
import net.futureclient.eventbus.Event;

public class ModuleEvent extends Event {
   private final Module module;

   public ModuleEvent(Module module) {
      this.module = module;
   }

   public Module getModule() {
      return this.module;
   }

   public static class Stop extends ModuleEvent {
      public Stop(Module module) {
         super(module);
      }
   }

   public static class Start extends ModuleEvent {
      public Start(Module module) {
         super(module);
      }
   }
}
