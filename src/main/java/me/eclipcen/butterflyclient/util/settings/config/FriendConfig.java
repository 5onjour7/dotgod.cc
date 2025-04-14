package me.eclipcen.butterflyclient.util.settings.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.File;
import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.util.settings.BaseConfig;

public class FriendConfig extends BaseConfig {
   public FriendConfig(File dir) {
      super(new File(dir, "Friends.json"));
   }

   @Override
   public void onLoad() {
      super.onLoad();

      getJsonObject().entrySet().forEach(entry -> {
         String name = entry.getKey();
         Butterfly.getInstance().getFriendManager().addFriend(name);
      });
   }

   @Override
   public void onSave() {
      JsonObject friendsListJsonObject = new JsonObject();

      Butterfly.getInstance().getFriendManager().getFriends().forEach(friend -> {
         JsonArray array = new JsonArray();
         friendsListJsonObject.add(friend.getName(), array);
      });

      saveJsonObjectToFile(friendsListJsonObject);
   }
}
