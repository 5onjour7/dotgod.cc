package me.eclipcen.butterflyclient.event.world;

import net.futureclient.eventbus.Event;
import net.minecraft.world.World;

public class WorldEvent extends Event {
   private final World world;

   public WorldEvent(World world) {
      this.world = world;
   }

   public World getWorld() {
      return world;
   }

   public static class Unload extends WorldEvent {
      public Unload(World world) {
         super(world);
      }
   }

   public static class Load extends WorldEvent {
      public Load(World world) {
         super(world);
      }
   }
}
