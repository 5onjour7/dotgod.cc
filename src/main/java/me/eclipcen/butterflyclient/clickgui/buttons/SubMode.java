package me.eclipcen.butterflyclient.clickgui.buttons;

import me.eclipcen.butterflyclient.clickgui.BaseButton;
import me.eclipcen.butterflyclient.clickgui.Window;
import me.eclipcen.butterflyclient.clickgui.utils.ColorUtils;
import me.eclipcen.butterflyclient.clickgui.utils.GuiUtils;
import me.eclipcen.butterflyclient.util.colors.Colors;
import me.eclipcen.butterflyclient.util.font.FontUtils;
import me.eclipcen.butterflyclient.util.settings.Setting;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

public class SubMode extends BaseButton {
   private final Button parent;
   private final Window window;
   private final Setting<Enum> option;

   public SubMode(Button parent, Setting<Enum> option) {
      super(parent.getWindow().getX() + 4, parent.getY() + 4, parent.getWindow().getWidth() - 7, 14);
      parent.getSubEntries().add(this);
      this.parent = parent;
      this.window = parent.getWindow();
      this.option = option;
   }

   public void processMouseClick(int mouseX, int mouseY, int button) {
      updateIsMouseHovered(mouseX, mouseY);
      if (isMouseHovered() && parent.isOpen()) {
         int arrayNumber;
         if (button == 0) {
            arrayNumber = option.getValue().ordinal() + 1;
            if (arrayNumber != option.getValue().getClass().getEnumConstants().length) {
               option.setValue(option.getValue().getClass().getEnumConstants()[arrayNumber]);
            } else {
               option.setValue(option.getValue().getClass().getEnumConstants()[0]);
            }

            MC.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         } else if (button == 1) {
            arrayNumber = option.getValue().ordinal() - 1;
            if (arrayNumber != -1) {
               option.setValue(option.getValue().getClass().getEnumConstants()[arrayNumber]);
            } else {
               option.setValue(option.getValue().getClass().getEnumConstants()[option.getValue().getClass().getEnumConstants().length - 1]);
            }

            MC.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         }
      }

   }

   public void draw(int mouseX, int mouseY) {
      y = window.getRenderYButton();
      x = window.getX() + 4;

      updateIsMouseHovered(mouseX, mouseY);
      GuiUtils.drawRect(getX(), getY(), getWidth(), getHeight(), colours.global.getValue().getRGB());
      GuiUtils.drawRect(getX(), getY(), -1, getHeight(), Colors.getGlobalColor().getRGB());
      FontUtils.drawStringWithShadow(option.getName() + ": " + option.getValue().name(), (float) (getX() + 2), (float) (getY() + 3), Colors.WHITE);
      drawDescription(option.getDesc(), mouseX, mouseY);
   }

   public int getColor() {
      return ColorUtils.getColorForGuiEntry(2, isMouseHovered(), false);
   }

   public boolean shouldRender() {
      return parent.isOpen() && parent.shouldRender();
   }

   public Button getParent() {
      return parent;
   }
}
