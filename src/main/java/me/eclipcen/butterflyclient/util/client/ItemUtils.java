package me.eclipcen.butterflyclient.util.client;

import me.eclipcen.butterflyclient.util.Globals;
import net.minecraft.block.Block;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class ItemUtils implements Globals {
   public static ItemStack getMainhand() {
      return MC.player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
   }

   public static ItemStack getOffhand() {
      return MC.player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
   }

   public static void replaceOffhand(int slot) {
      if (MC.player.openContainer instanceof ContainerPlayer && slot >= 9) {
         MC.playerController.windowClick(MC.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, MC.player);
         MC.playerController.windowClick(MC.player.inventoryContainer.windowId, 45, 0, ClickType.PICKUP, MC.player);
         MC.playerController.windowClick(MC.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, MC.player);
      } else if (slot != -1) {
         MC.playerController.windowClick(MC.player.inventoryContainer.windowId, 45, slot, ClickType.SWAP, MC.player);
      }

   }

   public static void attemptForceUpdate() {
      short actionId = -1;
   }

   public static int findItem(Item item, boolean holding) {
      int itemSlot = -1;
      if (MC.player.inventory.getItemStack().getItem() == item && holding) {
         return -2;
      } else {
         for(int i = 0; i <= 36; ++i) {
            if (MC.player.inventory.getStackInSlot(i).getItem().equals(item)) {
               itemSlot = i;
               break;
            }
         }

         return itemSlot;
      }
   }

   public static int findBlock(Block type) {
      return findItemHotbar(Item.getItemFromBlock(type));
   }

   public static int findItemHotbar(Item item) {
      int itemSlot = -1;

      for(int i = 0; i < 9; ++i) {
         if (MC.player.inventory.getStackInSlot(i).getItem() == item) {
            itemSlot = i;
            break;
         }
      }

      return itemSlot;
   }

   public static int getBestTool(Block block) {
      float best = -1.0F;
      int index = -1;

      for (int i = 0; i < 9; ++i) {
         ItemStack itemStack = MC.player.inventory.getStackInSlot(i);
         float str = itemStack.getItem().getDestroySpeed(itemStack, block.getDefaultState());
         if (str > best) {
            best = str;
            index = i;
         }
      }

      return index;
   }

   public static int getColourFromDurability(ItemStack stack) {
      double durability = (double) stack.getItemDamage() / (double) stack.getMaxDamage();
      return MathHelper.hsvToRGB(Math.max(0.0F, (float) (1.0D - durability)) / 3.0F, 1.0F, 1.0F);
   }
}
