package net.futureclient.interception.shared.network.packet.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.futureclient.interception.shared.network.packet.InterceptionPacket;

public final class ServerKeepAliveTime extends InterceptionPacket {
   private long id;
   private long time;

   public ServerKeepAliveTime() {
   }

   public ServerKeepAliveTime(long id, long time) {
      this.id = id;
      this.time = time;
   }

   @Override
   public void read(DataInputStream in) throws IOException {
      id = in.readLong();
      time = in.readLong();
   }

   @Override
   public void write(DataOutputStream out) throws IOException {
      out.writeLong(id);
      out.writeLong(time);
   }

   public long getId() {
      return id;
   }

   public long getTime() {
      return time;
   }
}
