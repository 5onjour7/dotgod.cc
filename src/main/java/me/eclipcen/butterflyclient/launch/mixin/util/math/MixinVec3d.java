package me.eclipcen.butterflyclient.launch.mixin.util.math;

import me.eclipcen.butterflyclient.wrapper.IMutableVec3d;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Vec3d.class)
public abstract class MixinVec3d implements IMutableVec3d {
   @Shadow
   @Final
   @Mutable
   public double x;
   @Shadow
   @Final
   @Mutable
   public double y;
   @Shadow
   @Final
   @Mutable
   public double z;

   public Vec3d setPosition(double x, double y, double z) {
      this.x = x;
      this.y = y;
      this.z = z;
      return (Vec3d) (Object) this;
   }
}
