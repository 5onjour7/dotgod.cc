package me.eclipcen.butterflyclient.event.client;

import net.futureclient.eventbus.EventCancelable;

public class SendChatMessageEvent extends EventCancelable {
   private final String message;

   public SendChatMessageEvent(String message) {
      this.message = message;
   }

   public String getMessage() {
      return message;
   }
}
