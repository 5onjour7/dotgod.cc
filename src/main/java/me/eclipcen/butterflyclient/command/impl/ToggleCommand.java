package me.eclipcen.butterflyclient.command.impl;

import java.util.Iterator;
import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.command.api.Command;
import me.eclipcen.butterflyclient.module.api.Module;
import me.eclipcen.butterflyclient.module.api.ModuleManager;
import me.eclipcen.butterflyclient.module.api.branches.ToggleMod;

public class ToggleCommand extends Command {
   public ToggleCommand() {
      super("toggle", new String[]{"t", "tog"}, 2, 2);
   }

   public void execute(String[] args) {
      ModuleManager moduleManager = Butterfly.getInstance().getModuleManager();
      Module module = moduleManager.getModuleFromName(args[1]);

      if (module == null) {
         printChatMessage("Could not find module: " + args[1]);
      } else {
         if (module instanceof ToggleMod) {
            ToggleMod toggleMod = (ToggleMod)module;
            toggleMod.toggle();
            String tog = toggleMod.isEnabled() ? "\u00A7aOn" : "\u00A7cOff";
            printChatMessage("\u00A73" + toggleMod.getModName() + "\u00A7d was toggled (" + tog + "\u00A7d)");
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
