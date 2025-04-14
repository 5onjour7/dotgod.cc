package me.eclipcen.butterflyclient.clickgui.utils;

import me.eclipcen.butterflyclient.util.math.MathUtil;
import me.eclipcen.butterflyclient.util.render.RenderUtil;

public class GuiUtils {
   public static double roundSliderForConfig(double val) {
      return Double.parseDouble(MathUtil.getRounded(val));
   }

   public static float roundSliderStep(float input, float step) {
      return (float) Math.round(input / step) * step;
   }

   public static float reCheckSliderRange(float value, float min, float max) {
      return Math.min(Math.max(value, min), max);
   }

   public static void drawRect(int x, int y, int w, int h, int color) {
      RenderUtil.drawRect((float) x, (float) y, (float) (x + w), (float) (y + h), color);
   }
}
