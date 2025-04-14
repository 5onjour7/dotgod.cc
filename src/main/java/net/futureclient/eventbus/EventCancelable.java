package net.futureclient.eventbus;

public abstract class EventCancelable extends Event {
   protected boolean canceled;

   public void cancel() {
      canceled = true;
   }

   public void setCanceled(boolean canceled) {
      this.canceled = canceled;
   }

   public boolean isCanceled() {
      return canceled;
   }
}
