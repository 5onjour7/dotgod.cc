package me.eclipcen.butterflyclient.command.impl;

import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.command.api.Command;

public class SaveCommand extends Command {
   public SaveCommand() {
      super("save", 1, 1);
   }

   public void execute(String[] args) {
      Butterfly.getInstance().getSettingManager().saveAllAsync();
      printChatMessage("Saved config!");
   }
}
