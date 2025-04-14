package me.eclipcen.butterflyclient.launch.mixin.client.renderer;

import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.event.render.RenderEntityFireEvent;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Render.class)
public class MixinRender {
   @Inject(
      method = "renderEntityOnFire",
      at = @At("HEAD"),
      cancellable = true
   )
   public void onRenderEntityOnFire(Entity entity, double x, double y, double z, float partialTicks, CallbackInfo ci) {
      RenderEntityFireEvent event = new RenderEntityFireEvent();
      Butterfly.getEventBus().transmit(event);
      if (event.isCanceled()) {
         ci.cancel();
      }

   }
}
