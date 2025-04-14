package me.eclipcen.butterflyclient.util.dataloader;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.swing.JOptionPane;
import me.eclipcen.butterflyclient.util.image.Texture;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;

public class DataLoader {
   protected final String resourcesUrl;
   protected final File cache;
   protected final JsonObject root;
   protected Function<String, InputStream> readerFunction;

   public Groups groups = new Groups();
   public MainMenu mainMenu = new MainMenu();

   public DataLoader(String resourcesUrl, File cache) {
      this.resourcesUrl = Objects.requireNonNull(resourcesUrl, "resourcesUrl");
      this.cache = cache;

      if (!cache.exists() && !cache.mkdirs()) {
         throw new IllegalStateException(String.format("Unable to create directory: %s", cache.getAbsolutePath()));
      }

      this.root = this.getResourcesRoot();
      {
         String baseurl = this.root.get("baseurl").getAsString();
         this.readerFunction = s -> {
            try {
               return new URL(String.format("%s%s", baseurl, s)).openStream();
            } catch (IOException e) {
               e.printStackTrace();
               return null;
            }
         };
      }
   }

   public void load() {
      JsonObject data = this.root.get("data").getAsJsonObject();
      if (data.has("groups")) {
         JsonObject json = this.readJson(data.get("groups").getAsString());

         Groups groups = new Groups();
         StreamSupport.stream(json.getAsJsonArray("groups").spliterator(), false)
                 .map(JsonElement::getAsString)
                 .map(this::readJson)
                 .forEach(object -> {
                    int color = 0x000000;
                    if (object.has("color")) {
                       JsonObject colorJson = object.getAsJsonObject("color");
                       color = (colorJson.get("r").getAsInt() << 16) |
                               (colorJson.get("g").getAsInt() << 8) |
                               (colorJson.get("b").getAsInt());
                    }
                    groups.addGroup(new Group(
                            object.get("id").getAsString(),
                            object.has("name") ? object.get("name").getAsString() : null,
                            StreamSupport.stream(object.getAsJsonArray("members").spliterator(), false)
                                    .map(JsonElement::getAsString)
                                    .map(UUID::fromString)
                                    .collect(Collectors.toSet()),
                            color,
                            object.has("cape") ? this.readTexture(object.get("cape").getAsString()) : null,
                            object.has("icon") ? this.readTexture(object.get("icon").getAsString()) : null
                    ));
                 });

         Groups oldGroups = this.groups;
         this.groups = groups;
         oldGroups.close();
      }

      if (data.has("mainmenu")) {
         JsonObject json = this.readJson(data.get("mainmenu").getAsString());
         this.mainMenu.setup(
                 StreamSupport.stream(json.getAsJsonArray("splashes").spliterator(), false)
                         .map(JsonElement::getAsString)
                         .toArray(String[]::new),
                 this.readTexture(json.get("banner").getAsString())
         );
      }
   }

   public Group getGroup(EntityPlayer entity)   {
      return this.getGroup(entity.getGameProfile());
   }

   public Group getGroup(NetworkPlayerInfo info)   {
      return this.getGroup(info.getGameProfile());
   }

   public Group getGroup(GameProfile profile)   {
      return this.getGroup(profile.getId());
   }

   public Group getGroup(UUID uuid)   {
      return this.groups.playerToGroup.get(uuid);
   }

   protected JsonObject getResourcesRoot() {
      return readJson(read("/resources.json", () -> {
         try {
            return new URL(resourcesUrl).openStream();
         } catch (IOException e) {
            e.printStackTrace();
            return null;
         }
      }, new File(cache, "resources.json")));
   }

   protected byte[] read(String path)  {
      return this.read(path, () -> this.readerFunction.apply(path), new File(this.cache, path.replace('/', File.separatorChar)));
   }

   protected byte[] read(String path, Supplier<InputStream> inSupplier, File cached)  {
      try {
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         try (InputStream in = inSupplier.get()) {
            int i;
            while ((i = in.read()) != -1) {
               baos.write(i);
            }
         } catch (IOException e) {
            if (cached.exists()) {
               //attempt to load from cache if error occurred while loading
               baos.reset();
               try (InputStream in = new BufferedInputStream(new FileInputStream(cached))) {
                  int i;
                  while ((i = in.read()) != -1)   {
                     baos.write(i);
                  }
               } catch (IOException e1) {
                  throw new RuntimeException(e1);
               }
            } else {
               throw new RuntimeException(e);
            }
         }
         byte[] b = baos.toByteArray();
         try {
            //write to cache
            if (!cached.exists()) {
               File parent = cached.getParentFile();
               if (!parent.exists() && !parent.mkdirs())   {
                  throw new IllegalStateException(String.format("Unable to create directory: %s", parent.getAbsolutePath()));
               } else if (!cached.createNewFile()) {
                  throw new IllegalStateException(String.format("Unable to create file: %s", cached.getAbsolutePath()));
               }
            }
            try (OutputStream out = new FileOutputStream(cached))   {
               out.write(b);
            }
         } catch (IOException e) {
            throw new RuntimeException(e);
         }
         return b;
      } catch (RuntimeException e)   {
         e.printStackTrace();
         JOptionPane.showMessageDialog(
                 null,
                 String.format("Unable to load resource: \"%s\"", path),
                 "butterfly load error",
                 JOptionPane.ERROR_MESSAGE
         );
         throw e;
      }
   }

   protected JsonObject readJson(byte[] in) {
      return  new JsonParser().parse(new InputStreamReader(new ByteArrayInputStream(in), StandardCharsets.UTF_8)).getAsJsonObject();
   }

   protected JsonObject readJson(String path) {
      return this.readJson(this.read(path));
   }

   protected Texture readTexture(String path) {
      try {
         return new Texture(this.read(path));
      } catch (IOException e) {
         throw new RuntimeException(String.format("Unable to parse texture: %s", path), e);
      }
   }
}