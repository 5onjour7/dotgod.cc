package net.futureclient.interception.loader.wrapper;

import javax.crypto.SecretKey;

public interface INetworkManager {
   SecretKey getSharedSecretKey();

   void setIp(String var1);

   void setPort(int var1);
}
