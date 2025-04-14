package me.eclipcen.butterflyclient.launch.mixin.client.optimization;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.CullFace;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderLivingBase.class)
public class MixinRenderLivingBase<T extends EntityLivingBase> {
   @Redirect(
      method = "applyRotations",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/util/text/TextFormatting;getTextWithoutFormattingCodes(Ljava/lang/String;)Ljava/lang/String;"
      )
   )
   public String text(String text) {
      return null;
   }

   @Redirect(
      method = "doRender(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/GlStateManager;disableCull()V"
      )
   )
   public void disableCull() {
      GlStateManager.enableCull();
      GlStateManager.cullFace(CullFace.BACK);
   }
}
