package me.eclipcen.butterflyclient.module.impl.render;

import me.eclipcen.butterflyclient.event.EventType;
import me.eclipcen.butterflyclient.event.player.PlayerUpdateEvent;
import me.eclipcen.butterflyclient.event.render.RenderItemTranslateEvent;
import me.eclipcen.butterflyclient.event.render.RotateArmEvent;
import me.eclipcen.butterflyclient.module.api.Category;
import me.eclipcen.butterflyclient.module.api.branches.ToggleMod;
import me.eclipcen.butterflyclient.util.settings.Setting;
import me.eclipcen.butterflyclient.wrapper.IItemRenderer;
import net.futureclient.eventbus.SubscribeEvent;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;

public class ViewModelChangerMod extends ToggleMod {
   private final Setting<Float> offZ = new Setting<>("OffZ", "offhand z-axis translation", 0.0F, -2.0F, 3.0F, 0.1F);
   private final Setting<Float> offY = new Setting<>("OffY", "offhand y-axis translation", 0.0F, -2.0F, 3.0F, 0.1F);
   private final Setting<Float> offX = new Setting<>("OffX", "offhand x-axis translation", 0.0F, -2.0F, 3.0F, 0.1F);
   private final Setting<Float> mainZ = new Setting<>("MainZ", "Mainhand z-axis translation", 0.0F, -2.0F, 3.0F, 0.1F);
   private final Setting<Float> mainY = new Setting<>("MainY", "Mainhand y-axis translation", 0.0F, -2.0F, 3.0F, 0.1F);
   private final Setting<Float> mainX = new Setting<>("MainX", "Mainhand x-axis translation", 0.0F, -2.0F, 3.0F, 0.1F);
   private final Setting<Boolean> handRotate = new Setting<>("HandRotate", "Disables your hands from rotating", false);
   private final Setting<Boolean> equipProgress = new Setting<>("EquipProgress", "Removes equipping animation", true);
   private final Setting<Boolean> offHand = new Setting<>("Offhand", "Does offhand changes", true);
   private final Setting<Boolean> mainHand = new Setting<>("Mainhand", "Does main hand changes", true);

   public ViewModelChangerMod() {
      super(Category.RENDER, "ViewModelChanger", "view model hax");
   }

   @SubscribeEvent
   public void renderItems(PlayerUpdateEvent event) {
      if (event.getType() == EventType.Type.PRE) {
         if (mainHand.getValue() && equipProgress.getValue() && (mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe || mc.player.getHeldItemMainhand().getItem() instanceof ItemAppleGold) && !mc.gameSettings.keyBindAttack.isKeyDown()) {
            ((IItemRenderer) mc.entityRenderer.itemRenderer).setEquippedProgressMainHand(1.0F);
            ((IItemRenderer) mc.entityRenderer.itemRenderer).setItemStackMainHand(mc.player.getHeldItem(EnumHand.MAIN_HAND));
         }

         if (offHand.getValue() && equipProgress.getValue()) {
            ((IItemRenderer) mc.entityRenderer.itemRenderer).setEquippedProgressOffHand(1.0F);
            ((IItemRenderer) mc.entityRenderer.itemRenderer).setItemStackOffHand(mc.player.getHeldItem(EnumHand.OFF_HAND));
         }

      }
   }

   @SubscribeEvent
   public void translateItems(RenderItemTranslateEvent event) {
      if (event.getHandSide() == EnumHandSide.RIGHT && mainHand.getValue()) {
         GlStateManager.translate(mainX.getValue(), mainY.getValue(), mainZ.getValue());
      } else if (event.getHandSide() == EnumHandSide.LEFT && offHand.getValue()) {
         GlStateManager.translate(offX.getValue(), offY.getValue(), offZ.getValue());
      }

   }

   @SubscribeEvent
   public void onRotateArm(RotateArmEvent event) {
      if (handRotate.getValue()) {
         event.setCanceled(true);
      }

   }
}
