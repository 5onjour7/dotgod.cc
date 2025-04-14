package net.futureclient.eventbus.util;

public final class TryUtil {
   private TryUtil() {
   }

   public static <T> T tryOrElseNull(TryUtil.SupplierWithThrowable<T, ?> supplier) {
      return tryOrElse(supplier, null);
   }

   public static <T> T tryOrElse(TryUtil.SupplierWithThrowable<T, ?> supplier, T value) {
      try {
         return supplier.get();
      } catch (Throwable var3) {
         return value;
      }
   }

   @FunctionalInterface
   public interface SupplierWithThrowable<T, E extends Throwable> {
      T get() throws E;
   }
}
