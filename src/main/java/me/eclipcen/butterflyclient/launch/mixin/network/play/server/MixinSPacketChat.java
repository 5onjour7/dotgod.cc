package me.eclipcen.butterflyclient.launch.mixin.network.play.server;

import me.eclipcen.butterflyclient.wrapper.ISPacketChat;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SPacketChat.class)
public abstract class MixinSPacketChat implements ISPacketChat {
   @Accessor
   public abstract void setChatComponent(ITextComponent var1);
}
