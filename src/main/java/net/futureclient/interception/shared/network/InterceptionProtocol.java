package net.futureclient.interception.shared.network;

import io.netty.buffer.Unpooled;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.futureclient.interception.shared.network.packet.InterceptionPacket;
import net.futureclient.interception.shared.network.packet.impl.ServerKeepAliveTime;
import net.futureclient.interception.shared.network.packet.impl.ServerMessage;
import net.futureclient.interception.shared.network.packet.impl.SharedAttributeSync;
import net.futureclient.interception.shared.util.SneakyThrowUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.server.SPacketCustomPayload;

public final class InterceptionProtocol {
   private static final Map<Integer, Class<? extends InterceptionPacket>> ID_TO_PACKET = new HashMap<>();
   private static final Map<Class<? extends InterceptionPacket>, Integer> PACKET_TO_ID = new HashMap<>();

   public static InterceptionPacket readServer(CPacketCustomPayload payload) throws IOException {
      if (!InterceptionProtocolConstants.MAGIC_STRING.equals(payload.getChannelName())) {
         return null;
      } else {
         PacketBuffer buf = payload.getBufferData();
         byte[] bytes = new byte[buf.readableBytes()];
         buf.readBytes(bytes);
         return read0(bytes);
      }
   }

   public static InterceptionPacket readClient(SPacketCustomPayload payload) throws IOException {
      if (!InterceptionProtocolConstants.MAGIC_STRING.equals(payload.getChannelName())) {
         return null;
      } else {
         PacketBuffer buf = payload.getBufferData();
         byte[] bytes = new byte[buf.readableBytes()];
         buf.readBytes(bytes);
         return read0(bytes);
      }
   }

   private static InterceptionPacket read0(byte[] bytes) throws IOException {
      DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));
      long magic = in.readLong();
      if (magic != InterceptionProtocolConstants.MAGIC_LONG) {
         throw new IOException("Bad magic: 0x" + Long.toString(magic, 16));
      } else {
         in.readInt();
         int id = in.readInt();
         InterceptionPacket packet = newPacket(id);
         if (packet == null) {
            throw new IOException("Unable to read unregistered packet " + id);
         } else {
            packet.read(in);
            return packet;
         }
      }
   }

   public static SPacketCustomPayload writeServer(InterceptionPacket packet) throws IOException {
      return new SPacketCustomPayload(InterceptionProtocolConstants.MAGIC_STRING, write0(packet));
   }

   public static CPacketCustomPayload writeClient(InterceptionPacket packet) throws IOException {
      return new CPacketCustomPayload(InterceptionProtocolConstants.MAGIC_STRING, write0(packet));
   }

   private static PacketBuffer write0(InterceptionPacket packet) throws IOException {
      Integer id = PACKET_TO_ID.get(packet.getClass());
      if (id == null) {
         throw new IOException("Unable to write unregistered packet " + packet);
      } else {
         ByteArrayOutputStream bout = new ByteArrayOutputStream();
         DataOutputStream out = new DataOutputStream(bout);

         out.writeLong(InterceptionProtocolConstants.MAGIC_LONG);
         out.writeInt(0);
         out.writeInt(id);

         packet.write(out);
         PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
         buf.writeBytes(bout.toByteArray());
         return buf;
      }
   }

   private static InterceptionPacket newPacket(int id) {
      Class<? extends InterceptionPacket> clazz = ID_TO_PACKET.get(id);
      if (clazz == null) {
         return null;
      } else {
         try {
            return clazz.newInstance();
         } catch (IllegalAccessException | InstantiationException var3) {
            SneakyThrowUtil.throwSneaky(var3);
            throw null;
         }
      }
   }

   private static void register(int id, Class<? extends InterceptionPacket> clazz) {
      ID_TO_PACKET.put(id, clazz);
      PACKET_TO_ID.put(clazz, id);
   }

   static {
      register(0, ServerMessage.class);
      register(1, ServerKeepAliveTime.class);
      register(3, SharedAttributeSync.class);
   }
}
