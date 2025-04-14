package me.eclipcen.butterflyclient.launch.mixin.client.gui;

import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.module.impl.service.CommandService;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiChat.class)
public abstract class MixinGuiChat extends GuiScreen {
   @Unique
   public String prevText = "";
   @Unique
   public String prevSuggestion = "";
   @Shadow
   protected GuiTextField inputField;

   @Inject(
      method = "drawScreen",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/GuiTextField;drawTextBox()V"
      )
   )
   public void drawSemiTransparentText(CallbackInfo ci) {
      if (inputField.getText().startsWith(CommandService.prefix.getValue())) {
         GL11.glPushMatrix();
         GL11.glEnable(3042);
         int x = inputField.x;
         int y = inputField.y;

         if (!prevText.equals(inputField.getText())) {
            prevText = inputField.getText();
            prevSuggestion = Butterfly.getInstance().getCommandManager().getSuggestionFor(prevText);
         }

         fontRenderer.drawString(prevSuggestion, (float) x, (float) y, 1610612735, false);
         GL11.glDisable(3042);
         GL11.glPopMatrix();
      }

   }
}
