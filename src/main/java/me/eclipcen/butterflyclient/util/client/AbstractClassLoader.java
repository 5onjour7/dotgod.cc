package me.eclipcen.butterflyclient.util.client;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import me.eclipcen.butterflyclient.util.Globals;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;

public abstract class AbstractClassLoader<E> implements Globals {
   protected AbstractClassLoader() {
   }

   @Nullable
   public abstract Class<E> getInheritedClass();

   public Collection<Class<? extends E>> filterClassPaths(ClassLoader classLoader, Collection<Path> classPaths) {
      List<Class<? extends E>> results = new ArrayList<>();

      for (Class<?> clazz : ClassLoaderHelper.getLoadedClasses(classLoader, classPaths)) {
         if (checkInheritedClass(clazz)) {
            Class<? extends E> wildCast = wildCast(clazz);
            if (valid(wildCast)) {
               results.add(wildCast);
            }
         }
      }

      return results;
   }

   protected boolean valid(Class<? extends E> clazz) {
       return true;
   }

   protected E create(Class<? extends E> clazz) {
      try {
         return clazz.getDeclaredConstructor().newInstance();
      } catch (InstantiationException
               | IllegalAccessException
               | InvocationTargetException
               | NoSuchMethodException e) {
         LOGGER.error("Failed to create new instance of {}", clazz.getSimpleName());
         LOGGER.error(e, e);
         return null;
      }
   }

   private Class<? extends E> wildCast(Class<?> clazz) {
      return (Class<? extends E>) clazz;
   }

   private boolean checkInheritedClass(Class<?> clazz) {
      return getInheritedClass() == null || getInheritedClass().isAssignableFrom(clazz);
   }

   public LaunchClassLoader getFMLClassLoader() {
      return Launch.classLoader;
   }
}
