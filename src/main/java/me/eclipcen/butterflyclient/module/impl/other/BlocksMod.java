package me.eclipcen.butterflyclient.module.impl.other;

import me.eclipcen.butterflyclient.module.api.Category;
import me.eclipcen.butterflyclient.module.api.branches.ToggleMod;
import me.eclipcen.butterflyclient.util.settings.Setting;

public class BlocksMod extends ToggleMod {
   public static Setting<Boolean> swing = new Setting<>("Swing", "Sends a swing packet", false);
   public static Setting<Boolean> rayTrace = new Setting<>("Raytrace", "Checks if you have a line of sight of the adjacent block you will place against", false);
   public static Setting<Boolean> rotate = new Setting<>("Rotate", "Rotates your player towards the block position server-side", false);

   public BlocksMod() {
      super(Category.OTHER, "Blocks", "Master control of all block placing modules");
   }
}
