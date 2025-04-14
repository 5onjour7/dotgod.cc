package me.eclipcen.butterflyclient.launch.mixin.client.entity;

import com.mojang.authlib.GameProfile;
import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.event.EventType;
import me.eclipcen.butterflyclient.event.client.SendChatMessageEvent;
import me.eclipcen.butterflyclient.event.player.PlayerLivingUpdateEvent;
import me.eclipcen.butterflyclient.event.player.PlayerMoveEvent;
import me.eclipcen.butterflyclient.event.player.PlayerPushOutOfBlocksEvent;
import me.eclipcen.butterflyclient.event.player.PlayerSprintEvent;
import me.eclipcen.butterflyclient.event.player.PlayerUpdateEvent;
import me.eclipcen.butterflyclient.event.player.PlayerUpdateWalkingEvent;
import me.eclipcen.butterflyclient.wrapper.IEntityPlayerSP;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.MoverType;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer implements IEntityPlayerSP {
   @Shadow
   @Final
   public NetHandlerPlayClient connection;
   @Unique
   private PlayerUpdateWalkingEvent event;
   @Unique
   private PlayerMoveEvent moveEvent;

   public MixinEntityPlayerSP(World worldIn, GameProfile playerProfile) {
      super(worldIn, playerProfile);
   }

   @Accessor
   public abstract boolean isPrevOnGround();

   @Accessor
   public abstract double getLastReportedPosX();

   @Accessor
   public abstract double getLastReportedPosY();

   @Accessor
   public abstract double getLastReportedPosZ();

   @Inject(
      method = "onUpdate",
      at = @At("HEAD")
   )
   public void preOnUpdate(CallbackInfo callbackInfo) {
      Butterfly.getEventBus().transmit(new PlayerUpdateEvent(EventType.Type.PRE));
   }

   @Inject(
      method = "onUpdate",
      at = @At("RETURN")
   )
   public void postOnUpdate(CallbackInfo callbackInfo) {
      Butterfly.getEventBus().transmit(new PlayerUpdateEvent(EventType.Type.POST));
   }

   @Inject(
      method = "onUpdateWalkingPlayer",
      at = @At("HEAD"),
      cancellable = true
   )
   public void preUpdateWalkingPlayer(CallbackInfo callbackInfo) {
      event = new PlayerUpdateWalkingEvent(EventType.Type.PRE, posX, getEntityBoundingBox().minY, posZ, isSprinting(), onGround, isSneaking(), rotationYaw, rotationPitch);
      Butterfly.getEventBus().transmit(event);

      rotationYaw = event.getRotationYaw();
      rotationPitch = event.getRotationPitch();
      
      posX = event.getPositionX();
      posY = event.getPositionY();
      posZ = event.getPositionZ();
      
      int slot = event.getSlot();
      if (slot != -1 && inventory.currentItem != slot) {
         connection.sendPacket(new CPacketHeldItemChange(slot));
      }

      if (event.isCanceled()) {
         callbackInfo.cancel();
      }

   }

   @Inject(
      method = "onUpdateWalkingPlayer",
      at = @At("RETURN")
   )
   public void postUpdateWalkingPlayer(CallbackInfo callbackInfo) {
      Butterfly.getEventBus().transmit(new PlayerUpdateWalkingEvent(EventType.Type.POST, rotationYaw, rotationPitch));

      rotationYaw = event.getOldRotationYaw();
      rotationPitch = event.getOldRotationPitch();

      if (event.getSlot() != -1) {
         connection.sendPacket(new CPacketHeldItemChange(inventory.currentItem));
      }

   }

   @Inject(
           method = "move",
           at = @At("HEAD")
   )
   private void move(MoverType type, double x, double y, double z, CallbackInfo callbackInfo) {
      Butterfly.getEventBus().transmit(moveEvent = new PlayerMoveEvent(type, x, y, z));
   }

   @ModifyVariable(
           method = "move",
           at = @At("HEAD"),
           ordinal = 0
   )
   private double setMoveX(double x) {
      return moveEvent.getX();
   }

   @ModifyVariable(
           method = "move",
           at = @At("HEAD"),
           ordinal = 1
   )
   private double setMoveY(double y) {
      return moveEvent.getY();
   }

   @ModifyVariable(
           method = "move",
           at = @At("HEAD"),
           ordinal = 2
   )
   private double setMoveZ(double z) {
      return moveEvent.getZ();
   }

   @Inject(
      method = "sendChatMessage",
      at = @At("HEAD"),
      cancellable = true
   )
   private void sendChatMessage(String message, CallbackInfo callbackInfo) {
      SendChatMessageEvent event = new SendChatMessageEvent(message);
      Butterfly.getEventBus().transmit(event);

      if (event.isCanceled()) {
         callbackInfo.cancel();
      }

   }

   @ModifyVariable(
      method = "setSprinting",
      at = @At("HEAD"),
      argsOnly = true,
      ordinal = 0
   )
   private boolean forceSprinting(boolean sprinting) {
      PlayerSprintEvent event = new PlayerSprintEvent(sprinting);
      Butterfly.getEventBus().transmit(event);

      return event.isSprinting();
   }

   @Inject(
      method = "pushOutOfBlocks",
      at = @At("HEAD"),
      cancellable = true
   )
   public void onPushOutOfBlocks(double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
      PlayerPushOutOfBlocksEvent event = new PlayerPushOutOfBlocksEvent();
      Butterfly.getEventBus().transmit(event);

      if (event.isCanceled()) {
         cir.cancel();
      }

   }

   @Inject(
      method = "onLivingUpdate",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/util/MovementInput;updatePlayerMoveState()V",
         shift = At.Shift.AFTER
      )
   )
   public void onLivingUpdate(CallbackInfo callbackInfo) {
      Butterfly.getEventBus().transmit(new PlayerLivingUpdateEvent());
   }
}
