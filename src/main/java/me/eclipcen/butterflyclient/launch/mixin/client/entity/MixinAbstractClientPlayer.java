package me.eclipcen.butterflyclient.launch.mixin.client.entity;

import com.mojang.authlib.GameProfile;
import javax.annotation.Nullable;
import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.event.render.FOVEvent;
import me.eclipcen.butterflyclient.util.dataloader.Group;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer extends EntityPlayer {
   @Shadow
   @Nullable
   protected abstract NetworkPlayerInfo getPlayerInfo();

   public MixinAbstractClientPlayer() {
      super(null, null);
   }

   @Inject(
      method = "getLocationCape",
      at = @At("HEAD"),
      cancellable = true
   )
   public void preGetLocationCape(CallbackInfoReturnable<ResourceLocation> callbackInfo) {
      if (getPlayerInfo() != null) {
         Group group = Butterfly.getInstance().data.getGroup(getPlayerInfo());
         if (group != null) {
            group.doWithCapeIfPresent((tex) -> {
               callbackInfo.setReturnValue(tex.texture);
            });
         }
      }

   }

   @Inject(
      method = "getFovModifier",
      at = @At("RETURN"),
      locals = LocalCapture.CAPTURE_FAILHARD,
      cancellable = true
   )
   public void getFovModifier(CallbackInfoReturnable<Float> cir, float f) {
      FOVEvent.Update event = new FOVEvent.Update(f);
      Butterfly.getEventBus().transmit(event);
      cir.setReturnValue(event.getFov());
   }
}
