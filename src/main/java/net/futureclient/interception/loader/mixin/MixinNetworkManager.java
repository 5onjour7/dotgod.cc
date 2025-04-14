package net.futureclient.interception.loader.mixin;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.crypto.SecretKey;
import net.futureclient.interception.client.Interception;
import net.futureclient.interception.loader.wrapper.IC00Handshake;
import net.futureclient.interception.loader.wrapper.INetworkManager;
import net.futureclient.interception.shared.network.InterceptionC00Handshake;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.util.CryptManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({NetworkManager.class})
public abstract class MixinNetworkManager implements INetworkManager {
   @Unique
   private static final String SERVER_PINGER_THREAD_NAME = "Server Pinger";
   @Unique
   private static final ThreadLocal<Object> CONNECTING_LOOPBACK = new ThreadLocal<>();
   @Unique
   private SecretKey secretKey;
   @Unique
   private String ip;
   @Unique
   private int port;

   @Override
   public SecretKey getSharedSecretKey() {
      return secretKey;
   }

   @Override
   public void setIp(String ip) {
      this.ip = ip;
   }

   @Override
   public void setPort(int port) {
      this.port = port;
   }

   @Unique
   private static boolean isLoopbackOrInternalWireGuard(InetAddress address) {
      if (address instanceof Inet4Address) {
         byte[] b = address.getAddress();
         if (b[0] == -64 && b[1] == -88 && b[2] == 69) {
            return true;
         }
      }

      return address.isLoopbackAddress();
   }

   @Inject(
      method = "createNetworkManagerAndConnect",
      at = @At("HEAD")
   )
   private static void onCreateNetworkManagerAndConnect(InetAddress address, int port, boolean useNativeTransport, CallbackInfoReturnable<NetworkManager> cir) {
      if (isLoopbackOrInternalWireGuard(address)) {
         String hostName = address.getHostName();
         String ip = hostName != null ? hostName : address.getHostAddress();
         System.out.println("[Interception] Ignoring loopback/wireguard address " + ip);
         CONNECTING_LOOPBACK.set(Boolean.TRUE);
      } else {
         CONNECTING_LOOPBACK.set(null);
      }
   }

   @Inject(
      method = "createNetworkManagerAndConnect",
      at = @At("RETURN")
   )
   private static void onCreateNetworkManagerAndReturn(InetAddress address, int port, boolean useNativeTransport, CallbackInfoReturnable<NetworkManager> cir) {
      CONNECTING_LOOPBACK.set(null);
   }

   @Inject(
      method = "createNetworkManagerAndConnect",
      at = @At(
         value = "INVOKE",
         target = "io/netty/channel/epoll/Epoll.isAvailable()Z"
      ),
      locals = LocalCapture.CAPTURE_FAILHARD
   )
   private static void onCreateNetworkManagerAndConnect(
      InetAddress address, int port, boolean useNativeTransport, CallbackInfoReturnable<NetworkManager> cir, NetworkManager networkManager
   ) {
      if (!allowNonProxyConnection()) {
         String hostName = address.getHostName();
         String ip = hostName != null ? hostName : address.getHostAddress();
         INetworkManager wrapper = (INetworkManager)networkManager;
         wrapper.setIp(ip);
         wrapper.setPort(port);
      }
   }

   @Redirect(
      method = "createNetworkManagerAndConnect",
      at = @At(
         value = "INVOKE",
         target = "io/netty/bootstrap/Bootstrap.connect(Ljava/net/InetAddress;I)Lio/netty/channel/ChannelFuture;"
      )
   )
   private static ChannelFuture onSendPacket$address(Bootstrap bootstrap, InetAddress address, int port) throws UnknownHostException {
      if (!allowNonProxyConnection()) {
         String hostName = address.getHostName();
         String ip = hostName != null ? hostName : address.getHostAddress();
         System.out.println("[Interception] Rewriting address and port from " + ip + ":" + port);
         address = InetAddress.getByName(Interception.getProxyIp());
         port = Interception.getProxyPort();
      }

      return bootstrap.connect(address, port);
   }

   @ModifyVariable(
           method = "sendPacket(Lnet/minecraft/network/Packet;)V",
           at = @At("HEAD"),
           argsOnly = true
   )
   private Packet<?> onSendPacket(Packet<?> packet) throws IOException {
      if (packet instanceof C00Handshake && ip != null) {
         C00Handshake handshake = (C00Handshake)packet;
         IC00Handshake handshakeWrapper = (IC00Handshake)handshake;
         System.out.println("[Interception] Rewriting handshake " + ip + ":" + port);
         
         return InterceptionC00Handshake.createInterceptionHandshake(
            ip,
            port,
            handshake.getRequestedState(),
            handshakeWrapper.getIp(),
            handshakeWrapper.getPort(),
            (secretKey = CryptManager.createNewSharedKey()).getEncoded()
         );
      } else {
         return packet;
      }
   }

   @Unique
   private static boolean allowNonProxyConnection() {
      return CONNECTING_LOOPBACK.get() != null || !Interception.isEnabled() || Thread.currentThread().getName().startsWith("Server Pinger");
   }
}
