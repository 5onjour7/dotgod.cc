package me.eclipcen.butterflyclient.event.player;

import net.futureclient.eventbus.Event;

public class PlayerSprintEvent extends Event {
   private boolean sprinting;

   public PlayerSprintEvent(boolean sprinting) {
      this.sprinting = sprinting;
   }

   public boolean isSprinting() {
      return sprinting;
   }

   public void setSprinting(boolean sprinting) {
      this.sprinting = sprinting;
   }
}
