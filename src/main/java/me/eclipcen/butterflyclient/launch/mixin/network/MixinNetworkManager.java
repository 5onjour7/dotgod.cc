package me.eclipcen.butterflyclient.launch.mixin.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.event.packet.PacketEvent;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public abstract class MixinNetworkManager {
   @Inject(
      method = "dispatchPacket",
      at = @At("HEAD"),
      cancellable = true
   )
   private void onDispatchPacketPre(Packet<?> inPacket, GenericFutureListener<? extends Future<? super Void>>[] futureListeners, CallbackInfo callbackInfo) {
      PacketEvent event = new PacketEvent.Send.Pre(inPacket);
      Butterfly.getEventBus().transmit(event);
      if (event.isCanceled()) {
         callbackInfo.cancel();
      }

   }

   @Inject(
      method = "dispatchPacket",
      at = @At("RETURN")
   )
   private void onDispatchPacketPost(Packet<?> inPacket, GenericFutureListener<? extends Future<? super Void>>[] futureListeners, CallbackInfo callbackInfo) {
      Butterfly.getEventBus().transmit(new PacketEvent.Send.Post(inPacket));
   }

   @Inject(
      method = "channelRead0*",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/network/Packet;processPacket(Lnet/minecraft/network/INetHandler;)V",
         shift = At.Shift.BEFORE
      ),
      cancellable = true
   )
   private void onChannelRead0Pre(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callbackInfo) {
      PacketEvent event = new PacketEvent.Receive.Pre(packet);
      Butterfly.getEventBus().transmit(event);
      if (event.isCanceled()) {
         callbackInfo.cancel();
      }

   }

   @Inject(
      method = "channelRead0*",
      at = @At("RETURN")
   )
   private void onChannelRead0Post(ChannelHandlerContext p_channelRead0_1_, Packet<?> packet, CallbackInfo callbackInfo) {
      Butterfly.getEventBus().transmit(new PacketEvent.Receive.Post(packet));
   }

   @Inject(
      method = "exceptionCaught",
      at = @At("RETURN")
   )
   public void postExceptionCaught(ChannelHandlerContext p_exceptionCaught_1_, Throwable p_exceptionCaught_2_, CallbackInfo callbackInfo) {
      p_exceptionCaught_2_.printStackTrace(System.out);
   }
}
