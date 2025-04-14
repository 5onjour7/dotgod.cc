package me.eclipcen.butterflyclient.module.impl.misc.announcer.impl;

import me.eclipcen.butterflyclient.module.impl.misc.announcer.MessagePrefixes;
import me.eclipcen.butterflyclient.module.impl.misc.announcer.QueuedTask;
import me.eclipcen.butterflyclient.module.impl.misc.announcer.TaskType;
import net.minecraft.block.Block;

public class TaskBlock extends QueuedTask {
   public Block block;
   public int count = 1;

   public TaskBlock(TaskType type, Block block) {
      super(type);
      this.block = block;
   }

   public String getMessage() {
      return MessagePrefixes.getMessage(type, block.getLocalizedName(), String.valueOf(count));
   }
}
