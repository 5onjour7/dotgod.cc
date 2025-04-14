package me.eclipcen.butterflyclient.launch.mixin.client.gui;

import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.module.impl.render.NameProtect;
import me.eclipcen.butterflyclient.util.Globals;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FontRenderer.class)
public abstract class MixinFontRenderer {
   @Shadow
   protected abstract ResourceLocation getUnicodePageLocation(int var1);

   @Inject(
      method = "<init>",
      at = @At("RETURN")
   )
   public void init(GameSettings gameSettingsIn, ResourceLocation location, TextureManager textureManagerIn, boolean unicode, CallbackInfo ci) {
      for (int i = 0; i < 256; ++i) {
         getUnicodePageLocation(i);
      }

   }

   @ModifyVariable(
           method = "renderString",
           at = @At("HEAD"),
           ordinal = 0,
           argsOnly = true
   )
   private String renderString(String string) {
      if (Globals.MC.player != null) {
         NameProtect nameProtect = Butterfly.getInstance().getModuleManager().getModule(NameProtect.class);

         if (nameProtect != null && nameProtect.isEnabled()) {
            return StringUtils.replace(string, Globals.MC.player.getName(), nameProtect.name.getValue());
         }
      }

      return string;
   }

   @ModifyVariable(
           method = "getStringWidth",
           at = @At("HEAD"),
           ordinal = 0,
           argsOnly = true
   )
   private String getStringWidth(String string) {
      if (Globals.MC.player != null) {
         NameProtect nameProtect = Butterfly.getInstance().getModuleManager().getModule(NameProtect.class);

         if (nameProtect != null && nameProtect.isEnabled()) {
            return StringUtils.replace(string, Globals.MC.player.getName(), nameProtect.name.getValue());
         }
      }

      return string;
   }
}
