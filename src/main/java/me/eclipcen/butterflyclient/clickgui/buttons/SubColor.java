package me.eclipcen.butterflyclient.clickgui.buttons;

import java.awt.Color;
import me.eclipcen.butterflyclient.clickgui.BaseButton;
import me.eclipcen.butterflyclient.clickgui.utils.ColorUtils;
import me.eclipcen.butterflyclient.clickgui.utils.GuiUtils;
import me.eclipcen.butterflyclient.module.impl.other.HudMod;
import me.eclipcen.butterflyclient.util.colors.Colors;
import me.eclipcen.butterflyclient.util.font.FontUtils;
import me.eclipcen.butterflyclient.util.render.RenderUtil;
import me.eclipcen.butterflyclient.util.settings.setting.ColorSetting;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import org.lwjgl.input.Mouse;

public class SubColor extends BaseButton {
   private final Button parent;
   private final ColorSetting option;
   private boolean isOpen = false;
   private static final int COLOR_PICKER_WIDTH = 78;
   private static final int DIVISOR = 10;
   private static final int ALPHA_HEIGHT = 95;
   private static final int INDICATOR = (new Color(255, 255, 255, 100)).getRGB();

   public SubColor(Button parent, ColorSetting option) {
      super(parent.getWindow().getX() + 4, parent.getY() + 4, parent.getWindow().getWidth() - 7, 14);
      parent.getSubEntries().add(this);
      this.parent = parent;
      this.window = parent.getWindow();
      this.option = option;
   }

   public void processMouseClick(int mouseX, int mouseY, int button) {
      updateIsMouseHovered(mouseX, mouseY);
      if (button == 0 && isOpen) {
         boolean global = option.getShouldGlobal();
         boolean rainbow = option.getShouldRainbow();
         if (global && rainbow) {
            if (isAreaClickable(mouseX, mouseY, getX(), getY() + 114, getWidth() - 2, 11.0D)) {
               option.setRainbowValue(!option.getRainbowValue());
               MC.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            } else if (isAreaClickable(mouseX, mouseY, getX(), getY() + 126, getWidth() - 2, 11.0D)) {
               option.setGlobalValue(!option.getGlobalValue());
               MC.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }
         } else if (global && isAreaClickable(mouseX, mouseY, getX(), getY() + 114, getWidth() - 2, 11.0D)) {
            option.setGlobalValue(!option.getGlobalValue());
            MC.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         } else if (rainbow && isAreaClickable(mouseX, mouseY, getX(), getY() + 114, getWidth() - 2, 11.0D)) {
            option.setRainbowValue(!option.getRainbowValue());
            MC.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         }
      }

      if (isMouseHovered()) {
         if (button == 1) {
            isOpen = !isOpen;
            MC.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         }

      }
   }

