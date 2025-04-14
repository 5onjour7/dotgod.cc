package me.eclipcen.butterflyclient.module.impl.misc.announcer.impl;

import me.eclipcen.butterflyclient.module.impl.misc.announcer.MessagePrefixes;
import me.eclipcen.butterflyclient.module.impl.misc.announcer.QueuedTask;
import me.eclipcen.butterflyclient.module.impl.misc.announcer.TaskType;
import me.eclipcen.butterflyclient.util.Globals;

public class TaskMove extends QueuedTask implements Globals {
   public double posX;
   public double posZ;

   public TaskMove(TaskType type) {
      super(type);
      posX = MC.player.posX;
      posZ = MC.player.posZ;
   }

   public String getMessage() {
      return MC.player.posX == posX && MC.player.posZ == posZ
         ? null
         : MessagePrefixes.getMessage(
             TaskType.WALK,
             String.format("%.2f", Math.abs(MC.player.posX - posX) + Math.abs(MC.player.posZ - posZ))
         );
   }
}
