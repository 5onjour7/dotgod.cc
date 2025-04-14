package me.eclipcen.butterflyclient.event.render;

import net.futureclient.eventbus.Event;

public class RenderItemGlintEffectEvent extends Event {
   private int color;

   public RenderItemGlintEffectEvent(int color) {
      this.color = color;
   }

   public int getColor() {
      return color;
   }

   public void setColor(int color) {
      this.color = color;
   }
}
