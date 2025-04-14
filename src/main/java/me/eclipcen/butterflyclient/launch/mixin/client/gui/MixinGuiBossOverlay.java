package me.eclipcen.butterflyclient.launch.mixin.client.gui;

import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.event.render.RenderBossOverlayEvent;
import net.minecraft.client.gui.GuiBossOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiBossOverlay.class)
public abstract class MixinGuiBossOverlay {
   @Inject(
      method = "renderBossHealth",
      at = @At("HEAD"),
      cancellable = true
   )
   public void onRenderBossHealth(CallbackInfo callbackInfo) {
      RenderBossOverlayEvent event = new RenderBossOverlayEvent();
      Butterfly.getEventBus().transmit(event);

      if (event.isCanceled()) {
         callbackInfo.cancel();
      }

   }
}
