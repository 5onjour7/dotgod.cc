package me.eclipcen.butterflyclient.module.api;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.command.impl.ModuleCommand;
import me.eclipcen.butterflyclient.module.api.branches.ServiceMod;
import me.eclipcen.butterflyclient.module.api.branches.ToggleMod;
import me.eclipcen.butterflyclient.util.client.AbstractClassLoader;
import me.eclipcen.butterflyclient.util.client.ClassLoaderHelper;
import me.eclipcen.butterflyclient.util.settings.Setting;

public final class ModuleManager extends AbstractClassLoader<Module> {
   private final Map<Class<? extends Module>, Module> mods = new HashMap<>();

   @Override
   public Class<Module> getInheritedClass() {
      return Module.class;
   }

   public void registerMods() {
      String packageDir = "me.eclipcen.butterflyclient.module.impl.*";

      try {
         for (Class<? extends Module> clazz : filterClassPaths(
                 getFMLClassLoader(), ClassLoaderHelper.getClassPathsInPackage(getFMLClassLoader(), "me.eclipcen.butterflyclient.module.impl.*")
         )) {
            Module module = create(clazz);
            registerModule(module);
         }
      } catch (IOException var6) {
         var6.printStackTrace();
      }
   }

   public void registerModule(@Nonnull Module mod) {
      try {
         mods.put(mod.getClass(), mod);

         for (Field field : mod.getClass().getDeclaredFields()) {
            if (Setting.class.isAssignableFrom(field.getType())) {
               if (!field.isAccessible()) {
                  field.setAccessible(true);
               }

               Setting val = (Setting)field.get(mod);
               mod.getSettingList().add(val);
            }
         }
      } catch (Exception var7) {
         var7.printStackTrace();
      }

      if (mod instanceof ToggleMod) {
         ToggleMod toggleMod = (ToggleMod)mod;
         if (toggleMod.isEnabled()) {
            toggleMod.toggle();
         }

         Butterfly.getInstance().getCommandManager().registerCommand(new ModuleCommand(toggleMod));
      }

      if (mod instanceof ServiceMod) {
         mod.start();
      }
   }

   public <T extends Module> T getModule(Class<? extends Module> clazz) {
      return (T) mods.get(clazz);
   }

   public Module getModuleFromName(String mod) {
      for (Module module : mods.values()) {
         if (module.getModName().equalsIgnoreCase(mod)) {
            return module;
         }
      }

      return null;
   }

   public Collection<Module> getMods() {
      return mods.values();
   }

   public List<Module> getModsByCategory(Category type) {
      List<Module> list = new ArrayList<>();

      for (Module module : getMods()) {
         if (module.getModCategory().equals(type)) {
            list.add(module);
         }
      }

      list.sort(Comparator.comparing(Module::getModNameCaseIgnored));
      return list;
   }
}
