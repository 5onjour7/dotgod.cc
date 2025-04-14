package me.eclipcen.butterflyclient.launch.mixin.client.settings;

import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyBinding.class)
public abstract class MixinKeyBinding {
   @Shadow
   private boolean pressed;

   @Inject(
      method = "isKeyDown",
      at = @At("RETURN"),
      cancellable = true
   )
   public void isKeyDown(CallbackInfoReturnable<Boolean> callbackInfo) {
      if (!(Boolean)callbackInfo.getReturnValue()) {
         callbackInfo.setReturnValue(pressed);
      }

   }
}
