package me.eclipcen.butterflyclient.util.dataloader;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import me.eclipcen.butterflyclient.util.image.Texture;

public class MainMenu implements AutoCloseable {
   public String[] splashes;
   public Texture banner;

   public void setup(String[] splashes, Texture banner) {
      if (this.banner != null) {
         this.close();
      }
      this.splashes = Objects.requireNonNull(splashes, "splashes");
      this.banner = Objects.requireNonNull(banner, "banner");
   }

   public String getRandomSplash() {
      Random r = ThreadLocalRandom.current();
      String[] colors = {
              "\u00A71",
              "\u00A79",
              "\u00A7a",
              "\u00A7b",
              "\u00A7c",
              "\u00A7f",
      };
      return colors[r.nextInt(colors.length)] + this.splashes[r.nextInt(this.splashes.length)];
   }

   @Override
   public void close() {
      this.banner.close();
      this.splashes = null;
   }
}
