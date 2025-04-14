package me.eclipcen.butterflyclient.event.render;

import net.futureclient.eventbus.Event;

public class FOVEvent extends Event {
   private float fov;

   public FOVEvent(float fov) {
      this.fov = fov;
   }

   public float getFov() {
      return fov;
   }

   public void setFov(float fov) {
      this.fov = fov;
   }

   public static class Modify extends FOVEvent {
      public Modify(float fov) {
         super(fov);
      }
   }

   public static class Update extends FOVEvent {
      public Update(float fov) {
         super(fov);
      }
   }
}
