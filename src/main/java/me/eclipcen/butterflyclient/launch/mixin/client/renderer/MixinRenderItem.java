package me.eclipcen.butterflyclient.launch.mixin.client.renderer;

import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.event.render.RenderItemGlintEffectEvent;
import net.minecraft.client.renderer.RenderItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(RenderItem.class)
public abstract class MixinRenderItem {
   @ModifyArg(
      method = "renderEffect",
      at = @At(
         value = "INVOKE",
         target = "net/minecraft/client/renderer/RenderItem.renderModel(Lnet/minecraft/client/renderer/block/model/IBakedModel;I)V"
      )
   )
   private int renderEffect(int glintVal) {
      RenderItemGlintEffectEvent event = new RenderItemGlintEffectEvent(glintVal);
      Butterfly.getEventBus().transmit(event);
      return event.getColor();
   }
}
