package me.eclipcen.butterflyclient.util.font;

import java.awt.Font;
import me.eclipcen.butterflyclient.util.Globals;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

public class CFontRenderer extends CFont implements Globals {
   protected CFont.CharData[] boldChars = new CFont.CharData[256];
   protected CFont.CharData[] italicChars = new CFont.CharData[256];
   protected CFont.CharData[] boldItalicChars = new CFont.CharData[256];
   private final int[] colorCode = new int[32];
   protected DynamicTexture texBold;
   protected DynamicTexture texItalic;
   protected DynamicTexture texItalicBold;

   public CFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics)
   {
      super(font, antiAlias, fractionalMetrics);
      setupMinecraftColorcodes();
      setupBoldItalicIDs();
   }

   public float drawStringWithShadow(String text, float x, float y, int color)
   {
      float shadowWidth = drawString(text, x + 1.0F, y + 1.0F, color, true);
      return Math.max(shadowWidth, drawString(text, x, y, color, false));
   }

   public float drawString(String text, float x, float y, int color)
   {
      return drawString(text, x, y, color, false);
   }

   public float drawCenteredStringWithShadow(String text, float x, float y, int color)
   {
      return drawStringWithShadow(text, x - getStringWidth(text) * 0.5F, y, color);
   }


   public float drawString(String text, float x, float y, int color, boolean shadow) {
      --x;
      if (text == null) {
         return 0.0F;
      } else {
         if (color == 553648127) {
            color = 16777215;
         }

         if ((color & -67108864) == 0) {
            color |= -16777216;
         }

         if (shadow) {
            color = (color & 16579836) >> 2 | color & -16777216;
         }

         CFont.CharData[] currentData = charData;
         x *= 2.0F;
         y = (y - 3.0F) * 2.0F;
         GL11.glPushMatrix();
         GlStateManager.scale(0.5D, 0.5D, 0.5D);
         GlStateManager.enableBlend();
         GlStateManager.blendFunc(770, 771);
         float alpha = (float)(color >> 24 & 255) * 0.003921569F;
         GlStateManager.color((float)(color >> 16 & 255) * 0.003921569F, (float)(color >> 8 & 255) * 0.003921569F, (float)(color & 255) * 0.003921569F, alpha);
         GlStateManager.enableTexture2D();
         GlStateManager.bindTexture(tex.getGlTextureId());
         GL11.glBegin(4);
         int size = text.length();

         for(int i = 0; i < size; ++i) {
            char character = text.charAt(i);
            if (character == 167) {
               if (i + 1 > size) {
                  break;
               }

               int colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i + 1));
               if (colorIndex < 16) {
                  currentData = charData;
                  if (colorIndex < 0) {
                     colorIndex = 15;
                  }

                  if (shadow) {
                     colorIndex += 16;
                  }

                  int colorCode = this.colorCode[colorIndex];
                  GlStateManager.color((float)(colorCode >> 16 & 255) * 0.003921569F, (float)(colorCode >> 8 & 255) * 0.003921569F, (float)(colorCode & 255) * 0.003921569F, alpha);
               }

               ++i;
            } else if (character < currentData.length) {
               drawChar(currentData, character, x, y);
               x += (float)(currentData[character].width - 8);
            }
         }

         GL11.glEnd();
         GL11.glPopMatrix();
         return x * 0.5F;
      }
   }

   @Override
   public int getStringWidth(String text) {
      if (text == null) {
         return 0;
      } else {
         int width = 0;
         CharData[] currentData = this.charData;
         int size = text.length();

         for (int i = 0; i < size; i++) {
            char character = text.charAt(i);
            if (character == 167) {
               i++;
            } else if (character < currentData.length) {
               width += currentData[character].width - 8;
            }
         }

         return width >> 1;
      }
   }

   public void setFont(Font font) {
      super.setFont(font);
      setupBoldItalicIDs();
   }

   public void setAntiAlias(boolean antiAlias) {
      super.setAntiAlias(antiAlias);
      setupBoldItalicIDs();
   }

   public void setFractionalMetrics(boolean fractionalMetrics) {
      super.setFractionalMetrics(fractionalMetrics);
      setupBoldItalicIDs();
   }

   private void setupBoldItalicIDs() {
      texBold = setupTexture(this.font.deriveFont(Font.BOLD), this.antiAlias, this.fractionalMetrics, this.boldChars);
      texItalic = setupTexture(this.font.deriveFont(Font.ITALIC), this.antiAlias, this.fractionalMetrics, this.italicChars);
      texItalicBold = setupTexture(this.font.deriveFont(Font.BOLD | Font.ITALIC), this.antiAlias, this.fractionalMetrics, this.boldItalicChars);
   }


   private void setupMinecraftColorcodes() {
      for (int index = 0; index < 32; index++) {
         int noClue = (index >> 3 & 0x1) * 85;
         int red = (index >> 2 & 0x1) * 170 + noClue;
         int green = (index >> 1 & 0x1) * 170 + noClue;
         int blue = (index & 0x1) * 170 + noClue;

         if (index == 6) {
            red += 85;
         }

         if (index >= 16) {
            red /= 4;
            green /= 4;
            blue /= 4;
         }

         this.colorCode[index] = ((red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF);
      }
   }
}
