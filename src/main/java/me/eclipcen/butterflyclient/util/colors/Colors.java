package me.eclipcen.butterflyclient.util.colors;

import java.awt.Color;
import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.module.impl.other.ColoursMod;

public class Colors {
   private static final ColoursMod COLOUR_MODULE = Butterfly.getInstance().getModuleManager().getModule(ColoursMod.class);
   public static final int WHITE = toRGBA(255, 255, 255, 255);
   public static final int BLACK = toRGBA(0, 0, 0, 255);

   public static Color getGlobalColor() {
      return new Color(COLOUR_MODULE.global.getValue().getRed(), COLOUR_MODULE.global.getValue().getGreen(), COLOUR_MODULE.global.getValue().getBlue());
   }

   public static int getColor(Color color) {
      return getColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
   }

   public static int getColor(int brightness) {
      return getColor(brightness, brightness, brightness, 255);
   }

   public static int getColor(int brightness, int alpha) {
      return getColor(brightness, brightness, brightness, alpha);
   }

   public static int getColor(int red, int green, int blue) {
      return getColor(red, green, blue, 255);
   }

   public static int getColor(int red, int green, int blue, int alpha) {
      int color = 0;
      color |= alpha << 24;
      color |= red << 16;
      color |= green << 8;
      return color | blue;
   }


   public static int toRGBA(int r, int g, int b, int a) {
      return (r << 16) + (g << 8) + b + (a << 24);
   }

   public static int toRGBA(float r, float g, float b, float a) {
      return toRGBA((int) (r * 255.0F), (int) (g * 255.0F), (int) (b * 255.0F), (int) (a * 255.0F));
   }

   public static int toRGBA(float[] colors) {
      if (colors.length == 4) {
         return toRGBA(colors[0], colors[1], colors[2], colors[3]);
      } else {
         throw new IllegalArgumentException("colors[] must have a length of 4!");
      }
   }

   public static int toRGBA(double[] colors) {
      if (colors.length == 4) {
         return toRGBA((float)colors[0], (float)colors[1], (float)colors[2], (float)colors[3]);
      } else {
         throw new IllegalArgumentException("colors[] must have a length of 4!");
      }
   }

   public static int[] toRGBAArray(int colorBuffer) {
      return new int[]{colorBuffer >> 16 & 255, colorBuffer >> 8 & 255, colorBuffer & 255, colorBuffer >> 24 & 255};
   }

   public static int changeAlpha(int origColor, int userInputtedAlpha) {
      origColor &= 16777215;
      return userInputtedAlpha << 24 | origColor;
   }

   public static String getHexString(int value) {
      StringBuilder builder = new StringBuilder(Integer.toHexString(value));

      while(builder.length() < 8) {
         builder.insert(0, 0);
      }

      return builder.toString();
   }
}
