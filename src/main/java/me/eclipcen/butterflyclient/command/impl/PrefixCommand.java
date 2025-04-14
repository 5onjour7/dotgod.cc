package me.eclipcen.butterflyclient.command.impl;

import me.eclipcen.butterflyclient.command.api.Command;
import me.eclipcen.butterflyclient.module.impl.service.CommandService;

public class PrefixCommand extends Command {
   public PrefixCommand() {
      super("prefix", 2, 2);
   }

   public void execute(String[] args) {
      CommandService.prefix.setValue(args[1]);
      printChatMessage("Set command prefix to: \u00A74" + CommandService.prefix.getValue());
   }
}
