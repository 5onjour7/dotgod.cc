package me.eclipcen.butterflyclient.event.gui;

import net.futureclient.eventbus.Event;
import net.minecraft.client.gui.GuiScreen;

public class GuiOpenEvent extends Event {
   private GuiScreen gui;

   public GuiOpenEvent(GuiScreen gui) {
      setGui(gui);
   }

   public GuiScreen getGui() {
      return gui;
   }

   public void setGui(GuiScreen gui) {
      this.gui = gui;
   }
}
