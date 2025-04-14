package me.eclipcen.butterflyclient.command.impl;

import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.command.api.Command;

public class ReloadCommand extends Command {
   public ReloadCommand() {
      super("reload", 1, 1);
   }

   public void execute(String[] args) {
      Butterfly.getInstance().getSettingManager().loadAll();
      printChatMessage("Successfully reloaded config!");
   }
}
