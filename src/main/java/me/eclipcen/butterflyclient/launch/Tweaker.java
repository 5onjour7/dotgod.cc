package me.eclipcen.butterflyclient.launch;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

public class Tweaker implements ITweaker {
   public static boolean OBF_ENVIRONMENT = true;
   private final List<String> args = new ArrayList<>();

   @Override
   public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
      this.args.addAll(args);

      if (gameDir != null) {
         this.args.add("--gameDir");
         this.args.add(gameDir.getAbsolutePath());
      }

      if (assetsDir != null) {
         this.args.add("--assetsDir");
         this.args.add(assetsDir.getAbsolutePath());
      }

      if (profile != null) {
         this.args.add("--version");
         this.args.add(profile);
      }
   }

   public void injectIntoClassLoader(LaunchClassLoader classLoader) {
      MixinBootstrap.init();

      List<String> tweakClasses = (List<String>) Launch.blackboard.get("TweakClasses");
      String obfuscation = "notch";

      if (tweakClasses.stream().anyMatch((s) -> s.contains("net.minecraftforge.fml.common.launcher"))) {
         obfuscation = "searge";
         OBF_ENVIRONMENT = false;
      }

      MixinEnvironment.getDefaultEnvironment().setSide(MixinEnvironment.Side.CLIENT);
      MixinEnvironment.getDefaultEnvironment().setObfuscationContext(obfuscation);

      Mixins.addConfiguration("mixins.butterflyclient.json");
      Mixins.addConfiguration("mixins.future.interception.json");
   }

   public String getLaunchTarget() {
      return "net.minecraft.client.main.Main";
   }

   @Override
   public String[] getLaunchArguments() {
      return ((List<?>) Launch.blackboard.get("ArgumentList")).isEmpty() ? args.toArray(new String[0]) : new String[0];
   }
}
