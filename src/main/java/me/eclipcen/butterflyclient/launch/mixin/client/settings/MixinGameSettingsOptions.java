package me.eclipcen.butterflyclient.launch.mixin.client.settings;

import net.minecraft.client.settings.GameSettings.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Options.class)
public abstract class MixinGameSettingsOptions {
   @ModifyConstant(
      method = "<clinit>",
      constant = @Constant(
         floatValue = 110.0F
      )
   )
   private static float modifySlider(float fov) {
      return 160.0F;
   }
}
