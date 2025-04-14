package me.eclipcen.butterflyclient.wrapper;

import net.minecraft.client.network.NetHandlerPlayClient;

public interface IPlayerControllerMP {
   void setBlockHitDelay(int var1);

   void setCurBlockDamageMP(float var1);

   float getCurBlockDamageMP();

   NetHandlerPlayClient getConnection();

   void doSyncCurrentPlayItem();
}
