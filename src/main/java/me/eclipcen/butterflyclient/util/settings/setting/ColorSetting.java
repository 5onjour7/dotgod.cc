package me.eclipcen.butterflyclient.util.settings.setting;

import java.awt.Color;
import me.eclipcen.butterflyclient.util.colors.Colors;
import me.eclipcen.butterflyclient.util.colors.rainbow.RainbowUtils;
import me.eclipcen.butterflyclient.util.settings.Setting;

public class ColorSetting extends Setting<Color> {
   private boolean rainbow;
   private boolean global;
   private final boolean shouldRainbow;
   private final boolean shouldGlobal;

   public ColorSetting(String name, String desc, Color value) {
      super(name, desc, value);
      this.rainbow = false;
      this.global = false;
      this.shouldGlobal = true;
      this.shouldRainbow = true;
   }

   public ColorSetting(String name, String desc, Color value, boolean rainbow, boolean global) {
      super(name, desc, value);
      this.shouldRainbow = rainbow;
      this.shouldGlobal = global;
      this.rainbow = false;
      this.global = false;
   }

   public ColorSetting(String name, String desc, Color value, boolean rainbow, boolean global, boolean defaultGlobal) {
      super(name, desc, value);
      this.shouldRainbow = rainbow;
      this.shouldGlobal = global;
      this.rainbow = false;
      this.global = defaultGlobal;
   }

   public boolean getRainbowValue() {
      return rainbow;
   }

   public void setRainbowValue(boolean rainbow) {
      this.rainbow = rainbow;
   }

   public boolean getGlobalValue() {
      return global;
   }

   public void setGlobalValue(boolean global) {
      this.global = global;
   }

   public boolean getShouldRainbow() {
      return shouldRainbow;
   }

   public boolean getShouldGlobal() {
      return shouldGlobal;
   }

   public Color getValue() {
      if (getRainbowValue()) {
         Color rainbow = RainbowUtils.getRainbowColor();
         return new Color(rainbow.getRed(), rainbow.getGreen(), rainbow.getBlue(), value.getAlpha());
      } else if (getGlobalValue()) {
         Color global = Colors.getGlobalColor();
         return new Color(global.getRed(), global.getGreen(), global.getBlue(), value.getAlpha());
      } else {
         return value;
      }
   }
}
