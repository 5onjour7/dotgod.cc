package me.eclipcen.butterflyclient.module.api;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.event.client.ModuleEvent;
import me.eclipcen.butterflyclient.util.Globals;
import me.eclipcen.butterflyclient.util.settings.Setting;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public abstract class Module implements Globals {
   private final String modName;
   private final String modDescription;
   private final Category category;
   public Setting<Boolean> toggled;
   public Setting<Boolean> drawn;
   private int key;
   private Color randomColor;
   private final List<Setting<?>> settingList;
   protected final Minecraft mc;

   public Module(Category category, String name, String desc) {
      settingList = new ArrayList<>();
      mc = Minecraft.getMinecraft();
      modName = name;
      modDescription = desc;
      this.category = category;
   }

   public Module(Category category, String name, String desc, String defaultBind) {
      this(category, name, desc);
      key = Keyboard.getKeyIndex(defaultBind);
   }

   public final void start() {
      Butterfly.getInstance().getSubscribers().register(this);
      Butterfly.getEventBus().transmit(new ModuleEvent.Start(this));

      onEnable();
   }

   public final void stop() {
      Butterfly.getEventBus().transmit(new ModuleEvent.Stop(this));
      Butterfly.getInstance().getSubscribers().unregister(this);

      onDisable();
   }

   public final String getModName() {
      return modName;
   }

   public final String getModNameCaseIgnored() {
      return modName.toLowerCase();
   }

   public final String getModDescription() {
      return modDescription;
   }

   public Category getModCategory() {
      return category;
   }

   public String getHUDTag() {
      return "";
   }

   public boolean isDrawn() {
      return false;
   }

   protected void onEnable() {
   }

   protected void onDisable() {
   }

   public abstract boolean isEnabled();

   public int getBind() {
      return key;
   }

   public void setBind(String bind) {
      key = Keyboard.getKeyIndex(bind);
   }

   public List<Setting<?>> getSettingList() {
      return settingList;
   }

   public Setting<?> findSetting(String find) {
      Iterator<Setting<?>> var2 = settingList.iterator();

      Setting<?> setting;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         setting = var2.next();
      } while(!setting.getName().equalsIgnoreCase(find));

      return setting;
   }

   public Color getRandomColor() {
      return randomColor;
   }

   public void setRandomColor(Color color) {
      randomColor = color;
   }
}
