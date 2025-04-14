package me.eclipcen.butterflyclient.util.settings;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.module.api.branches.ToggleMod;
import me.eclipcen.butterflyclient.util.client.AsyncUtil;
import me.eclipcen.butterflyclient.util.settings.config.FriendConfig;
import me.eclipcen.butterflyclient.util.settings.config.ModuleConfig;

public final class SettingManager {
   private File configDir;
   private File moduleConfigDir;
   private boolean firstLaunch;
   private final List<BaseConfig> configurations = new ArrayList<>();
   public static final String CONFIG_PATH = "butterfly/config/";

   public SettingManager() {
      generateDirectories();
   }

   private void generateDirectories() {
      configDir = new File("butterfly/config/");
      if (!configDir.exists()) {
         firstLaunch = true;
         configDir.mkdirs();
      }

      moduleConfigDir = new File("butterfly/config/Modules/");
      if (!moduleConfigDir.exists()) {
         moduleConfigDir.mkdirs();
      }

   }

   public void init() {
      Butterfly.getInstance()
              .getModuleManager()
              .getMods()
              .stream()
              .filter(module -> module instanceof ToggleMod)
              .forEach(module -> configurations.add(new ModuleConfig(moduleConfigDir, module)));
      configurations.add(new FriendConfig(configDir));
      if (firstLaunch) {
         saveAll();
      } else {
         loadAll();
      }
   }

   public void saveAll() {
      for (BaseConfig cfg : configurations) {
         cfg.onSave();
      }
   }

   public void saveAllAsync() {
      AsyncUtil.execute(() -> {
         for (BaseConfig cfg : configurations) {
            cfg.onSave();
         }
      });
   }

   public void loadAll() {
      for (BaseConfig cfg : configurations) {
         cfg.onLoad();
      }
   }
}