   @Override
   public void draw(int mouseX, int mouseY) {
      y = window.getRenderYButton();
      x = window.getX() + 4;
      updateIsMouseHovered(mouseX, mouseY);

      GuiUtils.drawRect(getX(), getY(), getWidth(), getHeight(), getColor());
      GuiUtils.drawRect(getX(), getY(), -1, getHeight(), Colors.getGlobalColor().getRGB());

      FontUtils.drawStringWithShadow(option.getName(), getX() + 2, getY() + 3, Colors.WHITE);
      RenderUtil.drawBorderedRect(
              getX() + getWidth() - 12.0,
              getY() + 2.0,
              getX() + getWidth() - 2.0,
              getY() + 12.0,
              2.0F,
              Color.BLACK.getRGB(),
              option.getValue().getRGB()
      );
      drawDescription(option.getDesc(), mouseX, mouseY);

      if (isOpen) {
         Color setting = option.getValue();

         float[] hsb = Color.RGBtoHSB(setting.getRed(), setting.getGreen(), setting.getBlue(), null);
         int x = getX() + 2;
         int divisor = 7;
         int width = x + 78;
         int y = getY() + 17;

         RenderUtil.drawColourPicker(
                 x,
                 y,
                 width,
                 y + 80,
                 Color.getHSBColor(hsb[0], 0.0F, 0.0F),
                 Color.getHSBColor(hsb[0], 0.0F, 1.0F),
                 Color.getHSBColor(hsb[0], 1.0F, 0.0F),
                 Color.getHSBColor(hsb[0], 1.0F, 1.0F)
         );
         int sliderBottom = y + 85;

         for (float i = 0.0F; i + 7.0F <= 78.0F; i += 7.0F) {
            RenderUtil.drawGradientRect(
                    x + i, sliderBottom, x + 7 + i, sliderBottom + 10, Color.getHSBColor(i / 78.0F, 1.0F, 1.0F), Color.getHSBColor((i + 7.0F) / 78.0F, 1.0F, 1.0F)
            );
         }

         int alphaX = getX() + getWidth() - 11;
         Color maxAlpha = new Color(setting.getRed(), setting.getGreen(), setting.getBlue(), 255);
         Color minAlpha = new Color(setting.getRed(), setting.getGreen(), setting.getBlue(), 0);
         RenderUtil.drawColourPicker(alphaX, y, getX() + getWidth() - 1.0F, sliderBottom, minAlpha, maxAlpha, minAlpha, maxAlpha);

         int indicatorAlpha = (int)(95 + y - setting.getAlpha() / 255.0F * 95.0F);
         int indicatorHue = x + (int)(hsb[0] * 78.0F);
         int indicatorX = (int)(x + hsb[1] * 78.0F);
         int indicatorY = (int)(80 + y - hsb[2] * 80.0F);

         GuiUtils.drawRect(indicatorHue, sliderBottom, 2, 10, INDICATOR);
         GuiUtils.drawRect(alphaX, indicatorAlpha - 1, 10, 2, INDICATOR);
         GuiUtils.drawRect(indicatorX - 1, indicatorY - 1, 2, 2, INDICATOR);

         onMouseClicked(mouseX, mouseY);
         int i = 114;

         if (option.getShouldRainbow()) {
            GuiUtils.drawRect(getX(), getY() + i, getWidth() - 2, 11, option.getRainbowValue() ? getColor() : 0);
            FontUtils.drawStringWithShadow("Rainbow", getX() + 2, getY() + i + 1, Colors.WHITE);
            i += 14;
         }

         if (option.getShouldGlobal()) {
            GuiUtils.drawRect(getX(), getY() + i, getWidth() - 2, 11, option.getGlobalValue() ? getColor() : 0);
            FontUtils.drawStringWithShadow("Global", getX() + 2, getY() + i + 1, Colors.WHITE);
         }
      }
   }

   @Override
   public int getHeight() {
      if (isOpen) {
         boolean global = option.getShouldGlobal();
         boolean rainbow = option.getShouldRainbow();

         if (global && rainbow) {
            return height + 126;
         } else {
            return !global && !rainbow ? height + 100 : height + 112;
         }
      } else {
         return height;
      }
   }

   @Override
   public boolean shouldRender() {
      return parent.isOpen() && parent.shouldRender();
   }

   @Override
   public int getColor() {
      return ColorUtils.getColorForGuiEntry(2, isMouseHovered(), false);
   }

   public Button getParent() {
      return parent;
   }

