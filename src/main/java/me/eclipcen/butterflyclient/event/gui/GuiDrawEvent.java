package me.eclipcen.butterflyclient.event.gui;

import net.futureclient.eventbus.Event;

public class GuiDrawEvent extends Event {
   private final int mouseX;
   private final int mouseY;
   private final float partialTicks;

   public GuiDrawEvent(int mouseX, int mouseY, float partialTicks) {
      this.mouseX = mouseX;
      this.mouseY = mouseY;
      this.partialTicks = partialTicks;
   }

   public int getMouseX() {
      return mouseX;
   }

   public int getMouseY() {
      return mouseY;
   }

   public float getPartialTicks() {
      return partialTicks;
   }
}
