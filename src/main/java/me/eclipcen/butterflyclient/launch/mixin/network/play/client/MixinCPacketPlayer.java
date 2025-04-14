package me.eclipcen.butterflyclient.launch.mixin.network.play.client;

import me.eclipcen.butterflyclient.wrapper.ICPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CPacketPlayer.class)
public abstract class MixinCPacketPlayer implements ICPacketPlayer {
   @Accessor("x")
   public abstract void setX(double var1);

   @Accessor("y")
   public abstract void setY(double var1);

   @Accessor("z")
   public abstract void setZ(double var1);

   @Accessor
   public abstract void setYaw(float var1);

   @Accessor
   public abstract void setPitch(float var1);

   @Accessor
   public abstract void setOnGround(boolean var1);

   @Accessor
   public abstract boolean getRotating();
}
