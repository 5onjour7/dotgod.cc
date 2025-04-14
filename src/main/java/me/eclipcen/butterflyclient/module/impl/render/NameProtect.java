package me.eclipcen.butterflyclient.module.impl.render;

import me.eclipcen.butterflyclient.module.api.Category;
import me.eclipcen.butterflyclient.module.api.branches.ToggleMod;
import me.eclipcen.butterflyclient.util.settings.Setting;

public class NameProtect extends ToggleMod {
   public final Setting<String> name = new Setting<>("Name", "bitchnigga54");

   public NameProtect() {
      super(Category.RENDER, "NameProtect", "scooby doo mod but replace the o's with e's");
   }
}
