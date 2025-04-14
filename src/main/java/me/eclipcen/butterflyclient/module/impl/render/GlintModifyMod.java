package me.eclipcen.butterflyclient.module.impl.render;

import java.awt.Color;
import me.eclipcen.butterflyclient.event.render.RenderItemGlintEffectEvent;
import me.eclipcen.butterflyclient.module.api.Category;
import me.eclipcen.butterflyclient.module.api.branches.ToggleMod;
import me.eclipcen.butterflyclient.util.settings.Setting;
import me.eclipcen.butterflyclient.util.settings.setting.ColorSetting;
import net.futureclient.eventbus.SubscribeEvent;

public class GlintModifyMod extends ToggleMod {
   private final Setting<Color> color = new ColorSetting("Color", "color of the render box", new Color(255, 139, 252), true, true, false);

   public GlintModifyMod() {
      super(Category.RENDER, "GlintModify", "Modifies the enchantment glint");
   }

   @SubscribeEvent
   public void onGlint(RenderItemGlintEffectEvent event) {
      event.setColor(color.getValue().getRGB());
   }
}
