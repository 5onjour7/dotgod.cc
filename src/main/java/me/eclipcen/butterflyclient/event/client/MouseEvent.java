package me.eclipcen.butterflyclient.event.client;

import net.futureclient.eventbus.Event;

public class MouseEvent extends Event {
   private final int buttonID;

   public MouseEvent(int buttonID) {
      this.buttonID = buttonID;
   }

   public int getButtonID() {
      return buttonID;
   }
}
