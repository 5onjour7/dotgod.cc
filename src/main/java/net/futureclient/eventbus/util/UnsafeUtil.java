package net.futureclient.eventbus.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class UnsafeUtil {
   private static final Object UNSAFE;
   private static final Method DEFINE_ANONYMOUS_CLASS;

   private UnsafeUtil() {
   }

   public static Class<?> defineAnonymousClass(Class<?> parent, byte[] bytes) {
      try {
         return (Class<?>) DEFINE_ANONYMOUS_CLASS.invoke(UNSAFE, parent, bytes, null);
      } catch (Throwable var3) {
         SneakyThrowUtil.throwSneaky(var3);
         throw null;
      }
   }

   static {
      try {
         Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
         Field theUnsafe = unsafeClass.getDeclaredField("theUnsafe");
         theUnsafe.setAccessible(true);
         UNSAFE = theUnsafe.get(null);
         DEFINE_ANONYMOUS_CLASS = UNSAFE.getClass().getDeclaredMethod("defineAnonymousClass", Class.class, byte[].class, Object[].class);
      } catch (Throwable var2) {
         SneakyThrowUtil.throwSneaky(var2);
         throw null;
      }
   }
}
