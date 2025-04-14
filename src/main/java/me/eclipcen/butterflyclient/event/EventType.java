package me.eclipcen.butterflyclient.event;

import net.futureclient.eventbus.EventCancelable;

public abstract class EventType extends EventCancelable {
   private Type type;

   public EventType(Type type) {
      this.type = type;
   }

   public EventType() {
   }

   public Type getType() {
      return type;
   }

   public enum Type {
      PRE,
      POST;
   }
}
