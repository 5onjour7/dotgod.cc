package me.eclipcen.butterflyclient.launch.mixin.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;

import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.event.client.FpsEvent;
import me.eclipcen.butterflyclient.event.client.GameLoopEvent;
import me.eclipcen.butterflyclient.event.client.KeyPressedEvent;
import me.eclipcen.butterflyclient.event.client.MouseEvent;
import me.eclipcen.butterflyclient.event.client.ProcessBindEvent;
import me.eclipcen.butterflyclient.event.client.TickEvent;
import me.eclipcen.butterflyclient.event.gui.GuiOpenEvent;
import me.eclipcen.butterflyclient.launch.Tweaker;
import me.eclipcen.butterflyclient.util.Globals;
import me.eclipcen.butterflyclient.wrapper.IMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;
import net.minecraft.util.Util;
import net.minecraft.util.Util.EnumOS;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft implements IMinecraft {
   @Shadow
   public EntityPlayerSP player;
   @Shadow
   @Final
   private static Logger LOGGER;

   @Shadow
   protected abstract ByteBuffer readImageToBuffer(InputStream var1) throws IOException;

   @Accessor
   public abstract void setRightClickDelayTimer(int var1);

   @Accessor
   public abstract Timer getTimer();

   @Accessor
   public abstract void setSession(Session var1);

   @Inject(
      method = "init",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/Minecraft;checkGLError(Ljava/lang/String;)V",
         ordinal = 2,
         shift = At.Shift.AFTER
      )
   )
   public void onInit(CallbackInfo callbackInfo) {
      Butterfly.getInstance().start();
   }

   @Inject(
      method = "shutdown",
      at = @At("HEAD")
   )
   public void onShutdown(CallbackInfo ci) {
      Globals.LOGGER.info("Saving config...");
      Butterfly.getInstance().getSettingManager().saveAll();
      Globals.LOGGER.info("Saved.");
   }

   @Inject(
      method = "runGameLoop",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/profiler/Profiler;endSection()V",
         ordinal = 0,
         shift = At.Shift.AFTER
      )
   )
   public void onRunGameLoop(CallbackInfo callbackInfo) {
      if (player != null) {
         GameLoopEvent event = new GameLoopEvent();
         Butterfly.getEventBus().transmit(event);
      }

   }

   @Inject(
      method = "runTick",
      at = @At("HEAD")
   )
   public void preOnRunTick(CallbackInfo callbackInfo) {
      Butterfly.getEventBus().transmit(new TickEvent.Pre());
   }

   @Inject(
      method = "runTick",
      at = @At("RETURN")
   )
   public void postOnRunTick(CallbackInfo callbackInfo) {
      Butterfly.getEventBus().transmit(new TickEvent.Post());
   }

   @Inject(
      method = "runTickMouse",
      at = @At(
         value = "INVOKE_ASSIGN",
         target = "Lorg/lwjgl/input/Mouse;getEventButton()I"
      )
   )
   public void onRunTickMouse(CallbackInfo callbackInfo) {
      if (Mouse.getEventButtonState()) {
         Butterfly.getEventBus().transmit(new MouseEvent(Mouse.getEventButton()));
      }

   }

   @Inject(
      method = "runTickKeyboard",
      at = @At(
         value = "FIELD",
         target = "Lnet/minecraft/client/Minecraft;currentScreen:Lnet/minecraft/client/gui/GuiScreen;",
         ordinal = 0
      ),
      locals = LocalCapture.CAPTURE_FAILHARD
   )
   private void onRunTickKeyboard(CallbackInfo ci, int i) {
      if (Keyboard.getEventKeyState()) {
         Butterfly.getEventBus().transmit(new KeyPressedEvent(i));
      }

   }

   @Redirect(
      method = "createDisplay",
      at = @At(
         value = "INVOKE",
         target = "Lorg/lwjgl/opengl/Display;setTitle(Ljava/lang/String;)V"
      )
   )
   public void changeWindowTitle(String title) {
      Display.setTitle("butterfly " + (Tweaker.OBF_ENVIRONMENT ? "vanilla" : "forge"));
   }

   @Inject(
      method = "processKeyBinds",
      at = @At("HEAD")
   )
   public void processKeyBinds(CallbackInfo ci) {
      ProcessBindEvent event = new ProcessBindEvent();
      Butterfly.getEventBus().transmit(event);
   }

   @Inject(
      method = "getLimitFramerate",
      at = @At("HEAD"),
      cancellable = true
   )
   public void getLimitFramerate(CallbackInfoReturnable<Integer> callbackInfoReturnable) {
      if (!Display.isActive()) {
         FpsEvent event = new FpsEvent();
         Butterfly.getEventBus().transmit(event);
         callbackInfoReturnable.setReturnValue(event.getFps());
      }

   }

   @Inject(
      method = "setWindowIcon",
      at = @At("HEAD"),
      cancellable = true
   )
   public void setWindowIcon(CallbackInfo callbackInfo) {
      if (Util.getOSType() == EnumOS.WINDOWS) {
         InputStream inputstream = null;
         FileInputStream inputstream1 = null;

         try {
            inputstream = Files.newInputStream(new File(Minecraft.getMinecraft().gameDir, File.separatorChar + "butterfly/resources/textures/butterfly_icon_16x16.png").toPath());
            inputstream1 = new FileInputStream(new File(Minecraft.getMinecraft().gameDir, File.separatorChar + "butterfly/resources/textures/butterfly_icon_32x32.png"));
            callbackInfo.cancel();
            Display.setIcon(new ByteBuffer[]{this.readImageToBuffer(inputstream), this.readImageToBuffer(inputstream1)});
         } catch (IOException var8) {
            LOGGER.error("Couldn't set icon", var8);
         } finally {
            IOUtils.closeQuietly(inputstream);
            IOUtils.closeQuietly(inputstream1);
         }
      }

   }

   @Inject(
      method = "displayGuiScreen",
      at = @At("HEAD")
   )
   public void onDisplayGuiScreen(GuiScreen guiScreen, CallbackInfo callbackInfo) {
      Butterfly.getEventBus().transmit(new GuiOpenEvent(guiScreen));
   }
}
