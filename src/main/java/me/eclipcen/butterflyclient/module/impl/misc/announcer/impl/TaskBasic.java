package me.eclipcen.butterflyclient.module.impl.misc.announcer.impl;

import me.eclipcen.butterflyclient.module.impl.misc.announcer.QueuedTask;
import me.eclipcen.butterflyclient.module.impl.misc.announcer.TaskType;

public class TaskBasic extends QueuedTask {
   public String message;

   public TaskBasic(TaskType type, String message) {
      super(type);
      this.message = message;
   }

   public String getMessage() {
      return message;
   }
}
