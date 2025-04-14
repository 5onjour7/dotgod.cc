package me.eclipcen.butterflyclient.command.impl;

import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.command.api.Command;

public class FriendCommand extends Command {
   public FriendCommand() {
      super("friend", 2, 2);
   }

   public void execute(String[] args) {
      String name = args[1];
      if (Butterfly.getInstance().getFriendManager().isFriend(name)) {
         printChatMessage("Removed \u00A7c" + name + "\u00A7d as a friend");
         Butterfly.getInstance().getFriendManager().removeFriend(name);
      } else {
         printChatMessage("Added \u00A79" + name + "\u00A7d as a friend");
         Butterfly.getInstance().getFriendManager().addFriend(name);
      }

      Butterfly.getInstance().getSettingManager().saveAllAsync();
   }
}
