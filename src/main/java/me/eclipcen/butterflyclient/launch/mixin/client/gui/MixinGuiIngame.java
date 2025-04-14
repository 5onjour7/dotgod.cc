package me.eclipcen.butterflyclient.launch.mixin.client.gui;

import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.event.render.CrosshairBlendEvent;
import me.eclipcen.butterflyclient.event.render.RenderVignetteEvent;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public abstract class MixinGuiIngame {
   @Inject(
      method = "renderPumpkinOverlay",
      at = @At("HEAD"),
      cancellable = true
   )
   protected void preRenderPumpkinOverlay(ScaledResolution scaledRes, CallbackInfo callbackInfo) {
      callbackInfo.cancel();
   }

   @Inject(
      method = "renderPotionEffects",
      at = @At("HEAD"),
      cancellable = true
   )
   public void prerenderPotionEffects(ScaledResolution resolution, CallbackInfo callbackInfo) {
      callbackInfo.cancel();
   }

   @Redirect(
      method = "renderAttackIndicator",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/GlStateManager;tryBlendFuncSeparate(Lnet/minecraft/client/renderer/GlStateManager$SourceFactor;Lnet/minecraft/client/renderer/GlStateManager$DestFactor;Lnet/minecraft/client/renderer/GlStateManager$SourceFactor;Lnet/minecraft/client/renderer/GlStateManager$DestFactor;)V"
      )
   )
   public void onRenderGameOverlayHead(SourceFactor srcFactor, DestFactor dstFactor, SourceFactor srcFactorAlpha, DestFactor dstFactorAlpha) {
      CrosshairBlendEvent crosshairBlendEvent = new CrosshairBlendEvent();
      Butterfly.getEventBus().transmit(crosshairBlendEvent);

      if (!crosshairBlendEvent.isCanceled()) {
         GlStateManager.tryBlendFuncSeparate(SourceFactor.ONE_MINUS_DST_COLOR, DestFactor.ONE_MINUS_SRC_COLOR, SourceFactor.ONE, DestFactor.ZERO);
      }

   }

   @Inject(
      method = "renderVignette",
      at = @At("HEAD"),
      cancellable = true
   )
   public void onRenderVignette(CallbackInfo callbackInfo) {
      RenderVignetteEvent event = new RenderVignetteEvent();
      Butterfly.getEventBus().transmit(event);

      if (event.isCanceled()) {
         callbackInfo.cancel();
      }

   }
}
