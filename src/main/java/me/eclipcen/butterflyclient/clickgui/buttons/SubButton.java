package me.eclipcen.butterflyclient.clickgui.buttons;

import me.eclipcen.butterflyclient.clickgui.BaseButton;
import me.eclipcen.butterflyclient.clickgui.utils.ColorUtils;
import me.eclipcen.butterflyclient.clickgui.utils.GuiUtils;
import me.eclipcen.butterflyclient.util.colors.Colors;
import me.eclipcen.butterflyclient.util.font.FontUtils;
import me.eclipcen.butterflyclient.util.settings.Setting;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

public class SubButton extends BaseButton {
   private final Button parent;
   private final Setting<Boolean> option;

   public SubButton(Button parent, Setting<Boolean> option) {
      super(parent.getWindow().getX() + 4, parent.getY() + 4, parent.getWindow().getWidth() - 7, 14);
      parent.getSubEntries().add(this);
      this.parent = parent;
      this.window = parent.getWindow();
      this.option = option;
   }

   public void processMouseClick(int mouseX, int mouseY, int button) {
      updateIsMouseHovered(mouseX, mouseY);
      if (isMouseHovered()) {
         if (button == 0) {
            option.setValue(!option.getValue());
            MC.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         }

      }
   }

   public void draw(int mouseX, int mouseY) {
      y = window.getRenderYButton();
      x = window.getX() + 4;

      updateIsMouseHovered(mouseX, mouseY);
      GuiUtils.drawRect(getX(), getY(), getWidth(), getHeight(), getColor());
      GuiUtils.drawRect(getX(), getY(), -1, getHeight(), Colors.getGlobalColor().getRGB());
      FontUtils.drawStringWithShadow(option.getName(), (float) (getX() + 2), (float) (getY() + 3), Colors.WHITE);
      drawDescription(option.getDesc(), mouseX, mouseY);
   }

   public int getColor() {
      return ColorUtils.getColorForGuiEntry(3, isMouseHovered(), option.getValue());
   }

   public boolean shouldRender() {
      return parent.isOpen() && parent.shouldRender();
   }

   public Button getParent() {
      return parent;
   }
}
