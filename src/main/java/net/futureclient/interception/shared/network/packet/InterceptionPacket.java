package net.futureclient.interception.shared.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.futureclient.interception.shared.util.ByteUtil;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextComponent.Serializer;

public abstract class InterceptionPacket {
   public abstract void read(DataInputStream var1) throws IOException;

   public abstract void write(DataOutputStream var1) throws IOException;

   protected void writeBytes(DataOutputStream out, byte[] bytes) throws IOException {
      if (bytes.length > Math.abs(-32768) + 32767) {
         throw new IOException("Unable to write " + bytes.length + " bytes because it was over the protocol limit!");
      } else {
         out.writeShort(bytes.length);
         out.write(bytes);
      }
   }

   protected byte[] readBytes(DataInputStream in) throws IOException {
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

   protected <T extends Enum<T>> T readEnumValue(DataInputStream in) throws IOException {
      return ((Class<T>) net.futureclient.interception.shared.network.packet.impl.ServerMessage.Type.class).getEnumConstants()[in.readInt()];
   }

   protected void writeEnumValue(DataOutputStream out, Enum<?> value) throws IOException {
      out.writeInt(value.ordinal());
   }

   protected ITextComponent readTextComponent(DataInputStream in) throws IOException {
      return Serializer.jsonToComponent(in.readUTF());
   }

   protected void writeTextComponent(DataOutputStream out, ITextComponent component) throws IOException {
      out.writeUTF(Serializer.componentToJson(component));
   }
}