   private void onMouseClicked(int mouseX, int mouseY) {
      Color color = option.getValue();

      String colorString = "Red: " + color.getRed() + ", Green: " + color.getGreen() + ", Blue: " + color.getBlue();
      String hexString = "Hex: #" + Colors.getHexString(color.getRGB());

      if (isAreaClickable(mouseX, mouseY, getX() + getWidth() - 11, getY() + 17, 10.0, 95.0)) {
         if (Mouse.isButtonDown(0)) {
            getAlpha(mouseY);
            return;
         }

         if (HudMod.descriptions.getValue()) {
            String alpha = "Alpha: " + option.getValue().getAlpha();
            RenderUtil.drawBorderedRect(
                    mouseX,
                    mouseY + 3,
                    mouseX + FontUtils.getStringWidth(alpha) + 2,
                    mouseY - 9,
                    0.5F,
                    Colors.toRGBA(10, 10, 10, 255),
                    Colors.toRGBA(10, 10, 10, 150)
            );
            FontUtils.drawStringWithShadow(alpha, mouseX + 1, mouseY - 7, Colors.WHITE);
         }
      } else if (isAreaClickable(mouseX, mouseY, getX() + 2, getY() + 102, 76.0, 10.0)) {
         if (Mouse.isButtonDown(0) && !option.getGlobalValue() && !option.getRainbowValue()) {
            getHue(mouseX);
            return;
         }

         if (HudMod.descriptions.getValue()) {
            RenderUtil.drawBorderedRect(
                    mouseX,
                    mouseY + 3,
                    mouseX + FontUtils.getStringWidth(colorString) + 2,
                    mouseY - 18,
                    0.5F,
                    Colors.toRGBA(10, 10, 10, 255),
                    Colors.toRGBA(10, 10, 10, 150)
            );
            FontUtils.drawStringWithShadow(colorString, mouseX + 1, mouseY - 7, Colors.WHITE);
            FontUtils.drawStringWithShadow(hexString, mouseX + 1, mouseY - 17, Colors.WHITE);
         }
      } else if (isAreaClickable(mouseX, mouseY, getX() + 2, getY() + 19, 77.0, 78.0)) {
         if (Mouse.isButtonDown(0) && !option.getGlobalValue() && !option.getRainbowValue()) {
            getRGB(mouseX, mouseY);
            return;
         }

         if (HudMod.descriptions.getValue()) {
            RenderUtil.drawBorderedRect(
                    mouseX,
                    mouseY + 3,
                    mouseX + FontUtils.getStringWidth(colorString) + 2,
                    mouseY - 18,
                    0.5F,
                    Colors.toRGBA(10, 10, 10, 255),
                    Colors.toRGBA(10, 10, 10, 150)
            );
            FontUtils.drawStringWithShadow(colorString, mouseX + 1, mouseY - 7, Colors.WHITE);
            FontUtils.drawStringWithShadow(hexString, mouseX + 1, mouseY - 17, Colors.WHITE);
         }
      }
   }

   private boolean isAreaClickable(int mouseX, int mouseY, double x, double y, double width, double height) {
      return x <= mouseX && mouseX <= x + width && y <= mouseY && mouseY <= y + height;
   }

   private void getRGB(float mouseX, float mouseY) {
      mouseX -= getX();
      mouseY -= getY();

      float saturation = (mouseX - 1.0F) / 78.0F;
      float brightness = 1.0F - (mouseY - 18.0F) / 80.0F;
      float[] hsb = Color.RGBtoHSB(option.getValue().getRed(), option.getValue().getGreen(), option.getValue().getBlue(), null);

      int rgb = Color.HSBtoRGB(hsb[0], saturation, brightness);

      Color color = new Color(rgb);
      option.setValue(new Color(color.getRed(), color.getGreen(), color.getBlue(), option.getValue().getAlpha()));
   }

   private void getHue(float mouseX) {
      mouseX -= getX();

      float hue = (mouseX - 2.0F) / 78.0F;
      float[] hsb = Color.RGBtoHSB(option.getValue().getRed(), option.getValue().getGreen(), option.getValue().getBlue(), null);
      int rgb = Color.HSBtoRGB(hue, hsb[1], hsb[2]);

      Color color = new Color(rgb);
      option.setValue(new Color(color.getRed(), color.getGreen(), color.getBlue(), option.getValue().getAlpha()));
   }

   private void getAlpha(float mouseY) {
      mouseY -= getY();
      int alpha = Math.max(0, 255 - (int)((mouseY - 17.0F) / 95.0F * 255.0F));
      Color setting = option.getValue();
      option.setValue(new Color(setting.getRed(), setting.getGreen(), setting.getBlue(), alpha));
   }
}
