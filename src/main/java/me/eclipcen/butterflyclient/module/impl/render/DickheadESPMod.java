package me.eclipcen.butterflyclient.module.impl.render;

import java.util.Iterator;
import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.event.render.RenderEvent;
import me.eclipcen.butterflyclient.module.api.Category;
import me.eclipcen.butterflyclient.module.api.branches.ToggleMod;
import me.eclipcen.butterflyclient.util.entity.EntityUtils;
import me.eclipcen.butterflyclient.util.render.RenderUtil;
import net.futureclient.eventbus.SubscribeEvent;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.Sphere;

public class DickheadESPMod extends ToggleMod {
   private static final Cylinder SHAFT = new Cylinder();
   private static final Sphere BALLS = new Sphere();
   private static final Sphere TIP = new Sphere();
   private static final int LINES = 20;

   public DickheadESPMod() {
      super(Category.RENDER, "DickheadESP", "ur mom is a dickhead");
      SHAFT.setDrawStyle(100013);
      BALLS.setDrawStyle(100013);
      TIP.setDrawStyle(100013);
   }

   @SubscribeEvent
   public void onRender(RenderEvent event) {
      RenderUtil.enableGl3D();

       for (EntityPlayer entityPlayer : this.mc.world.playerEntities) {
           if (entityPlayer != mc.player && !Butterfly.getInstance().getFriendManager().isFriend(entityPlayer.getName())) {
               Vec3d renderPos = EntityUtils.getInterpolatedRenderPos(entityPlayer, event.getPartialTicks());

               GlStateManager.pushMatrix();
               GlStateManager.translate(renderPos.x, renderPos.y, renderPos.z);
               GlStateManager.rotate(-entityPlayer.rotationYaw, 0.0F, entityPlayer.height, 0.0F);
               GlStateManager.translate(-renderPos.x, -renderPos.y, -renderPos.z);
               GlStateManager.translate(renderPos.x, renderPos.y + 1.8D, renderPos.z + 0.15D);
               GlStateManager.rotate(entityPlayer.rotationPitch, 1.0F, 0.0F, 0.0F);
               GlStateManager.color(0.8039216F, 0.52156866F, 0.24705882F, 1.0F);
               GlStateManager.translate(0.0D, 0.0D, 0.075D);
               SHAFT.draw(0.1F, 0.09F, 1.0F, 25, 40);

               GlStateManager.translate(0.0D, 0.0D, -0.075D);
               GlStateManager.translate(-0.05D, 0.0D, 0.0D);
               BALLS.draw(0.1F, 25, 20);
               GlStateManager.translate(0.1D, 0.0D, 0.0D);
               BALLS.draw(0.1F, 25, 20);

               GlStateManager.color(1.0F, 0.2F, 1.0F, 1.0F);
               GlStateManager.translate(-0.05D, 0.0D, 1.1D);
               TIP.draw(0.09F, 25, 20);

               GlStateManager.popMatrix();
           }
       }

      RenderUtil.disableGl3D();
   }
}
