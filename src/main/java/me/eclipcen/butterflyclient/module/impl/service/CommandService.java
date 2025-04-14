package me.eclipcen.butterflyclient.module.impl.service;

import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.event.client.ProcessBindEvent;
import me.eclipcen.butterflyclient.event.client.SendChatMessageEvent;
import me.eclipcen.butterflyclient.module.api.branches.ServiceMod;
import me.eclipcen.butterflyclient.util.settings.Setting;
import net.futureclient.eventbus.SubscribeEvent;
import net.minecraft.client.gui.GuiChat;
import org.lwjgl.input.Keyboard;

public class CommandService extends ServiceMod {
   public static Setting<String> prefix = new Setting<>("Prefix", "The command prefix.", ".");

   public CommandService() {
      super("Commands");
   }

   @SubscribeEvent
   public void onChatMessageSent(SendChatMessageEvent event) {
      if (event.getMessage().startsWith(prefix.getValue())) {
         Butterfly.getInstance().getCommandManager().runCommand(event.getMessage());
         event.setCanceled(true);
      }

   }

   @SubscribeEvent
   public void onBindProcess(ProcessBindEvent event) {
      String keyboardChar = Character.toString(Keyboard.getEventCharacter());

      if (mc.currentScreen == null && keyboardChar.equals(prefix.getValue())) {
         mc.displayGuiScreen(new GuiChat(prefix.getValue()));
      }

   }
}
