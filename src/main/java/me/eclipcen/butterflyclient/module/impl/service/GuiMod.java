package me.eclipcen.butterflyclient.module.impl.service;

import java.util.Iterator;
import me.eclipcen.butterflyclient.clickgui.ClickGUI;
import me.eclipcen.butterflyclient.clickgui.Window;
import me.eclipcen.butterflyclient.module.api.Category;
import me.eclipcen.butterflyclient.module.api.branches.ToggleMod;
import me.eclipcen.butterflyclient.util.settings.Setting;

public class GuiMod extends ToggleMod {
   public static Setting<Boolean> boxes = new Setting<>("Rectanlges", "Draws a box around modules when enabled", true);

   public GuiMod() {
      super(Category.OTHER, "ClickGUI", false, "RCONTROL", "Opens the GUI");
   }

   public boolean isDrawn() {
      return false;
   }

   public void onEnable() {
      if (mc.player != null) {

         for (Window window : ClickGUI.getInstance().getWindows()) {
            window.openGui();
         }

         mc.displayGuiScreen(ClickGUI.getInstance());
         toggle();
      }
   }
}
