package me.eclipcen.butterflyclient.clickgui.buttons;

import java.util.ArrayList;
import java.util.List;
import me.eclipcen.butterflyclient.clickgui.BaseButton;
import me.eclipcen.butterflyclient.clickgui.Window;
import me.eclipcen.butterflyclient.clickgui.utils.ColorUtils;
import me.eclipcen.butterflyclient.clickgui.utils.GuiUtils;
import me.eclipcen.butterflyclient.module.api.Module;
import me.eclipcen.butterflyclient.module.api.branches.ToggleMod;
import me.eclipcen.butterflyclient.module.impl.service.GuiMod;
import me.eclipcen.butterflyclient.util.colors.Colors;
import me.eclipcen.butterflyclient.util.font.FontUtils;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

public class Button extends BaseButton {
   private final List<BaseButton> subEntries = new ArrayList<>();
   private boolean isOpen = false;
   private final Module module;

   public Button(Window window, Module module) {
      super(window.getX() + 2, window.getY() + 2, window.getWidth() - 4, 14);
      this.window = window;
      this.module = module;
   }

   public void processMouseClick(int mouseX, int mouseY, int button) {
      updateIsMouseHovered(mouseX, mouseY);
      if (isMouseHovered()) {
         if (button == 0) {
            ((ToggleMod) module).toggle();
            MC.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         } else if (button == 1) {
            isOpen = !isOpen;
            MC.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         }

      }
   }

   public void draw(int mouseX, int mouseY) {
      y = window.getRenderYButton();
      x = window.getX() + 2;
      updateIsMouseHovered(mouseX, mouseY);
      if (GuiMod.boxes.getValue()) {
         GuiUtils.drawRect(getX(), getY(), getWidth(), getHeight(), ColorUtils.getColorForGuiEntry(4, isMouseHovered(), module.isEnabled()));
      }

      FontUtils.drawStringWithShadow(module.getModName(), (float) (getX() + 2), (float) (getY() + 3), getColor());
      if (isMouseHovered()) {
         GuiUtils.drawRect(getX(), getY(), getWidth(), getHeight(), Colors.toRGBA(100, 100, 100, 50));
      }

      drawDescription(module.getModDescription(), mouseX, mouseY);
   }

   public int getColor() {
      return ColorUtils.getColorForGuiEntry(0, isMouseHovered(), module.isEnabled());
   }

   public boolean isOpen() {
      return isOpen;
   }

   public Module getModule() {
      return module;
   }

   public List<BaseButton> getSubEntries() {
      return subEntries;
   }
}
