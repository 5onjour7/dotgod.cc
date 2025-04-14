package me.eclipcen.butterflyclient.clickgui;

import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.module.impl.other.ColoursMod;
import me.eclipcen.butterflyclient.module.impl.other.HudMod;
import me.eclipcen.butterflyclient.util.Globals;
import me.eclipcen.butterflyclient.util.colors.Colors;
import me.eclipcen.butterflyclient.util.font.FontUtils;
import me.eclipcen.butterflyclient.util.render.RenderUtil;

public abstract class BaseButton implements Globals {
   protected Window window;
   protected final int width;
   protected int height;
   protected int x;
   protected int y;
   private boolean isHoveredCached = false;
   public final ColoursMod colours = Butterfly.getInstance().getModuleManager().getModule(ColoursMod.class);

   public BaseButton(int x, int y, int width, int height) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
   }

   protected boolean isMouseHovered() {
      return isHoveredCached;
   }

   protected void updateIsMouseHovered(int mouseX, int mouseY) {
      int x = getX();
      int y = getY();
      int maxX = x + width;
      int maxY = y + height;
      isHoveredCached = x <= mouseX && mouseX <= maxX && y <= mouseY && mouseY <= maxY;
   }

   public void processMouseClick(int mouseX, int mouseY, int button) {
      updateIsMouseHovered(mouseX, mouseY);
   }

   public void processMouseRelease(int mouseX, int mouseY, int button) {
      updateIsMouseHovered(mouseX, mouseY);
   }

   public void processKeyPress(char character, int key) {
   }

   public int getX() {
      return x;
   }

   public void setX(int x) {
      this.x = x;
   }

   public int getY() {
      return y;
   }

   public void setY(int y) {
      this.y = y;
   }

   public int getWidth() {
      return width;
   }

   public int getHeight() {
      return height;
   }

   public boolean shouldRender() {
      return window.isOpen;
   }

   public void openGui() {
   }

   public Window getWindow() {
      return window;
   }

   public boolean isOpen() {
      return false;
   }

   public abstract void draw(int var1, int var2);

   public abstract int getColor();

   public void drawDescription(String text, int x, int y) {
      if (text != null && !text.isEmpty()) {
         if (isMouseHovered() && HudMod.descriptions.getValue()) {
            RenderUtil.drawBorderedRect(x, y + 3, x + FontUtils.getStringWidth(text) + 2, y - 9, 0.5F, Colors.toRGBA(10, 10, 10, 255), Colors.toRGBA(10, 10, 10, 150));
            FontUtils.drawStringWithShadow(text, (float) (x + 1), (float) (y - 7), Colors.WHITE);
         }

      }
   }
}
