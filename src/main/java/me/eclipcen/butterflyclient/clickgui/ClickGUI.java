package me.eclipcen.butterflyclient.clickgui;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import me.eclipcen.butterflyclient.module.api.Category;
import me.eclipcen.butterflyclient.util.colors.Colors;
import me.eclipcen.butterflyclient.util.render.RenderUtil;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

public class ClickGUI extends GuiScreen {
   private static ClickGUI INSTANCE;
   private final Set<Window> windows = new LinkedHashSet<>();

   public ClickGUI() {
      INSTANCE = this;
      windows.add(new Window(512, 2, "Other", Category.OTHER));
      windows.add(new Window(104, 2, "Combat", Category.COMBAT));
      windows.add(new Window(410, 2, "Player", Category.PLAYER));
      windows.add(new Window(308, 2, "Movement", Category.MOVEMENT));
      windows.add(new Window(2, 2, "Render", Category.RENDER));
      windows.add(new Window(206, 2, "Misc", Category.MISC));

      for (Window window : windows) {
         window.init(window.getCategory());
      }

   }

   public boolean doesGuiPauseGame() {
      return false;
   }

   public void drawScreen(int x, int y, float ticks) {
      RenderUtil.drawRect(0.0F, 0.0F, (float) width, (float) height, Colors.toRGBA(0, 0, 0, 150));

      for (Window window : windows) {
         window.draw(x, y);
      }

   }

   public void mouseClicked(int x, int y, int b) throws IOException {

      for (Window window : windows) {
         window.processMouseClick(x, y, b);
      }

      super.mouseClicked(x, y, b);
   }

   public void mouseReleased(int x, int y, int state) {

      for (Window window : windows) {
          window.processMouseRelease(x, y, state);
      }

      super.mouseReleased(x, y, state);
   }

   public void handleMouseInput() throws IOException {
      super.handleMouseInput();
      int scrollAmount = 20;
      if (Mouse.getEventDWheel() > 0) {
         for (Window window : windows) {
            if (window.y > 0) {
               return;
            }

            window.y += scrollAmount;
         }
      }

      if (Mouse.getEventDWheel() < 0) {
         for (Window window : windows) {
            window.y -= scrollAmount;
         }
      }
   }

   protected void keyTyped(char eventChar, int eventKey) {
      if (eventKey == 1) {
         mc.displayGuiScreen(null);
         if (mc.currentScreen == null) {
            mc.setIngameFocus();
         }
      }

      for (Window window : windows) {
         window.processKeyPress(eventChar, eventKey);
      }

   }

   public static ClickGUI getInstance() {
      return INSTANCE;
   }

   public Set<Window> getWindows() {
      return windows;
   }
}
