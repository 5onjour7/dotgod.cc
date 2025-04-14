package me.eclipcen.butterflyclient.event.gui;

import net.futureclient.eventbus.Event;
import net.minecraft.client.network.NetworkPlayerInfo;

public class TabOverlayEvent extends Event {
   public static class Icon extends TabOverlayEvent {
      private final NetworkPlayerInfo playerInfo;
      private final float x;
      private final float y;
      private final float width;

      public Icon(NetworkPlayerInfo playerInfo, float x, float y, float width) {
         this.playerInfo = playerInfo;
         this.x = x;
         this.y = y;
         this.width = width;
      }

      public NetworkPlayerInfo getPlayerInfo() {
         return playerInfo;
      }

      public float getX() {
         return x;
      }

      public float getY() {
         return y;
      }

      public float getWidth() {
         return width;
      }
   }

   public static class Font extends TabOverlayEvent {
      public String text;
      public float x;
      public float y;
      public int color;

      public Font(String text, float x, float y, int color) {
         this.text = text;
         this.x = x;
         this.y = y;
         this.color = color;
      }
   }

   public static class Size extends TabOverlayEvent {
      private int size;

      public Size(int size) {
         this.size = size;
      }

      public int getSize() {
         return size;
      }

      public void setSize(int size) {
         this.size = size;
      }
   }
}
