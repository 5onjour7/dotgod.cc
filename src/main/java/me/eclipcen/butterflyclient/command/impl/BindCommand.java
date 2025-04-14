package me.eclipcen.butterflyclient.command.impl;

import java.util.Iterator;
import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.command.api.Command;
import me.eclipcen.butterflyclient.module.api.Module;
import me.eclipcen.butterflyclient.module.api.branches.ToggleMod;
import org.lwjgl.input.Keyboard;

public class BindCommand extends Command {
   public BindCommand() {
      super("bind", 3, 3);
   }

   public void execute(String[] args) {
      Module mod = Butterfly.getInstance().getModuleManager().getModuleFromName(args[1]);
      if (mod != null) {
         if (args[2].equalsIgnoreCase(Keyboard.getKeyName(mod.getBind()))) {
            printChatMessage("\u00A73 " + mod.getModName() + "'s\u00A7d key is already \u00A7a" + args[2].toUpperCase());
         } else if (args[2].equalsIgnoreCase("NONE")) {
            printChatMessage("Bound\u00A73 " + mod.getModName() + "\u00A7d to \u00A7a" + args[2].toUpperCase());
            mod.setBind(args[2].toUpperCase());
         } else if (Keyboard.getKeyIndex(args[2].toUpperCase()) != 0) {
            printChatMessage("Bound\u00A73 " + mod.getModName() + "\u00A7d to \u00A7a" + args[2].toUpperCase());
            mod.setBind(args[2].toUpperCase());
         } else {
            printChatMessage("\u00A7c" + args[2] + "\u00A7f is not a valid key");
         }
      } else {
         printChatMessage("Unknown module \u00A73\"" + args[1] + "\"");
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
