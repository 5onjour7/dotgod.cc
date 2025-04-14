package me.eclipcen.butterflyclient.module.impl.other;

import java.awt.Color;
import me.eclipcen.butterflyclient.module.api.Category;
import me.eclipcen.butterflyclient.module.api.branches.ToggleMod;
import me.eclipcen.butterflyclient.util.settings.Setting;
import me.eclipcen.butterflyclient.util.settings.setting.ColorSetting;

public class ColoursMod extends ToggleMod {
   public final Setting<Integer> brightness = new Setting<>("Brightness", "Brightness of rainbow colours", 255, 0, 255, 1);
   public final Setting<Integer> saturation = new Setting<>("Saturation", "Saturation of rainbow colours", 255, 0, 255, 1);
   public final Setting<Float> rainbowSpeed = new Setting<>("RainbowSpeed", "", 3.0F, 1.0F, 10.0F, 0.1F);
   public final Setting<Color> disabled = new ColorSetting("Disabled", "colour of disabled modules", new Color(150, 150, 150, 255));
   public final Setting<Color> active = new ColorSetting("Active", "colour of active modules", new Color(255, 183, 255, 249));
   public final Setting<Color> boxDisabled = new ColorSetting("DisabledBox", "colour of disabled box", new Color(255, 128, 255, 47));
   public final Setting<Color> boxActive = new ColorSetting("ActiveBox", "colour of enabled box", new Color(255, 128, 255, 128));
   public final Setting<Color> window = new ColorSetting("Window", "colour of window", new Color(255, 255, 255, 28));
   public final Setting<Color> friend = new ColorSetting("Friend", "friends color", new Color(0, 255, 255, 255));
   public final Setting<Color> global = new ColorSetting("Global", "Global Color scheme", new Color(255, 100, 255, 122), true, false, false);

   public ColoursMod() {
      super(Category.OTHER, "Colours", "Control a lot of different colour settings");
   }
}
