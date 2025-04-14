package me.eclipcen.butterflyclient.launch.mixin.client.renderer.entity;

import me.eclipcen.butterflyclient.wrapper.IRenderManager;
import net.minecraft.client.renderer.entity.RenderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderManager.class)
public abstract class MixinRenderManager implements IRenderManager {
   @Accessor
   public abstract double getRenderPosX();

   @Accessor
   public abstract double getRenderPosY();

   @Accessor
   public abstract double getRenderPosZ();
}
