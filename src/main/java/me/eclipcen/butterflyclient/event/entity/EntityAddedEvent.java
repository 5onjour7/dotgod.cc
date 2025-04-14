package me.eclipcen.butterflyclient.event.entity;

import net.minecraft.entity.Entity;

public class EntityAddedEvent extends EntityEvent {
   public EntityAddedEvent(Entity entity) {
      super(entity);
   }
}
