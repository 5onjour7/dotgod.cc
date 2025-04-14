package me.eclipcen.butterflyclient.launch.mixin.client.gui;

import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.event.gui.RenderChatFontEvent;
import me.eclipcen.butterflyclient.event.gui.RenderChatRectEvent;
import me.eclipcen.butterflyclient.util.font.FontUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiNewChat.class)
public abstract class MixinGuiNewChat {
   @Redirect(
      method = "drawChat",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I"
      )
   )
   public int chatFontRender(FontRenderer fontrenderer, String text, float x, float y, int color) {
      RenderChatFontEvent event = new RenderChatFontEvent();
      Butterfly.getEventBus().transmit(event);

      return event.isCanceled() ? (int) FontUtils.drawStringWithShadow(text, x, y, color) : fontrenderer.drawStringWithShadow(text, x, y, color);
   }

   @Redirect(
      method = "drawChat",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/GuiNewChat;drawRect(IIIII)V"
      )
   )
   private void chatDrawRect(int left, int top, int right, int bottom, int color) {
      RenderChatRectEvent event = new RenderChatRectEvent();
      Butterfly.getEventBus().transmit(event);

      if (!event.isCanceled()) {
         Gui.drawRect(left, top, right, bottom, color);
      }

   }
}
