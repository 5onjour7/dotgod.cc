package me.eclipcen.butterflyclient.event.client;

import net.futureclient.eventbus.Event;

public class FpsEvent extends Event {
   private int fps;

   public int getFps() {
      return fps;
   }

   public void setFps(int fps) {
      this.fps = fps;
   }
}
