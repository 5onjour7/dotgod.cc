package me.eclipcen.butterflyclient.launch.mixin.network.play.server;

import me.eclipcen.butterflyclient.wrapper.ISPacketExplosion;
import net.minecraft.network.play.server.SPacketExplosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SPacketExplosion.class)
public abstract class MixinSPacketExplosion implements ISPacketExplosion {
   @Accessor
   public abstract void setMotionX(float var1);

   @Accessor
   public abstract void setMotionY(float var1);

   @Accessor
   public abstract void setMotionZ(float var1);
}
