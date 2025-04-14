package me.eclipcen.butterflyclient.launch.mixin.client.gui;

import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.event.gui.GuiDrawEvent;
import me.eclipcen.butterflyclient.event.gui.GuiKeyboardEvent;
import me.eclipcen.butterflyclient.event.gui.RenderToolTipEvent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreen.class)
public abstract class MixinGuiScreen {
   @Inject(
      method = "renderToolTip",
      at = @At("HEAD"),
      cancellable = true
   )
   public void onRenderToolTip(ItemStack stack, int x, int y, CallbackInfo callbackInfo) {
      RenderToolTipEvent event = new RenderToolTipEvent(stack);
      Butterfly.getEventBus().transmit(event);

      if (event.isCanceled()) {
         callbackInfo.cancel();
      }

   }

   @Inject(
      method = "drawScreen",
      at = @At("HEAD")
   )
   public void onDrawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo callbackInfo) {
      GuiDrawEvent event = new GuiDrawEvent(mouseX, mouseY, partialTicks);
      Butterfly.getEventBus().transmit(event);
   }

   @Inject(
      method = "handleKeyboardInput",
      at = @At("HEAD")
   )
   public void onHandleKeyboardInput(CallbackInfo callbackInfo) {
      Butterfly.getEventBus().transmit(new GuiKeyboardEvent());
   }
}
