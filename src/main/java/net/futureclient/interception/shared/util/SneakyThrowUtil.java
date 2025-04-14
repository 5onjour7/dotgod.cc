package net.futureclient.interception.shared.util;

public final class SneakyThrowUtil {
   public static <T extends Throwable> void throwSneaky(Throwable throwable) throws T {
       try {
           throw throwable;
       } catch (Throwable e) {
           throw new RuntimeException(e);
       }
   }
}
