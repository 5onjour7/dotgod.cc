package me.eclipcen.butterflyclient.launch.mixin.client.renderer;

import com.google.common.base.Predicate;
import java.util.Collections;
import java.util.List;
import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.event.player.PlayerMouseOverEvent;
import me.eclipcen.butterflyclient.event.render.HurtcamEffectEvent;
import me.eclipcen.butterflyclient.event.render.RenderEvent;
import me.eclipcen.butterflyclient.event.render.RenderFogEvent;
import me.eclipcen.butterflyclient.event.render.RenderOutlineBoxEvent;
import me.eclipcen.butterflyclient.util.render.RenderUtil;
import me.eclipcen.butterflyclient.wrapper.IEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer implements IEntityRenderer {
   @Shadow
   private float fogColorRed;
   @Shadow
   private float fogColorGreen;
   @Shadow
   private float fogColorBlue;

   @Invoker("setupCameraTransform")
   public abstract void setupCamera(float var1, int var2);

   @Inject(
      method = "renderWorldPass",
      at = @At(
         value = "INVOKE",
         target = "net/minecraft/client/renderer/GlStateManager.clear(I)V",
         ordinal = 1,
         shift = At.Shift.AFTER
      )
   )
   private void onRenderHand(int pass, float partialTicks, long finishTimeNano, CallbackInfo ci) {
      if (Display.isActive() || Display.isVisible()) {
         Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
         if (entity != null) {
            RenderUtil.FRUSTUM.setPosition(entity.posX, entity.posY, entity.posZ);
            Butterfly.getEventBus().transmit(new RenderEvent(partialTicks));
         }
      }

   }

   @Inject(
      method = "updateCameraAndRender",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/GuiIngame;renderGameOverlay(F)V"
      )
   )
   public void onUpdateCameraAndRender(float partialTicks, long nanoTime, CallbackInfo callbackInfo) {
      Butterfly.getEventBus().transmit(new RenderEvent.Render2DEvent(partialTicks));
   }

   @Redirect(
      method = "getMouseOver",
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/multiplayer/WorldClient;getEntitiesInAABBexcluding(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/AxisAlignedBB;Lcom/google/common/base/Predicate;)Ljava/util/List;"
)
   )
   public List<Entity> getEntitiesInAABBexcluding(WorldClient worldClient, Entity entityIn, AxisAlignedBB boundingBox, Predicate predicate) {
      PlayerMouseOverEvent event = new PlayerMouseOverEvent();
      Butterfly.getEventBus().transmit(event);
      return event.isCanceled() ? Collections.emptyList() : worldClient.getEntitiesInAABBexcluding(entityIn, boundingBox, predicate);
   }

   @Inject(
      method = "hurtCameraEffect",
      at = @At("HEAD"),
      cancellable = true
   )
   public void hurtCamera(float partialTicks, CallbackInfo callbackInfo) {
      HurtcamEffectEvent event = new HurtcamEffectEvent();
      Butterfly.getEventBus().transmit(event);
      if (event.isCanceled()) {
         callbackInfo.cancel();
      }

   }

   @Redirect(
      method = "renderWorldPass",
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/renderer/RenderGlobal;drawSelectionBox(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/math/RayTraceResult;IF)V"
)
   )
   public void onDrawBox(RenderGlobal renderGlobal, EntityPlayer player, RayTraceResult movingObjectPositionIn, int execute, float partialTicks) {
      RenderOutlineBoxEvent event = new RenderOutlineBoxEvent();
      Butterfly.getEventBus().transmit(event);
      if (!event.isCanceled()) {
         renderGlobal.drawSelectionBox(player, movingObjectPositionIn, execute, partialTicks);
      }

   }

   @Inject(
      method = "setupFog",
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/renderer/GlStateManager;color(FFFF)V",
   shift = At.Shift.AFTER
)}
   )
   public void onSetupFog(int startCoords, float partialTicks, CallbackInfo callbackInfo) {
      RenderFogEvent.Density event = new RenderFogEvent.Density(0.1F);
      Butterfly.getEventBus().transmit(event);
      GlStateManager.setFogDensity(event.getDensity());
   }

   @Inject(
      method = "updateFogColor",
      at = @At("RETURN")
   )
   public void onUpdateFogColor(float partialTicks, CallbackInfo ci) {
      RenderFogEvent.Colour event = new RenderFogEvent.Colour(this.fogColorRed, this.fogColorGreen, this.fogColorBlue);
      Butterfly.getEventBus().transmit(event);
      GlStateManager.clearColor(event.getRed(), event.getGreen(), event.getBlue(), 0.0F);
   }
}
