package me.eclipcen.butterflyclient.launch.mixin.util;

import me.eclipcen.butterflyclient.wrapper.ITimer;
import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Timer.class)
public abstract class MixinTimer implements ITimer {
   @Shadow
   private float tickLength;

   public float getTickLength() {
      return this.tickLength;
   }

   public void setTickLength(float tickLength) {
      this.tickLength = tickLength;
   }
}
