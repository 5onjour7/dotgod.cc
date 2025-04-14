package net.futureclient.interception.loader.mixin;

import net.futureclient.interception.loader.wrapper.IC00Handshake;
import net.minecraft.network.handshake.client.C00Handshake;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(C00Handshake.class)
public abstract class MixinC00Handshake implements IC00Handshake {
   @Accessor
   @Override
   public abstract String getIp();

   @Accessor
   @Override
   public abstract int getPort();
}
