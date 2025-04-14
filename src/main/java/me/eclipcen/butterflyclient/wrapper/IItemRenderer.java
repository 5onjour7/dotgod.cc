package me.eclipcen.butterflyclient.wrapper;

import net.minecraft.item.ItemStack;

public interface IItemRenderer {
   void setEquippedProgressMainHand(float var1);

   void setEquippedProgressOffHand(float var1);

   void setItemStackMainHand(ItemStack var1);

   void setItemStackOffHand(ItemStack var1);
}
