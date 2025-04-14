package me.eclipcen.butterflyclient.clickgui.buttons;

import java.awt.Color;
import me.eclipcen.butterflyclient.clickgui.BaseButton;
import me.eclipcen.butterflyclient.clickgui.utils.ColorUtils;
import me.eclipcen.butterflyclient.clickgui.utils.GuiUtils;
import me.eclipcen.butterflyclient.util.colors.Colors;
import me.eclipcen.butterflyclient.util.font.FontUtils;
import me.eclipcen.butterflyclient.util.math.MathUtil;
import me.eclipcen.butterflyclient.util.settings.Setting;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

public class SubSlider extends BaseButton {
   private final Button parent;
   private final Setting<Number> option;
   private float value;
   private int currentWidth;
   private boolean dragging = false;

   public SubSlider(Button parent, Setting<Number> option) {
      super(parent.getWindow().getX() + 4, parent.getY() + 4, parent.getWindow().getWidth() - 7, 14);
      parent.getSubEntries().add(this);
      this.parent = parent;
      this.window = parent.getWindow();

      if (option != null) {
         value = option.getValue().floatValue();
      }

      this.option = option;
   }

   public void processMouseClick(int mouseX, int mouseY, int button) {
      updateIsMouseHovered(mouseX, mouseY);
      if (isMouseHovered()) {
         if (button == 0) {
            dragging = true;
            MC.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         }

      }
   }

   public void processMouseRelease(int mouseX, int mouseY, int button) {
      updateIsMouseHovered(mouseX, mouseY);
      if (dragging) {
         if (button == 0) {
            dragging = false;
            setWidthFromValue();
         }

      }
   }

   public void draw(int mouseX, int mouseY) {
      if (dragging) {
         currentWidth = mouseX - getX();
         if (currentWidth < 0) {
            currentWidth = 0;
         } else if (currentWidth > 93) {
            currentWidth = 93;
         }

         updateValueFromWidth();
      }

      y = window.getRenderYButton();
      x = window.getX() + 4;
      updateIsMouseHovered(mouseX, mouseY);
      GuiUtils.drawRect(getX(), getY(), -1, getHeight(), Colors.getGlobalColor().getRGB());
      GuiUtils.drawRect(getX(), getY(), currentWidth, getHeight(), ((Color)colours.global.getValue()).getRGB());
      FontUtils.drawStringWithShadow(option.getName() + ": " + MathUtil.getRounded(value), (float)(getX() + 2), (float)(getY() + 3), Colors.WHITE);
      drawDescription(option.getDesc(), mouseX, mouseY);
   }

   public int getColor() {
      return ColorUtils.getColorForGuiEntry(2, isMouseHovered(), false);
   }

   public boolean shouldRender() {
      return parent.isOpen() && parent.shouldRender();
   }

   public void openGui() {
      if (option != null) {
         value = ((Number)option.getValue()).floatValue();
      }

      setWidthFromValue();
   }

   protected void setWidthFromValue() {
      float val = value;

      val -= getMin();
      val /= getMax() - getMin();

      currentWidth = (int)GuiUtils.reCheckSliderRange(val * 93.0F, 0.0F, 93.0F);
   }

   protected void updateValueFromWidth() {
      float val = (float)currentWidth / 92.0F;
      val *= getMax() - getMin();
      val += getMin();
      val = GuiUtils.roundSliderStep(val, getStep());
      value = val = GuiUtils.reCheckSliderRange(val, getMin(), getMax());
      Double roundedValue = GuiUtils.roundSliderForConfig(val);

      if (option.getValue() instanceof Long) {
         option.setValue(roundedValue.longValue());
      } else if (option.getValue() instanceof Integer) {
         option.setValue(roundedValue.intValue());
      } else if (option.getValue() instanceof Float) {
         option.setValue(roundedValue.floatValue());
      } else if (option.getValue() instanceof Double) {
         option.setValue(roundedValue);
      }

   }

   public Button getParent() {
      return parent;
   }

   public float getMax() {
      Number inc = option.getMax();
      return inc == null ? 100.0F : inc.floatValue();
   }

   public float getMin() {
      Number inc = option.getMin();
      return inc == null ? 0.0F : inc.floatValue();
   }

   public float getStep() {
      Number inc = option.getInc();
      return inc == null ? 1.0F : inc.floatValue();
   }
}
