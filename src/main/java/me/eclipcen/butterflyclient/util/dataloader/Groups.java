package me.eclipcen.butterflyclient.util.dataloader;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public class Groups implements AutoCloseable {
   protected Map<UUID, Group> playerToGroup = Collections.emptyMap();
   protected Collection<Group> groups = Collections.emptyList();

   public void addGroup(Group group) {
      if (this.groups.isEmpty()) {
         this.groups = new HashSet<>();
      }
      this.groups.add(group);
      group.members.forEach(uuid -> this.addPlayerMapping(uuid, group));
   }

   public void addPlayerMapping(UUID uuid, Group group) {
      if (this.playerToGroup.isEmpty()) {
         this.playerToGroup = new HashMap<>();
      }
      this.playerToGroup.put(uuid, group);
   }

   @Override
   public void close() {
      this.groups.forEach(Group::close);

      this.playerToGroup.clear();
      this.groups.clear();
   }
}