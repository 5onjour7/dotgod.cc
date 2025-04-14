package me.eclipcen.butterflyclient.event.gui;

import net.futureclient.eventbus.EventCancelable;
import net.minecraft.item.ItemStack;

public class RenderToolTipEvent extends EventCancelable {
   private final ItemStack stack;

   public RenderToolTipEvent(ItemStack stack) {
      this.stack = stack;
   }

   public ItemStack getStack() {
      return stack;
   }
}
