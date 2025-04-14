package me.eclipcen.butterflyclient.event.player;

import me.eclipcen.butterflyclient.event.EventType;

public class PlayerUpdateWalkingEvent extends EventType {
   private double positionX;
   private double oldPositionY;
   private double positionY;
   private double positionZ;
   private boolean sprinting;
   private boolean onGround;
   private boolean sneaking;
   private boolean lockview = false;
   private float oldRotationYaw;
   private float rotationYaw;
   private float oldRotationPitch;
   private float rotationPitch;
   private int slot = -1;

   public PlayerUpdateWalkingEvent(EventType.Type type, double positionX, double positionY, double positionZ, boolean sprinting, boolean onGround, boolean sneaking, float rotationYaw, float rotationPitch) {
      super(type);
      this.positionX = positionX;
      this.oldPositionY = positionY;
      this.positionY = positionY;
      this.positionZ = positionZ;
      this.sprinting = sprinting;
      this.onGround = onGround;
      this.sneaking = sneaking;
      this.oldRotationYaw = rotationYaw;
      this.rotationYaw = rotationYaw;
      this.oldRotationPitch = rotationPitch;
      this.rotationPitch = rotationPitch;
   }

   public PlayerUpdateWalkingEvent(EventType.Type type, float rotationYaw, float rotationPitch) {
      super(type);
      this.rotationYaw = rotationYaw;
      this.rotationPitch = rotationPitch;
   }

   public double getPositionX() {
      return positionX;
   }

   public void setPositionX(double positionX) {
      this.positionX = positionX;
   }

   public double getPositionY() {
      return positionY;
   }

   public double getOldPositionY() {
      return oldPositionY;
   }

   public void setPositionY(double positionY) {
      this.positionY = positionY;
   }

   public double getPositionZ() {
      return positionZ;
   }

   public void setPositionZ(double positionZ) {
      this.positionZ = positionZ;
   }

   public boolean isSprinting() {
      return sprinting;
   }

   public void setSprinting(boolean sprinting) {
      this.sprinting = sprinting;
   }

   public boolean isOnGround() {
      return onGround;
   }

   public void setOnGround(boolean onGround) {
      this.onGround = onGround;
   }

   public boolean isSneaking() {
      return sneaking;
   }

   public void setSneaking(boolean sneaking) {
      this.sneaking = sneaking;
   }

   public float getRotationYaw() {
      return rotationYaw;
   }

   public void setRotationYaw(float rotationYaw) {
      this.rotationYaw = rotationYaw;
   }

   public float getOldRotationPitch() {
      return oldRotationPitch;
   }

   public float getRotationPitch() {
      return rotationPitch;
   }

   public float getOldRotationYaw() {
      return oldRotationYaw;
   }

   public void setRotationPitch(float rotationPitch) {
      this.rotationPitch = rotationPitch;
   }

   public int getSlot() {
      return slot;
   }

   public void setSlot(int slot) {
      this.slot = slot;
   }

   public boolean isLockview() {
      return lockview;
   }

   public void setLockview(boolean lockview) {
      this.lockview = lockview;
   }
}
