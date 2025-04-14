package me.eclipcen.butterflyclient.event.render;

import net.futureclient.eventbus.Event;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;

public class RenderEntityLayerEvent extends Event {
   private final ModelBase modelBase;
   private final EntityLivingBase entity;
   private final float limbSwing;
   private final float limbSwingAmount;
   private final float age;
   private final float headYaw;
   private final float headPitch;
   private final float scale;

   public RenderEntityLayerEvent(ModelBase modelBase, EntityLivingBase entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch, float scale) {
      this.entity = entity;
      this.modelBase = modelBase;
      this.limbSwing = limbSwing;
      this.limbSwingAmount = limbSwingAmount;
      this.age = age;
      this.headPitch = headPitch;
      this.scale = scale;
      this.headYaw = headYaw;
   }

   public ModelBase getModelBase() {
      return modelBase;
   }

   public EntityLivingBase getEntity() {
      return entity;
   }

   public float getLimbSwing() {
      return limbSwing;
   }

   public float getLimbSwingAmount() {
      return limbSwingAmount;
   }

   public float getAge() {
      return age;
   }

   public float getHeadYaw() {
      return headYaw;
   }

   public float getHeadPitch() {
      return headPitch;
   }

   public float getScale() {
      return scale;
   }
}
