package me.eclipcen.butterflyclient.util.render;

import java.awt.Color;
import me.eclipcen.butterflyclient.util.Globals;
import me.eclipcen.butterflyclient.wrapper.IRenderManager;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager.CullFace;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public final class RenderUtil implements Globals {
   private static final Tessellator TESSELLATOR = Tessellator.getInstance();
   private static final BufferBuilder BUILDER = TESSELLATOR.getBuffer();
   public static final IRenderManager RENDER_MANAGER = (IRenderManager) MC.getRenderManager();
   public static final ICamera FRUSTUM = new Frustum();
   private static final AxisAlignedBB MUTABLE_BB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

   public static void drawRect(float paramXStart, float paramYStart, float paramXEnd, float paramYEnd, int color) {
      float alpha;
      if (paramXStart < paramXEnd) {
         alpha = paramXStart;
         paramXStart = paramXEnd;
         paramXEnd = alpha;
      }

      if (paramYStart < paramYEnd) {
         alpha = paramYStart;
         paramYStart = paramYEnd;
         paramYEnd = alpha;
      }

      alpha = (float)(color >> 24 & 255) * 0.003921569F;
      float red = (float)(color >> 16 & 255) * 0.003921569F;
      float green = (float)(color >> 8 & 255) * 0.003921569F;
      float blue = (float)(color & 255) * 0.003921569F;
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.disableAlpha();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GL11.glColor4f(red, green, blue, alpha);
      GL11.glBegin(7);
      GL11.glVertex2d((double)paramXEnd, (double)paramYStart);
      GL11.glVertex2d((double)paramXStart, (double)paramYStart);
      GL11.glVertex2d((double)paramXStart, (double)paramYEnd);
      GL11.glVertex2d((double)paramXEnd, (double)paramYEnd);
      GL11.glEnd();
      GlStateManager.enableAlpha();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public static void drawBorderedRect(double x, double y, double x2, double y2, float l1, int col1, int col2) {
      drawRect((float)x, (float)y, (float)x2, (float)y2, col2);
      float f = (float)(col1 >> 24 & 255) * 0.003921569F;
      float f1 = (float)(col1 >> 16 & 255) * 0.003921569F;
      float f2 = (float)(col1 >> 8 & 255) * 0.003921569F;
      float f3 = (float)(col1 & 255) * 0.003921569F;
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glColor4f(f1, f2, f3, f);
      GL11.glLineWidth(l1);
      GL11.glBegin(1);
      GL11.glVertex2d(x, y);
      GL11.glVertex2d(x, y2);
      GL11.glVertex2d(x2, y2);
      GL11.glVertex2d(x2, y);
      GL11.glVertex2d(x, y);
      GL11.glVertex2d(x2, y);
      GL11.glVertex2d(x, y2);
      GL11.glVertex2d(x2, y2);
      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
      GL11.glPopMatrix();
   }

   public static void drawColourPicker(double x, double y, double width, double height, Color bottomLeft, Color topLeft, Color bottomRight, Color topRight) {
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.disableAlpha();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.shadeModel(7425);
      BUILDER.begin(7, DefaultVertexFormats.POSITION_COLOR);
      BUILDER.pos(width, y, 0.0D).color(topRight.getRed(), topRight.getGreen(), topRight.getBlue(), topRight.getAlpha()).endVertex();
      BUILDER.pos(x, y, 0.0D).color(topLeft.getRed(), topLeft.getGreen(), topLeft.getBlue(), topLeft.getAlpha()).endVertex();
      BUILDER.pos(x, height, 0.0D).color(bottomLeft.getRed(), bottomLeft.getGreen(), bottomLeft.getBlue(), bottomLeft.getAlpha()).endVertex();
      BUILDER.pos(width, height, 0.0D).color(bottomRight.getRed(), bottomRight.getGreen(), bottomRight.getBlue(), bottomRight.getAlpha()).endVertex();
      TESSELLATOR.draw();
      GlStateManager.enableAlpha();
      GlStateManager.disableBlend();
      GlStateManager.shadeModel(7424);
      GlStateManager.enableTexture2D();
   }

   public static void drawGradientRect(double x, float y, double width, double height, Color left, Color right) {
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.disableAlpha();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.shadeModel(7425);
      BUILDER.begin(7, DefaultVertexFormats.POSITION_COLOR);
      BUILDER.pos(width, (double)y, 0.0D).color(right.getRed(), right.getGreen(), right.getBlue(), right.getAlpha()).endVertex();
      BUILDER.pos(x, (double)y, 0.0D).color(left.getRed(), left.getGreen(), left.getBlue(), left.getAlpha()).endVertex();
      BUILDER.pos(x, height, 0.0D).color(left.getRed(), left.getGreen(), left.getBlue(), left.getAlpha()).endVertex();
      BUILDER.pos(width, height, 0.0D).color(right.getRed(), right.getGreen(), right.getBlue(), right.getAlpha()).endVertex();
      TESSELLATOR.draw();
      GlStateManager.enableAlpha();
      GlStateManager.disableBlend();
      GlStateManager.shadeModel(7424);
      GlStateManager.enableTexture2D();
   }

   public static void enableGl3D() {
      enableGl3D(1.0F);
   }

   public static void enableGl3D(float lineWidth) {
      GlStateManager.pushMatrix();
      GlStateManager.disableTexture2D();
      GlStateManager.enableBlend();
      GlStateManager.disableAlpha();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.shadeModel(7425);
      GlStateManager.disableDepth();
      GlStateManager.disableLighting();
      GlStateManager.enableCull();
      GlStateManager.cullFace(CullFace.BACK);
      GlStateManager.glLineWidth(lineWidth);
      GL11.glEnable(2848);
      GL11.glHint(3154, 4353);
   }

   public static void disableGl3D() {
      GL11.glDisable(2848);
      GlStateManager.glLineWidth(1.0F);
      GlStateManager.shadeModel(7424);
      GlStateManager.disableBlend();
      GlStateManager.enableAlpha();
      GlStateManager.enableTexture2D();
      GlStateManager.enableDepth();
      GlStateManager.enableCull();
      GlStateManager.popMatrix();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
   }
}
