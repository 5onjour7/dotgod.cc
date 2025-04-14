package me.eclipcen.butterflyclient.module.impl.render;

import java.awt.Color;
import me.eclipcen.butterflyclient.event.render.RenderEvent;
import me.eclipcen.butterflyclient.module.api.Category;
import me.eclipcen.butterflyclient.module.api.branches.ToggleMod;
import me.eclipcen.butterflyclient.util.font.FontUtils;
import me.eclipcen.butterflyclient.util.settings.Setting;
import net.futureclient.eventbus.SubscribeEvent;
import net.minecraft.util.math.MathHelper;

public class CompassMod extends ToggleMod {
   public Setting<Integer> scale = new Setting<>("Scale", "Scale of the compass", 3, 1, 10, 1);
   private static final double HALF_PI = Math.PI / 2;
   private final CompassMod.Direction[] directions = CompassMod.Direction.values();

   public CompassMod() {
      super(Category.RENDER, "Compass", "Puts a cool compass on the screen (thanks to forgehax)");
   }

   @SubscribeEvent
   public void onRender(RenderEvent.Render2DEvent event) {
      double centerX = event.getScreenWidth() * 0.5D;
      double centerY = event.getScreenHeight() * 0.8D;

      for (CompassMod.Direction dir : directions) {
         double rad = getPosOnCompass(dir);

         FontUtils.drawCentredString(
                 dir.name(),
                 (float) (centerX + getX(rad)),
                 (float) (centerY + getY(rad)),
                 dir == CompassMod.Direction.N ? Color.RED.getRGB() : Color.WHITE.getRGB()
         );
      }

   }


   private double getX(double rad) {
      return Math.sin(rad) * (scale.getValue() * 10);
   }

   private double getY(double rad) {
      final double epicPitch = MathHelper
              .clamp(mc.player.rotationPitch + 30f, -90f, 90f);
      final double pitchRadians = Math.toRadians(epicPitch); // player pitch
      return Math.cos(rad) * Math.sin(pitchRadians) * (scale.getValue() * 10);
   }

   private double getPosOnCompass(Direction dir) {
      double yaw =
              Math.toRadians(
                      MathHelper.wrapDegrees(mc.player.rotationYaw)); // player yaw
      int index = dir.ordinal();
      return yaw + (index * HALF_PI);
   }

   private enum Direction {
      N,
      W,
      S,
      E
   }


}
