package me.eclipcen.butterflyclient.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextComponentString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface Globals {
   Minecraft MC = Minecraft.getMinecraft();
   Logger LOGGER = LogManager.getLogger("butterfly");
   String CHAT_PREFIX = "\u00A75[\u00A7dDotGod.CC\u00A75]\u00A7d ";
   float NORM = 0.003921569F;
   EnumFacing[] ENUM_FACINGS = EnumFacing.values();

   default void printChatMessage(String string) {
      if (MC.world != null) {
         MC.ingameGUI.getChatGUI().printChatMessage(new TextComponentString("\u00A75[\u00A7dDotGod.CC\u00A75]\u00A7d " + string));
      }

   }

   default void printChatMessageWithDelete(String string) {
      this.printChatMessageWithDelete(string, -9);
   }

   default void printChatMessageWithDelete(String string, int id) {
      if (MC.world != null) {
         MC.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString("\u00A75[\u00A7dDotGod.CC\u00A75]\u00A7d " + string), id);
      }

   }
}
