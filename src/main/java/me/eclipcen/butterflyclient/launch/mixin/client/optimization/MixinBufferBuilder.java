package me.eclipcen.butterflyclient.launch.mixin.client.optimization;

import java.util.List;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumUsage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BufferBuilder.class)
public abstract class MixinBufferBuilder {
   @Shadow
   private VertexFormatElement vertexFormatElement;
   @Shadow
   private int vertexFormatIndex;
   @Shadow
   private VertexFormat vertexFormat;

   @Overwrite
   private void nextVertexFormatIndex() {
      List<VertexFormatElement> elements = this.vertexFormat.getElements();
      int size = elements.size();

      do {
         if (++this.vertexFormatIndex >= size) {
            this.vertexFormatIndex -= size;
         }

         this.vertexFormatElement = elements.get(this.vertexFormatIndex);
      } while(this.vertexFormatElement.getUsage() == EnumUsage.PADDING);

   }
}
