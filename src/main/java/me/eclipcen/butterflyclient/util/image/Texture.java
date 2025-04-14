package me.eclipcen.butterflyclient.util.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import javax.imageio.ImageIO;
import me.eclipcen.butterflyclient.util.Globals;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class Texture implements AutoCloseable, Globals {
   protected static DynamicTexture loadTexture(BufferedImage image) {
      try {
         return new DynamicTexture(image);
      } catch (RuntimeException e) {
         if ("No OpenGL context found in the current thread.".equalsIgnoreCase(e.getMessage())) {
            //load async
            try {
               return MC.addScheduledTask(() -> new DynamicTexture(image)).get();
            } catch (InterruptedException | ExecutionException e1) {
               throw new RuntimeException(e1);
            }
         } else {
            throw e;
         }
      }
   }

   public final ResourceLocation texture;
   protected volatile Runnable cleaner;

   public Texture(byte[] in) throws IOException {
      this(new ByteArrayInputStream(in));
   }

   public Texture(InputStream in) throws IOException {
      this(ImageIO.read(in));
   }

   public Texture(BufferedImage img) {
      this(MC.getTextureManager().getDynamicTextureLocation(UUID.randomUUID().toString(), loadTexture(img)), true);
   }

   public Texture(ResourceLocation texture) {
      this(texture, false);
   }

   public Texture(ResourceLocation texture, boolean clean) {
      this.texture = texture;
      this.cleaner = clean ? () -> MC.addScheduledTask(() -> MC.getTextureManager().deleteTexture(texture)) : null;
   }

   public void render(float x, float y, float width, float height) {
      this.bindTexture();
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder renderer = tessellator.getBuffer();
      renderer.begin(7, DefaultVertexFormats.POSITION_TEX);
      renderer.pos(x, y + height, 0F).tex(0, 1).endVertex();
      renderer.pos(x + width, y + height, 0F).tex(1, 1).endVertex();
      renderer.pos(x + width, y, 0F).tex(1, 0).endVertex();
      renderer.pos(x, y, 0F).tex(0, 0).endVertex();
      tessellator.draw();
   }

   public void bindTexture() {
      MC.getTextureManager().bindTexture(this.texture);
      GlStateManager.enableTexture2D();
   }

   @Override
   public void close() {
      if (cleaner != null) {
         cleaner.run();
         cleaner = null;
      }
   }

   @Override
   public String toString() {
      return this.texture.getPath();
   }

   protected void finalize() throws Throwable {
      if (cleaner != null) {
         cleaner.run();
      }

      super.finalize();
   }
}