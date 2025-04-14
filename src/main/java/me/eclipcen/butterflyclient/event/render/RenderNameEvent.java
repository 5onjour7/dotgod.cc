package me.eclipcen.butterflyclient.event.render;

import net.futureclient.eventbus.EventCancelable;
import net.minecraft.entity.Entity;

public class RenderNameEvent extends EventCancelable {
   private final Entity entity;

   public RenderNameEvent(Entity entity) {
      this.entity = entity;
   }

   public Entity getEntity() {
      return entity;
   }
}
