package me.eclipcen.butterflyclient.event.block;

import net.futureclient.eventbus.EventCancelable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class RightClickBlockEvent extends EventCancelable {
   private final EnumHand hand;
   private final BlockPos pos;
   private final EnumFacing face;

   public RightClickBlockEvent(EnumHand hand, BlockPos pos, EnumFacing face) {
      this.hand = hand;
      this.pos = pos;
      this.face = face;
   }

   public BlockPos getPos() {
      return pos;
   }

   public EnumHand getHand() {
      return hand;
   }

   public EnumFacing getFace() {
      return face;
   }
}
