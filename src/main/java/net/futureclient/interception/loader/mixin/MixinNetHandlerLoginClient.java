package net.futureclient.interception.loader.mixin;

import javax.crypto.SecretKey;
import net.futureclient.interception.loader.wrapper.INetworkManager;
import net.futureclient.interception.shared.util.ByteUtil;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.CryptManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(NetHandlerLoginClient.class)
public abstract class MixinNetHandlerLoginClient {
   private static final String SECRET_KEY_STRING = "F6C4443DB248242B36451F7D162C9477";
   @Shadow
   @Final
   private NetworkManager networkManager;

   @Redirect(
      method = "handleEncryptionRequest",
      at = @At(
         value = "INVOKE",
         target = "net/minecraft/util/CryptManager.createNewSharedKey()Ljavax/crypto/SecretKey;"
      )
   )
   private SecretKey onCreateNewSharedKey() {
      SecretKey key = ((INetworkManager) networkManager).getSharedSecretKey();
      if (key != null) {
         System.out.println("[Interception] Overriding key: " + ByteUtil.bytesToHexString(key.getEncoded()));
         return key;
      } else {
         return CryptManager.createNewSharedKey();
      }
   }
}
