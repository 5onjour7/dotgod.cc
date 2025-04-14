package me.eclipcen.butterflyclient.module.api.branches;

import me.eclipcen.butterflyclient.module.api.Category;
import me.eclipcen.butterflyclient.module.api.Module;
import me.eclipcen.butterflyclient.util.settings.Setting;

public abstract class ToggleMod extends Module {
   public ToggleMod(Category category, String modName, String description) {
      this(category, modName, false, "NONE", description);
   }

   public ToggleMod(Category category, String modName, boolean defaultValue, String defaultKeybind, String description) {
      super(category, modName, description, defaultKeybind);
      toggled = new Setting<>("Toggled", "Toggles the mod", defaultValue);
      drawn = new Setting<>("Drawn", "Show module on the ArrayList", true);
      getSettingList().add(toggled);
      getSettingList().add(drawn);
   }

   public boolean isDrawn() {
      return drawn.getValue();
   }

   public boolean isEnabled() {
      return toggled.getValue();
   }

   public void toggle() {
      toggled.setValue(!toggled.getValue());
      if (toggled.getValue()) {
         start();
      } else {
         stop();
      }

   }
}
