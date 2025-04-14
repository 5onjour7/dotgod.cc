package me.eclipcen.butterflyclient.util.friend;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;

public final class FriendManager {
   private final Map<String, Friend> friends = new HashMap<>();

   public boolean isFriend(String name) {
      return getFriendByName(name) != null;
   }

   public boolean isFriend(EntityPlayer player) {
      return isFriend(player.getName());
   }

   public void addFriend(String name) {
      friends.put(name.toLowerCase(Locale.ROOT), new Friend(name));
   }

   public void removeFriend(String name) {
      friends.remove(name.toLowerCase(Locale.ROOT));
   }

   public Friend getFriendByName(String name) {
      return friends.get(name.toLowerCase(Locale.ROOT));
   }

   public Collection<Friend> getFriends() {
      return friends.values();
   }
}
