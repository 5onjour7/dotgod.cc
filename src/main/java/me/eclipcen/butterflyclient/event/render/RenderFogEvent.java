package me.eclipcen.butterflyclient.event.render;

import net.futureclient.eventbus.EventCancelable;

public class RenderFogEvent extends EventCancelable {
   public static class Colour extends RenderFogEvent {
      private float red;
      private float green;
      private float blue;

      public Colour(float red, float green, float blue) {
         this.red = red;
         this.green = green;
         this.blue = blue;
      }

      public float getRed() {
         return red;
      }

      public float getGreen() {
         return green;
      }

      public float getBlue() {
         return blue;
      }

      public void setRed(float red) {
         this.red = red;
      }

      public void setGreen(float green) {
         this.green = green;
      }

      public void setBlue(float blue) {
         this.blue = blue;
      }
   }

   public static class Density extends RenderFogEvent {
      private float density;

      public Density(float density) {
         this.density = density;
      }

      public float getDensity() {
         return density;
      }

      public void setDensity(float density) {
         this.density = density;
      }
   }
}
