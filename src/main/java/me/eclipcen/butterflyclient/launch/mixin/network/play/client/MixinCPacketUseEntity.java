package me.eclipcen.butterflyclient.launch.mixin.network.play.client;

import me.eclipcen.butterflyclient.wrapper.ICPacketUseEntity;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketUseEntity.Action;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CPacketUseEntity.class)
public abstract class MixinCPacketUseEntity implements ICPacketUseEntity {
   @Accessor
   public abstract int getEntityId();

   @Accessor
   public abstract void setEntityId(int var1);

   @Accessor
   public abstract void setAction(Action var1);
}
