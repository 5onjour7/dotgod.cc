package me.eclipcen.butterflyclient.clickgui.utils;

import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.module.impl.other.ColoursMod;
import me.eclipcen.butterflyclient.util.colors.Colors;

public class ColorUtils {
   private static final ColoursMod MODULE = Butterfly.getInstance().getModuleManager().getModule(ColoursMod.class);
   public static final int BUTTON_ON_OFF = Colors.toRGBA(150, 150, 150, 250);
   public static final int BUTTON_ON_ON = Colors.WHITE;

   public static int getColorForGuiEntry(int type, boolean hovered, boolean state) {
      int BUTTON2_OFF = Colors.toRGBA(0, 0, 0, 0);
      int BUTTON2_OFF_HOV = Colors.toRGBA(150, 150, 150, 50);
      int BUTTON2_ON = MODULE.global.getValue().getRGB();
      int BUTTON2_ON_HOV = MODULE.global.getValue().getRGB();
      switch (type) {
         case 0:
            if (hovered) {
               if (!state) {
                  return BUTTON_ON_OFF;
               }

               return BUTTON_ON_ON;
            } else {
               if (!state) {
                  return MODULE.disabled.getValue().getRGB();
               }

               return MODULE.active.getValue().getRGB();
            }
         case 1:
            return MODULE.window.getValue().getRGB();
         case 2:
            if (!hovered) {
               return BUTTON2_ON;
            }

            return BUTTON2_ON_HOV;
         case 3:
            if (!hovered) {
               if (!state) {
                  return BUTTON2_OFF;
               }

               return BUTTON2_ON;
            } else {
               if (!state) {
                  return BUTTON2_OFF_HOV;
               }

               return BUTTON2_ON_HOV;
            }
         case 4:
            if (!state) {
               return MODULE.boxDisabled.getValue().getRGB();
            }

            return MODULE.boxActive.getValue().getRGB();
         default:
            throw new IllegalStateException("Invalid type: " + type);
      }
   }
}
