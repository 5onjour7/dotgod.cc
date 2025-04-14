package me.eclipcen.butterflyclient.command.impl;

import java.util.Iterator;
import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.command.api.Command;

public class HelpCommand extends Command {
   public HelpCommand() {
      super("help", 1, 1);
   }

   public void execute(String[] args) {
      StringBuilder toSend = new StringBuilder();

      for (String command : Butterfly.getInstance().getCommandManager().getCommandNames().keySet()) {
         if (Butterfly.getInstance().getModuleManager().getModuleFromName(command) == null) {
            toSend.append(command).append(", ");
         }
      }

      toSend.append("modulename").append(", ");
      toSend = new StringBuilder(toSend.substring(0, toSend.length() - 2));

      printChatMessage(toSend.toString());
   }
}
