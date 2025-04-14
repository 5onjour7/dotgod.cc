package me.eclipcen.butterflyclient;

import java.awt.Font;
import java.io.File;
import me.eclipcen.butterflyclient.clickgui.ClickGUI;
import me.eclipcen.butterflyclient.command.api.CommandManager;
import me.eclipcen.butterflyclient.module.api.ModuleManager;
import me.eclipcen.butterflyclient.util.Globals;
import me.eclipcen.butterflyclient.util.client.AsyncUtil;
import me.eclipcen.butterflyclient.util.dataloader.DataLoader;
import me.eclipcen.butterflyclient.util.font.CFontRenderer;
import me.eclipcen.butterflyclient.util.friend.FriendManager;
import me.eclipcen.butterflyclient.util.settings.SettingManager;
import net.futureclient.eventbus.EventBus;
import net.futureclient.eventbus.EventSubscribers;

public class Butterfly implements Globals {
   public DataLoader data;

   private CFontRenderer fontRenderer;
   private SettingManager settingManager;
   private FriendManager friendManager;
   private CommandManager commandManager;
   private ModuleManager moduleManager;

   private static final Butterfly INSTANCE = new Butterfly();
   private static final EventBus EVENT_BUS = new EventBus();
   private static final EventSubscribers SUBSCRIBERS;
   private static final String ALT_KEY = "butterfly.development";
   private static final boolean USE_ALT = Boolean.parseBoolean(System.getProperty("butterfly.development", System.getenv("butterfly.development")));

   public void start() {
      long start = System.currentTimeMillis();
      LOGGER.info("Initialising butterfly...");

      data = new DataLoader("https://dabigbulletz638.github.io/resources/resources.json", new File(MC.gameDir, "butterfly/resources/"));

      settingManager = new SettingManager();
      friendManager = new FriendManager();
      commandManager = new CommandManager();
      moduleManager = new ModuleManager();
      commandManager.registerCommands();
      LOGGER.info("Commands registered!");

      moduleManager.registerMods();
      LOGGER.info("Modules registered!");

      new ClickGUI();

      fontRenderer = new CFontRenderer(new Font("Verdana", Font.PLAIN, 18), true, true);
      settingManager.init();

      AsyncUtil.execute(() -> data.load());
      LOGGER.info("Resources loaded!");

      long finish = System.currentTimeMillis() - start;
      LOGGER.info("Butterfly initialisation finished in {}ms!", finish);
   }

   public static Butterfly getInstance() {
      return INSTANCE;
   }

   public static EventBus getEventBus() {
      return EVENT_BUS;
   }

   public EventSubscribers getSubscribers() {
      return SUBSCRIBERS;
   }

   public CFontRenderer getFontRenderer() {
      return fontRenderer;
   }

   public ModuleManager getModuleManager() {
      return moduleManager;
   }

   public CommandManager getCommandManager() {
      return commandManager;
   }

   public SettingManager getSettingManager() {
      return settingManager;
   }

   public FriendManager getFriendManager() {
      return friendManager;
   }

   static {
       SUBSCRIBERS = EVENT_BUS.attach(new EventSubscribers());
   }
}
