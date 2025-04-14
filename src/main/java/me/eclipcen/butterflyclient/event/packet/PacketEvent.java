package me.eclipcen.butterflyclient.event.packet;

import net.futureclient.eventbus.EventCancelable;
import net.minecraft.network.Packet;

public class PacketEvent extends EventCancelable {
   private final Packet<?> packet;

   public PacketEvent(Packet<?> packetIn) {
      this.packet = packetIn;
   }

   public <T extends Packet<?>> T getPacket() {
      return (T) packet;
   }

   public static class Send extends PacketEvent {
      public Send(Packet<?> packetIn) {
         super(packetIn);
      }

      public static class Post extends PacketEvent.Send {
         public Post(Packet<?> packetIn) {
            super(packetIn);
         }
      }

      public static class Pre extends PacketEvent.Send {
         public Pre(Packet<?> packetIn) {
            super(packetIn);
         }
      }
   }

   public static class Receive extends PacketEvent {
      public Receive(Packet<?> packetIn) {
         super(packetIn);
      }

      public static class Post extends PacketEvent.Receive {
         public Post(Packet<?> packetIn) {
            super(packetIn);
         }
      }

      public static class Pre extends PacketEvent.Receive {
         public Pre(Packet<?> packetIn) {
            super(packetIn);
         }
      }
   }
}
