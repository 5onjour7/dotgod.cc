package me.eclipcen.butterflyclient.util.client;

public final class Timer {
   private long time = -1L;

   public boolean passed(long ms) {
      return System.currentTimeMillis() - time >= ms;
   }

   public void reset() {
      time = System.currentTimeMillis();
   }
}
