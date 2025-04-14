package me.eclipcen.butterflyclient.launch.mixin.world;

import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.event.entity.EntityAddedEvent;
import me.eclipcen.butterflyclient.event.entity.EntityRemovedEvent;
import me.eclipcen.butterflyclient.event.world.LightUpdateEvent;
import me.eclipcen.butterflyclient.wrapper.IWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class MixinWorld implements IWorld {
   @Invoker
   public abstract void invokeOnEntityRemoved(Entity var1);

   @Inject(
      method = "onEntityAdded",
      at = @At("HEAD")
   )
   public void onEntityAdded(Entity entityIn, CallbackInfo callback) {
      Butterfly.getEventBus().transmit(new EntityAddedEvent(entityIn));
   }

   @Inject(
      method = "Lnet/minecraft/world/World;onEntityRemoved(Lnet/minecraft/entity/Entity;)V",
      at = @At("HEAD")
   )
   public void onEntityRemoved(Entity entity, CallbackInfo callbackInfo) {
      Butterfly.getEventBus().transmit(new EntityRemovedEvent(entity));
   }

   @Inject(
      method = "checkLightFor",
      at = @At("HEAD"),
      cancellable = true
   )
   private void onCheckLightFor(EnumSkyBlock lightType, BlockPos pos, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
      LightUpdateEvent event = new LightUpdateEvent();
      Butterfly.getEventBus().transmit(event);

      if (!Minecraft.getMinecraft().isSingleplayer() && event.isCanceled()) {
         callbackInfoReturnable.setReturnValue(false);
         callbackInfoReturnable.cancel();
      }

   }
}
