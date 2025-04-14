package me.eclipcen.butterflyclient.clickgui.buttons;

import me.eclipcen.butterflyclient.clickgui.BaseButton;
import me.eclipcen.butterflyclient.clickgui.Window;
import me.eclipcen.butterflyclient.clickgui.utils.GuiUtils;
import me.eclipcen.butterflyclient.util.colors.Colors;
import me.eclipcen.butterflyclient.util.font.FontUtils;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import org.lwjgl.input.Keyboard;

public class SubBind extends BaseButton {
   private final Button parent;
   private final Window window;
   private boolean accepting = false;

   public SubBind(Button parent) {
      super(parent.getWindow().getX() + 4, parent.getY() + 4, parent.getWindow().getWidth() - 7, 14);
      parent.getSubEntries().add(this);
      this.parent = parent;
      this.window = parent.getWindow();
   }

   public void processMouseClick(int mouseX, int mouseY, int button) {
      updateIsMouseHovered(mouseX, mouseY);
      if (isMouseHovered()) {
         if (button == 0) {
            accepting = true;
            MC.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         }

      }
   }

   public void processKeyPress(char character, int key) {
      if (accepting) {
         if (key != 211 && key != 14 && key != 1) {
            parent.getModule().setBind(Keyboard.getKeyName(key));
         } else {
            parent.getModule().setBind("NONE");
         }
          accepting = false;
      }
   }

   public void draw(int mouseX, int mouseY) {
      String keyName = !accepting ? "Bind: " + Keyboard.getKeyName(parent.getModule().getBind()) : "Press a key...";

      y = window.getRenderYButton();
      x = window.getX() + 4;

      updateIsMouseHovered(mouseX, mouseY);
      GuiUtils.drawRect(getX(), getY(), getWidth(), getHeight(), getColor());
      GuiUtils.drawRect(getX(), getY(), -1, getHeight(), Colors.getGlobalColor().getRGB());
      FontUtils.drawStringWithShadow(keyName, (float)(getX() + 2), (float)(getY() + 3), Colors.WHITE);
      drawDescription("Keybind of mod", mouseX, mouseY);
   }

   public int getColor() {
      return !isMouseHovered() ? Colors.toRGBA(0, 0, 0, 50) : Colors.toRGBA(150, 150, 150, 50);
   }

   public boolean shouldRender() {
      return parent.isOpen() && parent.shouldRender();
   }

   public Button getParent() {
      return parent;
   }
}
