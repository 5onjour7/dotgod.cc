package me.eclipcen.butterflyclient.launch.mixin.util;

import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.module.impl.service.CommandService;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.TabCompleter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TabCompleter.class)
public abstract class MixinTabCompleter {
   @Shadow
   @Final
   protected GuiTextField textField;

   @Inject(
      method = "complete",
      at = @At("HEAD"),
      cancellable = true
   )
   public void preComplete(CallbackInfo callbackInfo) {
      if (this.textField.getText().startsWith(CommandService.prefix.getValue())) {
         String completed = Butterfly.getInstance().getCommandManager().getSuggestionFor(this.textField.getText());
         if (completed.isEmpty() || completed.length() <= this.textField.getText().length()) {
            return;
         }

         this.textField.setText(completed);
         callbackInfo.cancel();
      }

   }
}
