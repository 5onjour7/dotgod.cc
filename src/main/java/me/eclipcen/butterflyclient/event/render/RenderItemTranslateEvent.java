package me.eclipcen.butterflyclient.event.render;

import net.futureclient.eventbus.Event;
import net.minecraft.util.EnumHandSide;

public class RenderItemTranslateEvent extends Event {
   private final EnumHandSide handSide;

   public RenderItemTranslateEvent(EnumHandSide handSide) {
      this.handSide = handSide;
   }

   public EnumHandSide getHandSide() {
      return handSide;
   }
}
