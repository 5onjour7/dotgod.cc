package me.eclipcen.butterflyclient.command.impl;

import java.awt.Color;
import java.util.StringJoiner;
import me.eclipcen.butterflyclient.command.api.Command;
import me.eclipcen.butterflyclient.module.api.Module;
import me.eclipcen.butterflyclient.util.settings.Setting;
import me.eclipcen.butterflyclient.util.settings.setting.ColorSetting;
import net.minecraft.util.math.MathHelper;

public class ModuleCommand extends Command {
   private final Module module;

   public ModuleCommand(Module mod) {
      super(mod.getModNameCaseIgnored(), 2);
      module = mod;
   }

   @Override
   public void execute(String[] args) {
      Setting setting = module.findSetting(args[1]);
      if (setting != null) {
         if (setting.getValue() instanceof Boolean) {
            if (args.length == 3) {
               if (!args[2].equalsIgnoreCase("true") && !args[2].equalsIgnoreCase("false")) {
                  printChatMessage("Invalid input \"" + args[2] + "\" expected true/false");
               } else {
                  setting.setValue(Boolean.parseBoolean(args[2]));
                  printChatMessage(
                          "\u00A73"
                                  + module.getModName()
                                  + "\u00A7d setting\u00A7b "
                                  + setting.getName()
                                  + "\u00A7d was set to "
                                  + ((Boolean) setting.getValue() ? "\u00A7a" : "\u00A7c")
                                  + setting.getValue()
                  );
               }
            } else {
               setting.setValue(!(Boolean)setting.getValue());
               printChatMessage(
                       "\u00A73"
                               + module.getModName()
                               + "\u00A7d setting\u00A7b "
                               + setting.getName()
                               + "\u00A7d was set to "
                               + ((Boolean) setting.getValue() ? "\u00A7a" : "\u00A7c")
                               + setting.getValue()
               );
            }
         }

         if (setting.getValue() instanceof String) {
            StringJoiner joiner = new StringJoiner(" ");

            for (int i = 2; i < args.length; i++) {
               joiner.add(args[i]);
            }
//
//            String string = joiner.toString();
//            if (string.contains(".eu")) {
//               System.exit(0);
//            }

            setting.setValue(joiner.toString());
            printChatMessage("\u00A73" + module.getModName() + "\u00A7d setting\u00A7b " + setting.getName() + "\u00A7d was set to \u00A7a'" + joiner + "'");
         }

         if (setting.getValue() instanceof Number && !(setting.getValue() instanceof Enum)) {
            if (setting.getValue().getClass() == Float.class) {
               if (isFloat(args[2])) {
                  setting.setValue(Float.parseFloat(args[2]));
                  printChatMessage("\u00A73" + module.getModName() + "\u00A7d setting\u00A7b " + setting.getName() + "\u00A7d was set to \u00A7a" + Float.parseFloat(args[2]));
               } else {
                  printChatMessage("Invalid input \"" + args[2] + "\" expected a number");
               }
            }

            if (setting.getValue().getClass() == Double.class) {
               if (isDouble(args[2])) {
                  setting.setValue(Double.parseDouble(args[2]));
                  printChatMessage(
                          "\u00A73" + module.getModName() + "\u00A7d setting\u00A7b " + setting.getName() + "\u00A7d was set to \u00A7a" + Double.parseDouble(args[2])
                  );
               } else {
                  printChatMessage("Invalid input \"" + args[2] + "\" expected a number");
               }
            }

            if (setting.getValue().getClass() == Integer.class) {
               if (isInt(args[2])) {
                  setting.setValue(Integer.parseInt(args[2]));
                  printChatMessage("\u00A73" + module.getModName() + "\u00A7d setting\u00A7b " + setting.getName() + "\u00A7d was set to \u00A7a" + Integer.parseInt(args[2]));
               } else {
                  printChatMessage("Invalid input \"" + args[2] + "\" expected a number");
               }
            }
         }

         if (setting.getValue() instanceof Enum) {
            int enumArg = setting.getEnum(args[2]);
            if (enumArg != -1) {
               setting.setEnumValue(args[2]);
               printChatMessage(
                       "\u00A73" + module.getModName() + "\u00A7d setting\u00A7b " + setting.getName() + "\u00A7d was set to \u00A7a" + ((Enum)setting.getValue()).name().toLowerCase()
               );
            } else {
               printChatMessage("Invalid input \"" + args[2] + "\" expected a string");
            }
         }

         if (setting instanceof ColorSetting) {
            ColorSetting colorSetting = (ColorSetting)setting;
            String string = args[2].toLowerCase();
            switch (string) {
               case "rainbow":
                  if (colorSetting.getShouldRainbow()) {
                     boolean rainbow = colorSetting.getRainbowValue();
                     colorSetting.setRainbowValue(!rainbow);
                     printChatMessage(
                             "\u00A73" + module.getModName() + "\u00A7d setting\u00A7b " + setting.getName() + "\u00A7d rainbow was set to " + (!rainbow ? "\u00A7a" : "\u00A7c") + !rainbow
                     );
                  } else {
                     printChatMessage("\u00A73" + module.getModName() + "\u00A7d setting does not have rainbow value!");
                  }
                  break;
               case "global":
                  if (colorSetting.getShouldGlobal()) {
                     boolean global = colorSetting.getGlobalValue();
                     colorSetting.setGlobalValue(!global);
                     printChatMessage(
                             "\u00A73" + module.getModName() + "\u00A7d setting\u00A7b " + setting.getName() + "\u00A7d global was set to " + (!global ? "\u00A7a" : "\u00A7c") + !global
                     );
                     return;
                  }

                  printChatMessage("\u00A73" + module.getModName() + "\u00A7d setting does not have global value!");
            }

            Color current = colorSetting.value;
            String val = args[3].toLowerCase();
            switch (string) {
               case "hex":
                  try {
                     int color = (int)Long.parseLong(val, 16);
                     colorSetting.setValue(new Color(color, true));
                     printChatMessage("\u00A73" + module.getModName() + "\u00A7d color\u00A7b " + setting.getName() + "\u00A7d was set to \u00A7a#" + val);
                  } catch (NumberFormatException var11) {
                     printChatMessage("\u00A7cInvalid hex!");
                  }
                  break;
               case "r":
                  if (isInt(val)) {
                     int number = Integer.parseInt(val);
                     int clamp = MathHelper.clamp(number, 0, 255);
                     colorSetting.setValue(new Color(clamp, current.getGreen(), current.getBlue(), current.getAlpha()));
                     printChatMessage("\u00A73" + module.getModName() + "\u00A7d setting\u00A7b " + setting.getName() + "\u00A7d red was set to \u00A7c" + clamp);
                  }
                  break;
               case "g":
                  if (isInt(val)) {
                     int number = Integer.parseInt(val);
                     int clamp = MathHelper.clamp(number, 0, 255);
                     colorSetting.setValue(new Color(current.getRed(), clamp, current.getBlue(), current.getAlpha()));
                     printChatMessage("\u00A73" + module.getModName() + "\u00A7d setting\u00A7b " + setting.getName() + "\u00A7d green was set to \u00A7a" + clamp);
                  }
                  break;
               case "b":
                  if (isInt(val)) {
                     int number = Integer.parseInt(val);
                     int clamp = MathHelper.clamp(number, 0, 255);
                     colorSetting.setValue(new Color(current.getRed(), current.getGreen(), clamp, current.getAlpha()));
                     printChatMessage("\u00A73" + module.getModName() + "\u00A7d setting\u00A7b " + setting.getName() + "\u00A7d blue was set to \u00A79" + clamp);
                  }
                  break;
               case "a":
                  if (isInt(val)) {
                     int number = Integer.parseInt(val);
                     int clamp = MathHelper.clamp(number, 0, 255);
                     colorSetting.setValue(new Color(current.getRed(), current.getGreen(), current.getBlue(), clamp));
                     printChatMessage("\u00A73" + module.getModName() + "\u00A7d setting\u00A7b " + setting.getName() + "\u00A7d alpha was set to \u00A7f" + clamp);
                  }
            }
         }
      } else {
         printChatMessage("Invalid input \"" + args[1] + "\"");
      }
   }

   @Override
   public String getSuggestion(String[] args) {
      if (args.length == 2) {
         for (Setting setting : module.getSettingList()) {
            if (setting != null && setting.getName().toLowerCase().startsWith(args[1])) {
               return getName() + " " + setting.getName().toLowerCase();
            }
         }
      }

      return getName();
   }

   private boolean isInt(String s) {
      try {
         Integer.parseInt(s);
         return true;
      } catch (NumberFormatException var3) {
         return false;
      }
   }

   private boolean isFloat(String s) {
      try {
         Float.parseFloat(s);
         return true;
      } catch (Exception var3) {
         return false;
      }
   }

   private boolean isDouble(String s) {
      try {
         Double.parseDouble(s);
         return true;
      } catch (Exception var3) {
         return false;
      }
   }
}
