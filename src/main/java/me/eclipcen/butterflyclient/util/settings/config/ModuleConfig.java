package me.eclipcen.butterflyclient.util.settings.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.awt.Color;
import java.io.File;
import java.util.Iterator;
import java.util.Map.Entry;
import me.eclipcen.butterflyclient.module.api.Module;
import me.eclipcen.butterflyclient.module.api.branches.ToggleMod;
import me.eclipcen.butterflyclient.util.colors.Colors;
import me.eclipcen.butterflyclient.util.settings.BaseConfig;
import me.eclipcen.butterflyclient.util.settings.Setting;
import me.eclipcen.butterflyclient.util.settings.setting.ColorSetting;
import org.lwjgl.input.Keyboard;

public class ModuleConfig extends BaseConfig {
   private final Module module;

   public ModuleConfig(File dir, Module module) {
      super(new File(dir, module.getModName() + ".json"));
      this.module = module;
   }

   public void onLoad() {
      super.onLoad();
      this.getJsonObject().entrySet().forEach((entry) -> {
         if (((String)entry.getKey()).equals("Toggled")) {
            if (!this.module.isEnabled() && ((JsonElement)entry.getValue()).getAsBoolean()) {
               ((ToggleMod)this.module).toggle();
            }
         } else if (((String)entry.getKey()).equals("Keybind")) {
            this.module.setBind(((JsonElement)entry.getValue()).getAsString());
         }

         Iterator var2 = this.module.getSettingList().iterator();

         while(true) {
            while(true) {
               Setting setting;
               do {
                  if (!var2.hasNext()) {
                     return;
                  }

                  setting = (Setting)var2.next();
               } while(!setting.getName().equalsIgnoreCase((String)entry.getKey()));

               if (setting.getValue() instanceof Boolean) {
                  setting.setValue(((JsonElement)entry.getValue()).getAsBoolean());
               } else if (setting.getValue() instanceof Number && !(setting.getValue() instanceof Enum)) {
                  if (setting.getValue().getClass() == Float.class) {
                     setting.setValue(((JsonElement)entry.getValue()).getAsFloat());
                  } else if (setting.getValue().getClass() == Double.class) {
                     setting.setValue(((JsonElement)entry.getValue()).getAsDouble());
                  } else if (setting.getValue().getClass() == Integer.class) {
                     setting.setValue(((JsonElement)entry.getValue()).getAsInt());
                  } else if (setting.getValue().getClass() == Long.class) {
                     setting.setValue(((JsonElement)entry.getValue()).getAsLong());
                  }
               } else if (setting.getValue() instanceof Enum) {
                  setting.setEnumValue(((JsonElement)entry.getValue()).getAsString());
               } else if (setting.getValue() instanceof String) {
                  setting.setValue(((JsonElement)entry.getValue()).getAsString());
               } else if (setting instanceof ColorSetting) {
                  ColorSetting colorSetting = (ColorSetting)setting;
                  JsonObject jsonObject = ((JsonElement)entry.getValue()).getAsJsonObject();

                  for (Entry<String, JsonElement> stringJsonElementEntry : jsonObject.entrySet()) {
                     Entry<String, JsonElement> subEntry = (Entry) stringJsonElementEntry;
                     String var8 = (String) subEntry.getKey();
                     byte var9 = -1;
                     switch (var8.hashCode()) {
                        case -1656737386:
                           if (var8.equals("Rainbow")) {
                              var9 = 2;
                           }
                           break;
                           case 65290051:
                               if (var8.equals("Color")) {
                                   var9 = 0;
                               }
                               break;
                           case 2135814083:
                               if (var8.equals("Global")) {
                                   var9 = 1;
                               }
                       }

                       switch (var9) {
                           case 0:
                               int color = (int) Long.parseLong(((JsonElement) subEntry.getValue()).getAsString(), 16);
                               colorSetting.setValue(new Color(color, true));
                               break;
                           case 1:
                               colorSetting.setGlobalValue(((JsonElement) subEntry.getValue()).getAsBoolean());
                               break;
                           case 2:
                               colorSetting.setRainbowValue(((JsonElement) subEntry.getValue()).getAsBoolean());
                       }
                   }
               }
            }
         }
      });
   }

   public void onSave() {
      JsonObject moduleJsonObject = new JsonObject();
      moduleJsonObject.addProperty("Keybind", this.module.getBind() == 0 ? "NONE" : Keyboard.getKeyName(this.module.getBind()));
      if (this.module.getSettingList().size() != 0) {
         this.module.getSettingList().forEach((value) -> {
            if (value.getValue() instanceof Boolean) {
               moduleJsonObject.addProperty(value.getName(), (Boolean)value.getValue());
            } else if (value.getValue() instanceof Number && !(value.getValue() instanceof Enum)) {
               if (value.getValue().getClass() == Float.class) {
                  moduleJsonObject.addProperty(value.getName(), (Float)value.getValue());
               } else if (value.getValue().getClass() == Double.class) {
                  moduleJsonObject.addProperty(value.getName(), (Double)value.getValue());
               } else if (value.getValue().getClass() == Integer.class) {
                  moduleJsonObject.addProperty(value.getName(), (Integer)value.getValue());
               } else if (value.getValue().getClass() == Long.class) {
                  moduleJsonObject.addProperty(value.getName(), (Long)value.getValue());
               }
            } else if (value.getValue() instanceof Enum) {
               moduleJsonObject.addProperty(value.getName(), ((Enum)value.getValue()).name());
            } else if (value.getValue() instanceof String) {
               moduleJsonObject.addProperty(value.getName(), (String)value.getValue());
            } else if (value instanceof ColorSetting) {
               ColorSetting setting = (ColorSetting)value;
               JsonObject childObject = new JsonObject();
               childObject.addProperty("Color", Colors.getHexString(((Color)setting.value).getRGB()));
               if (setting.getShouldGlobal()) {
                  childObject.addProperty("Global", setting.getGlobalValue());
               }

               if (setting.getShouldRainbow()) {
                  childObject.addProperty("Rainbow", setting.getRainbowValue());
               }

               moduleJsonObject.add(value.getName(), childObject);
            }

         });
      }

      this.saveJsonObjectToFile(moduleJsonObject);
   }
}
