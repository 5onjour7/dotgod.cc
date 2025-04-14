package me.eclipcen.butterflyclient.util.entity;

import me.eclipcen.butterflyclient.util.Globals;
import net.minecraft.util.math.MathHelper;

public class MovementUtils implements Globals {
   public static double getPlayerSpeed() {
      double distTraveledLastTickX = MC.player.posX - MC.player.lastTickPosX;
      double distTraveledLastTickZ = MC.player.posZ - MC.player.lastTickPosZ;
      return (double) MathHelper.sqrt(distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ) * 20.0D * 3.6D;
   }
}
