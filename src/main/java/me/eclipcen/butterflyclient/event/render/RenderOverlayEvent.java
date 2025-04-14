package me.eclipcen.butterflyclient.event.render;

import net.futureclient.eventbus.EventCancelable;

public class RenderOverlayEvent extends EventCancelable {
   private final RenderOverlayEvent.OverlayType type;

   public RenderOverlayEvent(RenderOverlayEvent.OverlayType type) {
      this.type = type;
   }

   public RenderOverlayEvent.OverlayType getType() {
      return type;
   }

   public enum OverlayType {
      BLOCK,
      LIQUID,
      FIRE;
   }
}
