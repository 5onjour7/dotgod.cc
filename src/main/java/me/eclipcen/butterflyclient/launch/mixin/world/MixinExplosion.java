package me.eclipcen.butterflyclient.launch.mixin.world;

import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.event.render.RenderExplosionEvent;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Explosion.class)
public abstract class MixinExplosion {
   @Redirect(
      method = "doExplosionB",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/World;spawnParticle(Lnet/minecraft/util/EnumParticleTypes;DDDDDD[I)V"
      )
   )
   public void doExplosionB(World world, EnumParticleTypes particleType, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int[] parameters) {
      RenderExplosionEvent event = new RenderExplosionEvent();
      Butterfly.getEventBus().transmit(event);
      if (!event.isCanceled()) {
         world.spawnParticle(particleType, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters);
      }

   }
}
