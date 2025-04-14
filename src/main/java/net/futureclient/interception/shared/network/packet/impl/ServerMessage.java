package net.futureclient.interception.shared.network.packet.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.futureclient.interception.shared.network.packet.InterceptionPacket;
import net.minecraft.util.text.ITextComponent;

public final class ServerMessage extends InterceptionPacket {
   private ServerMessage.Type type;
   private ITextComponent component;

   public ServerMessage() {
   }

   public ServerMessage(ServerMessage.Type type, ITextComponent component) {
      this.type = type;
      this.component = component;
   }

   @Override
   public void read(DataInputStream in) throws IOException {
      type = readEnumValue(in);
      component = readTextComponent(in);
   }

   @Override
   public void write(DataOutputStream out) throws IOException {
      writeEnumValue(out, type);
      writeTextComponent(out, component);
   }

   public ServerMessage.Type getType() {
      return type;
   }

   public ITextComponent getComponent() {
      return component;
   }

   public enum Type {
      GENERIC,
      WARNING,
      ERROR;
   }
}
