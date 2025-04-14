package me.eclipcen.butterflyclient.command.impl;

import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.command.api.Command;
import me.eclipcen.butterflyclient.module.api.Module;
import me.eclipcen.butterflyclient.module.api.ModuleManager;
import me.eclipcen.butterflyclient.module.api.branches.ToggleMod;

public class DrawnCommand extends Command {
   public DrawnCommand() {
      super("drawn", 2, 2);
   }

   public void execute(String[] args) {
      ModuleManager moduleManager = Butterfly.getInstance().getModuleManager();
      Module module = moduleManager.getModuleFromName(args[1]);

      if (module == null) {
         printChatMessage("\u00A74Could not find module: " + args[1]);
      } else {
         if (module instanceof ToggleMod) {
            module.drawn.setValue(!module.drawn.getValue());
            String tog = module.drawn.getValue() ? "\u00A7a set visible." : "\u00A7c set hidden.";
            printChatMessage("\u00A73" + module.getModName() + "\u00A7d was" + tog);
         }

      }
   }

   public String getSuggestion(String[] args) {
      if (args.length == 2) {
         for (Module module : Butterfly.getInstance().getModuleManager().getMods()) {
            if (module instanceof ToggleMod && module.getModNameCaseIgnored().startsWith(args[1])) {
               return getName() + " " + module.getModNameCaseIgnored();
            }
         }
      }

      return getName();
   }
}
