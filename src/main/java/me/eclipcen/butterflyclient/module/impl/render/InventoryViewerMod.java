package me.eclipcen.butterflyclient.module.impl.render;

import me.eclipcen.butterflyclient.event.render.RenderEvent;
import me.eclipcen.butterflyclient.module.api.Category;
import me.eclipcen.butterflyclient.module.api.branches.ToggleMod;
import me.eclipcen.butterflyclient.util.settings.Setting;
import net.futureclient.eventbus.SubscribeEvent;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class InventoryViewerMod extends ToggleMod {
   private final Setting<Integer> yAxis = new Setting<>("yAxis", "aseuwifhseosiuw", 2, 1, 1080, 1);
   private final Setting<Integer> xAxis = new Setting<>("xAxis", "How far alone the xseodl", 2, 1, 1920, 1);

   public InventoryViewerMod() {
      super(Category.RENDER, "InventoryViewer", "Shows your inventory on your screen no clue why you want that but whatver kek Im not a fan xded");
   }

   @SubscribeEvent
   public void onRender(RenderEvent.Render2DEvent event) {
      GlStateManager.pushMatrix();
      GlStateManager.depthMask(true);
      GlStateManager.clear(256);
      GlStateManager.scale(1.0F, 1.0F, 0.01F);
      GlStateManager.enableDepth();

      RenderHelper.enableStandardItemLighting();
      NonNullList<ItemStack> items = mc.player.inventory.mainInventory;
      int size = items.size();

      for (int item = 9; item < size; item++) {
         int slotX = xAxis.getValue() + 1 + item % 9 * 18;
         int slotY = yAxis.getValue() + 1 + (item / 9 - 1) * 18;

         mc.getRenderItem().renderItemAndEffectIntoGUI(items.get(item), slotX, slotY);
         mc.getRenderItem().renderItemOverlays(mc.fontRenderer, items.get(item), slotX, slotY);
      }

      RenderHelper.disableStandardItemLighting();
      GlStateManager.disableDepth();
      GlStateManager.popMatrix();
   }
}
