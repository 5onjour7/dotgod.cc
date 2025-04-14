package me.eclipcen.butterflyclient.util.entity;

import me.eclipcen.butterflyclient.util.Globals;
import me.eclipcen.butterflyclient.wrapper.IRenderManager;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public final class EntityUtils implements Globals {
   private static final IRenderManager RENDER_MANAGER = (IRenderManager) MC.getRenderManager();

   public static Vec3d getInterpolatedRenderPos(Entity entity, float ticks) {
      return new Vec3d(
              entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) ticks - RENDER_MANAGER.getRenderPosX(),
              entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) ticks - RENDER_MANAGER.getRenderPosY(),
              entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) ticks - RENDER_MANAGER.getRenderPosZ()
      );
   }

   public static int getPing(EntityPlayer player) {
      int ping = -1;
      if (player != null && MC.getConnection() != null) {
         NetworkPlayerInfo info = MC.getConnection().getPlayerInfo(player.getUniqueID());
         if (info != null) {
            ping = info.getResponseTime();
         }
      }

      return ping;
   }
}
