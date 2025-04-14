package net.futureclient.eventbus.util;

public final class SneakyThrowUtil {
   private SneakyThrowUtil() {
   }

   public static <T extends Throwable> void throwSneaky(Throwable throwable) throws T {
      try {
         throw throwable;
      } catch (Throwable e) {
         throw new RuntimeException(e);
      }
   }
}
