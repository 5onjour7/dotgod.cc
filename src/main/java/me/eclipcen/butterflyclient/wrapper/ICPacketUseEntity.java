package me.eclipcen.butterflyclient.wrapper;

import net.minecraft.network.play.client.CPacketUseEntity.Action;

public interface ICPacketUseEntity {
   int getEntityId();

   void setEntityId(int var1);

   void setAction(Action var1);
}
