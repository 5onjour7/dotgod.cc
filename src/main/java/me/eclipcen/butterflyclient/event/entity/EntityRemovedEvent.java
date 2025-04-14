package me.eclipcen.butterflyclient.event.entity;

import net.minecraft.entity.Entity;

public class EntityRemovedEvent extends EntityEvent {
   public EntityRemovedEvent(Entity entity) {
      super(entity);
   }
}
