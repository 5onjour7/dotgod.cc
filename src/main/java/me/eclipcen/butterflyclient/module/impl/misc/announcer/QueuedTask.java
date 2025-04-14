package me.eclipcen.butterflyclient.module.impl.misc.announcer;

public abstract class QueuedTask {
   public final TaskType type;

   public QueuedTask(TaskType type) {
      this.type = type;
   }

   public abstract String getMessage();
}
