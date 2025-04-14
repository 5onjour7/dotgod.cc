package me.eclipcen.butterflyclient.util.font;

import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.module.impl.other.HudMod;
import me.eclipcen.butterflyclient.module.impl.render.NameProtect;
import me.eclipcen.butterflyclient.util.Globals;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.StringUtils;

public class FontUtils implements Globals {
   public static float drawStringWithShadow(String text, float x, float y, int color) {
      NameProtect nameProtect = Butterfly.getInstance().getModuleManager().getModule(NameProtect.class);
      if (nameProtect.isEnabled()) {
         text = StringUtils.replace(text, Minecraft.getMinecraft().player.getName(), nameProtect.name.getValue());
      }

      return HudMod.customFont.getValue() ? Butterfly.getInstance().getFontRenderer().drawStringWithShadow(text, x, y, color) : (float) MC.fontRenderer.drawStringWithShadow(text, x, y, color);
   }

   public static float drawCentredString(String text, float x, float y, int color) {
      return HudMod.customFont.getValue() ? Butterfly.getInstance().getFontRenderer().drawCenteredStringWithShadow(text, x, y, color) : (float) MC.fontRenderer.drawStringWithShadow(text, x - (float)MC.fontRenderer.getStringWidth(text) * 0.5F, y, color);
   }

   public static int getStringWidth(String str) {
      NameProtect nameProtect = Butterfly.getInstance().getModuleManager().getModule(NameProtect.class);
      if (nameProtect.isEnabled()) {
         str = StringUtils.replace(str, Minecraft.getMinecraft().player.getName(), nameProtect.name.getValue());
      }

      return HudMod.customFont.getValue() ? Butterfly.getInstance().getFontRenderer().getStringWidth(str) : MC.fontRenderer.getStringWidth(str);
   }

   public static int getFontHeight() {
      return HudMod.customFont.getValue() ? Butterfly.getInstance().getFontRenderer().getHeight() : MC.fontRenderer.FONT_HEIGHT;
   }
}
