package me.eclipcen.butterflyclient.event.render;

import net.futureclient.eventbus.Event;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class RenderItemEvent extends Event {
   public ItemRenderer itemRenderer;
   public AbstractClientPlayer player;
   public float partialTicks;
   public float pitch;
   public EnumHand hand;
   public float swingProgress;
   public ItemStack stack;
   public float equipProgress;

   public RenderItemEvent(ItemRenderer itemRenderer, AbstractClientPlayer player, float partialTicks, float pitch, EnumHand hand, float swingProgress, ItemStack stack, float equipProgress) {
      this.itemRenderer = itemRenderer;
      this.player = player;
      this.partialTicks = partialTicks;
      this.pitch = pitch;
      this.hand = hand;
      this.swingProgress = swingProgress;
      this.stack = stack;
      this.equipProgress = equipProgress;
   }
}
