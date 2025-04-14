package me.eclipcen.butterflyclient.launch.mixin.client.gui;

import java.util.List;
import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.event.gui.TabOverlayEvent;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiPlayerTabOverlay.class)
public abstract class MixinGuiPlayerTabOverlay {
   @Redirect(
      method = "renderPlayerlist",
      at = @At(
         value = "INVOKE",
         target = "Ljava/util/List;subList(II)Ljava/util/List;"
      )
   )
   public List<?> subList(List<?> list, int fromIndex, int toIndex) {
      TabOverlayEvent.Size event = new TabOverlayEvent.Size(toIndex);
      Butterfly.getEventBus().transmit(event);

      return list.subList(fromIndex, Math.min(event.getSize(), list.size()));
   }

   @Redirect(
      method = "renderPlayerlist",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I",
         ordinal = 2
      )
   )
   public int drawString(FontRenderer fontRenderer, String text, float x, float y, int color) {
      TabOverlayEvent.Font event = new TabOverlayEvent.Font(text, x, y, color);
      Butterfly.getEventBus().transmit(event);

      return fontRenderer.drawStringWithShadow(event.text, event.x, event.y, event.color);
   }

   @Inject(
      method = "drawPing",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/GuiPlayerTabOverlay;drawTexturedModalRect(IIIIII)V"
      )
   )
   public void drawIconIfPossible(int p_175245_1_, int p_175245_2_, int p_175245_3_, NetworkPlayerInfo info, CallbackInfo callbackInfo) {
      TabOverlayEvent.Icon event = new TabOverlayEvent.Icon(info, (float)p_175245_1_, (float)p_175245_3_, (float)p_175245_2_);
      Butterfly.getEventBus().transmit(event);
   }
}
