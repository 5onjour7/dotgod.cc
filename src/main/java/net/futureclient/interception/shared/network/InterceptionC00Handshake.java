package net.futureclient.interception.shared.network;

import io.netty.buffer.Unpooled;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.futureclient.interception.shared.util.ByteUtil;
import net.futureclient.interception.shared.util.CryptoUtil;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.handshake.client.C00Handshake;

public final class InterceptionC00Handshake extends C00Handshake {
   private String ip;
   private int port;
   private String interceptionIp;
   private int interceptionPort;
   private byte[] encryptionKey;
   private boolean interceptionClient;

   public InterceptionC00Handshake() {
   }

   public InterceptionC00Handshake(String ip, int port, EnumConnectionState state) {
      super(ip, port, state);
   }

   public static C00Handshake createInterceptionHandshake(
      String ip, int port, EnumConnectionState state, String interceptionIp, int interceptionPort, byte[] encryptionKey
   ) throws IOException {
      return new C00Handshake(mutateIp(ip, interceptionIp, interceptionPort, encryptionKey), port, state);
   }

   private static String mutateIp(String ip, String interceptionIp, int interceptionPort, byte[] encryptionKey) throws IOException {
      ByteArrayOutputStream bout = new ByteArrayOutputStream();
      DataOutputStream out = new DataOutputStream(bout);
      out.writeLong(InterceptionProtocolConstants.MAGIC_LONG);
      out.writeInt(0);
      byte[] iv = CryptoUtil.generateBytes(16);
      writeBytes(out, iv);
      ByteArrayOutputStream ebout = new ByteArrayOutputStream();
      DataOutputStream eout = new DataOutputStream(ebout);
      eout.writeUTF(interceptionIp);
      eout.writeShort(interceptionPort);
      writeBytes(eout, encryptionKey);
      byte[] data = ebout.toByteArray();

      byte[] encryptedData;
      try {
         encryptedData = CryptoUtil.encrypt(data, ByteUtil.hexStringToBytes("BCD166B90944DF6BE2C268E2EBEACBD4"), iv);
      } catch (Exception var12) {
         var12.printStackTrace();
         throw new IOException("Encryption failed");
      }

      writeBytes(out, encryptedData);
      return ip.concat("\u0000" + InterceptionProtocolConstants.MAGIC_STRING).concat(ByteUtil.bytesToHexString(bout.toByteArray()) + "\u0000");
   }

   public String getIp() {
      return ip;
   }

   public int getPort() {
      return port;
   }

   public String getInterceptionIp() {
      return interceptionIp;
   }

   public int getInterceptionPort() {
      return interceptionPort;
   }

   public byte[] getEncryptionKey() {
      return encryptionKey;
   }

   public boolean isInterceptionClient() {
      return interceptionClient;
   }

   public void readPacketData(PacketBuffer buf) throws IOException {
      PacketBuffer newBuf = new PacketBuffer(Unpooled.buffer());
      newBuf.writeVarInt(buf.readVarInt());
      String ipStr = buf.readString(32767);
      String[] magicSplit = ipStr.split("\u0000" + InterceptionProtocolConstants.MAGIC_STRING.replace("$", "\\$"));
      if (magicSplit.length > 1) {
         String ip = magicSplit[0];
         newBuf.writeString(ip = ip);

         try {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(ByteUtil.hexStringToBytes(magicSplit[1].split("\u0000")[0])));
            long magic = in.readLong();
            if (magic != InterceptionProtocolConstants.MAGIC_LONG) {
               throw new IOException("Bad magic: 0x" + Long.toString(magic, 16));
            }

            in.readInt();
            byte[] iv = readBytes(in);

            try {
               byte[] encryptedData = readBytes(in);
               in = new DataInputStream(new ByteArrayInputStream(CryptoUtil.decrypt(encryptedData, ByteUtil.hexStringToBytes("BCD166B90944DF6BE2C268E2EBEACBD4"), iv)));
            } catch (Exception var11) {
               var11.printStackTrace();
               throw new IOException("Decryption failed");
            }

            interceptionIp = in.readUTF();
            interceptionPort = in.readUnsignedShort();
            encryptionKey = readBytes(in);
            interceptionClient = true;
         } catch (Throwable var12) {
            var12.printStackTrace();
            throw var12;
         }
      } else {
         newBuf.writeString(ip = ipStr);
      }

      newBuf.writeShort(port = buf.readUnsignedShort());
      newBuf.writeVarInt(buf.readVarInt());
      super.readPacketData(newBuf);
   }

   private static void writeBytes(DataOutputStream out, byte[] bytes) throws IOException {
      if (bytes.length > Math.abs(-32768) + 32767) {
         throw new IOException("Unable to write " + bytes.length + " bytes because it was over the protocol limit!");
      } else {
         out.writeShort(bytes.length);
         out.write(bytes);
      }
   }

   private static byte[] readBytes(DataInputStream in) throws IOException {
      byte[] bytes = new byte[in.readUnsignedShort()];
      int readBytes = in.read(bytes, 0, bytes.length);
      if (readBytes != bytes.length) {
         throw new IOException(
            "Failed to read expected " + bytes.length + " bytes and read " + readBytes + " bytes instead! " + ByteUtil.bytesToHexString(bytes)
         );
      } else {
         return bytes;
      }
   }
}
