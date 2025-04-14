package me.eclipcen.butterflyclient.launch.mixin.client.renderer;

import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.event.render.RenderItemEvent;
import me.eclipcen.butterflyclient.event.render.RenderItemTranslateEvent;
import me.eclipcen.butterflyclient.event.render.RenderOverlayEvent;
import me.eclipcen.butterflyclient.event.render.RotateArmEvent;
import me.eclipcen.butterflyclient.wrapper.IItemRenderer;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer implements IItemRenderer {
   @Accessor
   public abstract void setEquippedProgressMainHand(float var1);

   @Accessor
   public abstract void setEquippedProgressOffHand(float var1);

   @Accessor
   public abstract void setItemStackMainHand(ItemStack var1);

   @Accessor
   public abstract void setItemStackOffHand(ItemStack var1);

   @Inject(
      method = "renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V",
      at = @At("HEAD"),
      cancellable = true
   )
   private void renderItemInFirstPerson(AbstractClientPlayer abstractClientPlayer, float partialTicks, float pitch, EnumHand hand, float swingProgress, ItemStack stack, float equipProgress, CallbackInfo callbackInfo) {
      RenderItemEvent event = new RenderItemEvent((ItemRenderer) (Object) this, abstractClientPlayer, partialTicks, pitch, hand, swingProgress, stack, equipProgress);
      Butterfly.getEventBus().transmit(event);
   }

   @Inject(
      method = "transformFirstPerson",
      at = @At("HEAD")
   )
   public void transformFirstPerson(EnumHandSide hand, float p_187459_2_, CallbackInfo callbackInfo) {
      RenderItemTranslateEvent event = new RenderItemTranslateEvent(hand);
      Butterfly.getEventBus().transmit(event);
   }

   @Inject(
      method = "rotateArm",
      at = @At("HEAD"),
      cancellable = true
   )
   public void onRotateArm(float partialTicks, CallbackInfo callbackInfo) {
      RotateArmEvent event = new RotateArmEvent();
      Butterfly.getEventBus().transmit(event);
      if (event.isCanceled()) {
         callbackInfo.cancel();
      }

   }

   @Inject(
      method = "renderSuffocationOverlay",
      at = @At("HEAD"),
      cancellable = true
   )
   public void onRenderSuffocationOverlay(CallbackInfo callbackInfo) {
      RenderOverlayEvent event = new RenderOverlayEvent(RenderOverlayEvent.OverlayType.BLOCK);
      Butterfly.getEventBus().transmit(event);
      if (event.isCanceled()) {
         callbackInfo.cancel();
      }

   }

   @Inject(
      method = "renderWaterOverlayTexture",
      at = @At("HEAD"),
      cancellable = true
   )
   public void onRenderWaterOverlayTexture(CallbackInfo callbackInfo) {
      RenderOverlayEvent event = new RenderOverlayEvent(RenderOverlayEvent.OverlayType.LIQUID);
      Butterfly.getEventBus().transmit(event);
      if (event.isCanceled()) {
         callbackInfo.cancel();
      }

   }

   @Inject(
      method = "renderFireInFirstPerson",
      at = @At("HEAD"),
      cancellable = true
   )
   public void onRenderWFireInFirstPerson(CallbackInfo callbackInfo) {
      RenderOverlayEvent event = new RenderOverlayEvent(RenderOverlayEvent.OverlayType.FIRE);
      Butterfly.getEventBus().transmit(event);
      if (event.isCanceled()) {
         callbackInfo.cancel();
      }

   }
}
