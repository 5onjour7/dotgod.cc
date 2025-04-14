package net.futureclient.interception.shared.network.packet.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.futureclient.interception.shared.attribute.AttributeManager;
import net.futureclient.interception.shared.network.packet.InterceptionPacket;

public final class SharedAttributeSync extends InterceptionPacket {
   private byte[] attributesBytes;

   public SharedAttributeSync() {
   }

   public SharedAttributeSync(AttributeManager attributeManager, boolean full) throws IOException {
      attributesBytes = attributeManager.writeAttributes(attributeManager.sync(full));
   }

   @Override
   public void read(DataInputStream in) throws IOException {
      attributesBytes = readBytes(in);
   }

   @Override
   public void write(DataOutputStream out) throws IOException {
      writeBytes(out, attributesBytes);
   }

   public byte[] getAttributesBytes() {
      return attributesBytes;
   }
}
