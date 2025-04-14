package me.eclipcen.butterflyclient.launch.mixin.client.gui;

import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.util.colors.Colors;
import me.eclipcen.butterflyclient.util.dataloader.DataLoader;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMainMenu.class)
public abstract class MixinGuiMainMenu extends GuiScreen {
   @Shadow
   private String splashText;

   @Redirect(
      method = "drawScreen",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/GuiMainMenu;drawTexturedModalRect(IIIIII)V"
      )
   )
   public void removeMenuLogoRendering(GuiMainMenu guiMainMenu, int x, int y, int textureX, int textureY, int width, int height) {
   }

   @Redirect(
      method = "drawScreen",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/GuiMainMenu;drawModalRectWithCustomSizedTexture(IIFFIIFF)V"
      )
   )
   public void removeSubLogoRendering(int x, int y, float a, float b, int c, int d, float e, float f) {
   }

   @Redirect(
      method = "drawScreen",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/GuiMainMenu;drawString(Lnet/minecraft/client/gui/FontRenderer;Ljava/lang/String;III)V"
      )
   )
   public void removeAllDrawStrings(GuiMainMenu guiMainMenu, FontRenderer fontRenderer1, String string, int i1, int i2, int i3) {
   }

   @Inject(
      method = "initGui",
      at = @At("RETURN")
   )
   public void postConstructor(CallbackInfo callbackInfo) {
      DataLoader loader = Butterfly.getInstance().data;
      if (loader.mainMenu.splashes != null) {
         splashText = loader.mainMenu.getRandomSplash();
      }
   }

   @Redirect(
      method = "initGui()V",
      at = @At(
         value = "FIELD",
         target = "Lnet/minecraft/client/gui/GuiMainMenu;splashText:Ljava/lang/String;",
         opcode = 181
      )
   )
   public void preventSettingSplashInInitGui(GuiMainMenu menu, String val) {
   }

   @Redirect(
      method = "drawScreen(IIF)V",
      at = @At(
         value = "FIELD",
         target = "Lnet/minecraft/client/gui/GuiMainMenu;splashText:Ljava/lang/String;",
         opcode = 181
      )
   )
   public void preventSettingSplashInDrawScreen(GuiMainMenu menu, String val) {
   }

   @Redirect(
      method = "drawScreen(IIF)V",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/texture/TextureManager;bindTexture(Lnet/minecraft/util/ResourceLocation;)V",
         ordinal = 0
      )
   )
   public void removeMenuLogoInit(TextureManager textureManager, ResourceLocation resource) {
      DataLoader loader = Butterfly.getInstance().data;
      if (loader.mainMenu.banner != null) {
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         loader.mainMenu.banner.render(width / 2.0F - 150.0F, 10.0F, 300.0F, 100.0F);
      }

   }

   @Inject(
      method = "drawScreen",
      at = @At("RETURN")
   )
   public void addDrawButterflyStuff(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
      String antiDutch = "dutch ppl suck";
      String butterflyText = "i just flew 10 metres like a butterfly danke an dotgod.cc";
      drawString(
              fontRenderer,
              TextFormatting.LIGHT_PURPLE + "dutch ppl suck",
              width - fontRenderer.getStringWidth("dutch ppl suck") - 2,
              height - 10,
              -1
      );
      drawString(fontRenderer, TextFormatting.LIGHT_PURPLE + "DotGod.CC 1.12.2", 2, height - 20, -1);
      drawString(
              fontRenderer, "i just flew 10 metres like a butterfly danke an dotgod.cc", 2, height - 10, Colors.getGlobalColor().getRGB()
      );
   }
}
