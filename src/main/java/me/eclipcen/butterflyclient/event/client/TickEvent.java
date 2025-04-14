package me.eclipcen.butterflyclient.event.client;

import net.futureclient.eventbus.Event;

public class TickEvent extends Event {
   public static class Post extends TickEvent {
   }

   public static class Pre extends TickEvent {
   }
}
