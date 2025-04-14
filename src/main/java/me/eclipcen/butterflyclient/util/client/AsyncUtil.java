package me.eclipcen.butterflyclient.util.client;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public final class AsyncUtil {
   private static final ThreadPoolExecutor EXECUTOR = (ThreadPoolExecutor)Executors.newFixedThreadPool(4);

   public static void execute(Runnable runnable) {
      EXECUTOR.execute(() -> {
         try {
            runnable.run();
         } catch (Throwable throwable) {
            throwable.printStackTrace();
         }
      });
   }
}
