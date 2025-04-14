package me.eclipcen.butterflyclient.util.settings;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public abstract class BaseConfig {
   private final File file;
   private JsonObject jsonObject;

   public BaseConfig(File file) {
      this.file = file;
   }

   public File getFile() {
      return file;
   }

   public void onLoad() {
      jsonObject = convertJsonObjectFromFile();
   }

   public void onSave() {
   }

   protected void saveJsonObjectToFile(JsonObject object) {
      if (file.exists()) {
         file.delete();
      }

      try {
         file.createNewFile();
         FileWriter writer = new FileWriter(file);
         Throwable var3 = null;

         try {
            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(object));
         } catch (Throwable var13) {
            var3 = var13;
            throw var13;
         } finally {
             if (var3 != null) {
                 try {
                   writer.close();
                 } catch (Throwable var12) {
                   var3.addSuppressed(var12);
                 }
             } else {
                 writer.close();
             }
         }
      } catch (IOException var15) {
         var15.printStackTrace();
      }
   }

   protected JsonObject convertJsonObjectFromFile() {
      if (!getFile().exists()) {
         return new JsonObject();
      } else {
         FileReader reader = null;
         if (getFile().exists()) {
            try {
               reader = new FileReader(getFile());
            } catch (FileNotFoundException var5) {
               var5.printStackTrace();
            }
         }

         if (reader == null) {
            return new JsonObject();
         } else {
            JsonElement element = new JsonParser().parse(reader);
            if (!element.isJsonObject()) {
               return new JsonObject();
            } else {
               try {
                  reader.close();
               } catch (IOException var4) {
                  var4.printStackTrace();
               }

               return element.getAsJsonObject();
            }
         }
      }
   }

   public JsonObject getJsonObject() {
      return jsonObject;
   }
}
