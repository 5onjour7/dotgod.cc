package me.eclipcen.butterflyclient.event.client;

import net.futureclient.eventbus.Event;

public class KeyPressedEvent extends Event {
   private int key;

   public KeyPressedEvent(int key) {
      this.key = key;
   }

   public int getKey() {
      return key;
   }

   public void setKey(int key) {
      this.key = key;
   }
}
