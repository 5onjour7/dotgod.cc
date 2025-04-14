package me.eclipcen.butterflyclient.event.entity;

import net.futureclient.eventbus.Event;
import net.minecraft.entity.Entity;

public class EntityEvent extends Event {
   private final Entity entity;

   public EntityEvent(Entity entity) {
      this.entity = entity;
   }

   public Entity getEntity() {
      return entity;
   }
}
