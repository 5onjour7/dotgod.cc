package me.eclipcen.butterflyclient.launch.mixin.client.multiplayer;

import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.event.block.RightClickBlockEvent;
import me.eclipcen.butterflyclient.wrapper.IPlayerControllerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public abstract class MixinPlayerControllerMP implements IPlayerControllerMP {
   @Accessor
   public abstract void setBlockHitDelay(int var1);

   @Accessor
   public abstract void setCurBlockDamageMP(float var1);

   @Accessor
   public abstract float getCurBlockDamageMP();

   @Accessor
   public abstract NetHandlerPlayClient getConnection();

   @Invoker("syncCurrentPlayItem")
   public abstract void doSyncCurrentPlayItem();

   @Inject(
      method = "processRightClickBlock",
      at = @At("HEAD"),
      cancellable = true
   )
   private void onProcessRightClickBlock(EntityPlayerSP player, WorldClient worldIn, BlockPos pos, EnumFacing direction, Vec3d vec, EnumHand hand, CallbackInfoReturnable<EnumActionResult> callbackInfoReturnable) {
      RightClickBlockEvent event = new RightClickBlockEvent(hand, pos, direction);
      Butterfly.getEventBus().transmit(event);

      if (event.isCanceled()) {
         callbackInfoReturnable.setReturnValue(EnumActionResult.FAIL);
      }

   }
}
