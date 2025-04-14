package me.eclipcen.butterflyclient.util.colors.rainbow;

import java.awt.Color;
import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.module.impl.other.ColoursMod;
import me.eclipcen.butterflyclient.util.Globals;

public class RainbowUtils implements Globals {
   private static final ColoursMod colours = Butterfly.getInstance().getModuleManager().getModule(ColoursMod.class);

   public static Color getRainbowColor() {
      float speed = colours.rainbowSpeed.getValue() * 1000.0F;
      float hue = (float) (System.currentTimeMillis() % (int) speed) / speed;
      return Color.getHSBColor(hue, colours.saturation.getValue() * 0.003921569F, colours.brightness.getValue() * 0.003921569F);
   }

   public static Color getRainbowColorOffset(long offset) {
      float speed = colours.rainbowSpeed.getValue() * 1000.0F;
      float hue = (float) ((System.currentTimeMillis() + offset) % (int)speed) / speed;
      return Color.getHSBColor(hue, colours.saturation.getValue() * 0.003921569F, colours.brightness.getValue() * 0.003921569F);
   }
}
