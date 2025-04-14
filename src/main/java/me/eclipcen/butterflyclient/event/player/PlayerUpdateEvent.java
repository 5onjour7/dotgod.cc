package me.eclipcen.butterflyclient.event.player;

import me.eclipcen.butterflyclient.event.EventType;

public class PlayerUpdateEvent extends EventType {
   public PlayerUpdateEvent(EventType.Type type) {
      super(type);
   }
}
