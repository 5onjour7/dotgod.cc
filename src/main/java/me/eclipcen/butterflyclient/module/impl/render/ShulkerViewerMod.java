package me.eclipcen.butterflyclient.module.impl.render;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.Nullable;
import me.eclipcen.butterflyclient.event.gui.GuiDrawEvent;
import me.eclipcen.butterflyclient.event.gui.GuiKeyboardEvent;
import me.eclipcen.butterflyclient.event.gui.GuiOpenEvent;
import me.eclipcen.butterflyclient.event.gui.RenderToolTipEvent;
import me.eclipcen.butterflyclient.module.api.Category;
import me.eclipcen.butterflyclient.module.api.branches.ToggleMod;
import me.eclipcen.butterflyclient.util.Globals;
import me.eclipcen.butterflyclient.util.colors.Colors;
import me.eclipcen.butterflyclient.util.font.FontUtils;
import me.eclipcen.butterflyclient.util.settings.Setting;
import me.eclipcen.butterflyclient.wrapper.IGuiContainer;
import net.futureclient.eventbus.SubscribeEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import org.lwjgl.input.Keyboard;

public class ShulkerViewerMod extends ToggleMod {
   private final Setting<Float> yOffset = new Setting<>("Y-Offset", "side offset", 0.0F, 0.0F, 100.0F, 1.0F);
   private final Setting<Float> xOffset = new Setting<>("X-Offset", "x offset", 8.0F, 1.0F, 100.0F, 1.0F);
   private static final Setting<Float> tooltipOpacity = new Setting<>("TooltipOpacity", "tooltip Opacity", 200.0F, 0.0F, 255.0F, 1.0F);
   private static final Setting<Float> lockedOpacity = new Setting<>("LockedOpacity", "Shifted Opacity", 255.0F, 0.0F, 255.0F, 1.0F);
   private static final ResourceLocation SHULKER_GUI_TEXTURE = new ResourceLocation("textures/gui/container/shulker_box.png");
   private static final int SHULKER_GUI_SIZE = 76;
   private static final int CACHE_HOVERING_INDEX = 0;
   private static final int CACHE_HOLDING_INDEX = 1;
   private static final int CACHE_RESERVE_SIZE = 2;
   private final List<ShulkerViewerMod.GuiShulkerViewer> guiCache = Lists.newArrayListWithExpectedSize(2);
   private final Lock cacheLock = new ReentrantLock();
   private static boolean locked = false;
   private static boolean updated = false;
   private boolean isKeySet = false;
   private static boolean isMouseInShulkerGui = false;
   private static boolean isModGeneratedToolTip = false;
   private int lastX = -1;
   private int lastY = -1;

   public ShulkerViewerMod() {
      super(Category.RENDER, "ShulkerViewer", "Lets you look into shulkers (credits to forgehax for source)");
   }

   private static boolean isLocked() {
      return locked && updated;
   }

   private boolean setInCache(int index, @Nullable ShulkerViewerMod.GuiShulkerViewer viewer) {
      if (index < 0) {
         return false;
      } else if (viewer == null && index > 1 && index == guiCache.size() - 1) {
         guiCache.remove(index);
         int previous = index - 1;
         return previous <= 1 || getInCache(previous).isPresent() || setInCache(previous, null);
      } else if (index <= guiCache.size() - 1) {
         guiCache.set(index, viewer);
         return true;
      } else {
         for (int i = Math.max(guiCache.size(), 1); i < index; i++) {
            guiCache.add(i, null);
         }

         guiCache.add(index, viewer);
         return true;
      }
   }

   private Optional<ShulkerViewerMod.GuiShulkerViewer> getInCache(int index) {
      return isInRange(guiCache, index) ? Optional.ofNullable(guiCache.get(index)) : Optional.empty();
   }

   private void clearCache() {
      for(int i = 0; i < 2; ++i) {
         setInCache(i, null);
      }

      while(guiCache.size() > 2) {
         setInCache(guiCache.size() - 1, null);
      }

   }

   private void reset() {
      locked = updated = isKeySet = isMouseInShulkerGui = isModGeneratedToolTip = false;
      lastX = lastY = -1;
      clearCache();
   }

   private ShulkerViewerMod.GuiShulkerViewer newShulkerGui(ItemStack parentShulker, int priority) {
      return new GuiShulkerViewer(new ShulkerContainer(new ShulkerInventory(getShulkerContents(parentShulker)), 27), parentShulker, priority);
   }

   protected void onEnable() {
      cacheLock.lock();

      try {
         reset();
      } finally {
         cacheLock.unlock();
      }

   }

   protected void onDisable() {
      onEnable();
   }

   public static void drawTexturedRect(int x, int y, int textureX, int textureY, int width, int height, int zLevel) {
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder builder = tessellator.getBuffer();
      builder.begin(7, DefaultVertexFormats.POSITION_TEX);
      builder.pos(x, y + height, zLevel).tex((float) textureX * 0.00390625F, (float)(textureY + height) * 0.00390625F).endVertex();
      builder.pos(x + width, y + height, zLevel).tex((float)(textureX + width) * 0.00390625F, (float) (textureY + height) * 0.00390625F).endVertex();
      builder.pos(x + width, y, zLevel).tex((float) (textureX + width) * 0.00390625F, (float) textureY * 0.00390625F).endVertex();
      builder.pos(x, y, zLevel).tex((float) textureX * 0.00390625F, (float) textureY * 0.00390625F).endVertex();
      tessellator.draw();
   }

   @SubscribeEvent
   public void onPreTooptipRender(RenderToolTipEvent event) {
      if (mc.currentScreen instanceof GuiContainer && !isModGeneratedToolTip) {
         if (isMouseInShulkerGui) {
            event.setCanceled(true);
         } else if (event.getStack().getItem() instanceof ItemShulkerBox) {
            event.setCanceled(true);
         }

      }
   }

   @SubscribeEvent
   public void onGuiChanged(GuiOpenEvent event) {
      if (event.getGui() == null) {
         reset();
      }

   }

   @SubscribeEvent
   public void onRender(GuiDrawEvent event) {
      if (mc.currentScreen instanceof GuiContainer) {
         cacheLock.lock();

         try {
            GuiContainer gui = (GuiContainer)mc.currentScreen;
            if (!isLocked()) {
               Slot slotUnder = ((IGuiContainer)gui).getHoveredSlot();
               if (slotUnder != null && slotUnder.getHasStack() && !slotUnder.getStack().isEmpty() && slotUnder.getStack().getItem() instanceof ItemShulkerBox) {
                  if (!ItemStack.areItemStacksEqual(getInCache(0).map(GuiShulkerViewer::getParentShulker).orElse(ItemStack.EMPTY), slotUnder.getStack())) {
                     setInCache(0, newShulkerGui(slotUnder.getStack(), 1));
                  }
               } else {
                  setInCache(0, null);
               }

               ItemStack stackHeld = getInventory().getItemStack();
               if (!stackHeld.isEmpty() && stackHeld.getItem() instanceof ItemShulkerBox) {
                  if (!ItemStack.areItemStacksEqual(getInCache(1).map(GuiShulkerViewer::getParentShulker).orElse(ItemStack.EMPTY), stackHeld)) {
                     setInCache(1, newShulkerGui(stackHeld, 0));
                  }
               } else {
                  setInCache(1, null);
               }

               if (locked && !updated && guiCache.stream().anyMatch(Objects::nonNull)) {
                  updated = true;
               }
            }

            AtomicInteger renderX;
            AtomicInteger renderY;
            if (isLocked() && (lastX != -1 || lastY != -1)) {
               renderX = new AtomicInteger(lastX);
               renderY = new AtomicInteger(lastY);
            } else {
               int count = (int)guiCache.stream().filter(Objects::nonNull).count();
               renderX = new AtomicInteger(lastX = (int) ((float) event.getMouseX() + xOffset.getValue()));
               renderY = new AtomicInteger(lastY = (int) ((float) (event.getMouseY() - 76 * count / 2) + yOffset.getValue()));
            }

            isMouseInShulkerGui = false;
            guiCache.stream().filter(Objects::nonNull).sorted().forEach((ui) -> {
               ui.posX = renderX.get();
               ui.posY = renderY.get();

               ui.drawScreen(event.getMouseX(), event.getMouseY(), event.getPartialTicks());
               renderY.set(renderY.get() + 76 + 1);
            });
         } finally {
            cacheLock.unlock();
         }

         GlStateManager.enableLighting();
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      }
   }

   @SubscribeEvent
   public void onKeyboardInput(GuiKeyboardEvent event) {
      if (Keyboard.getEventKey() == 42) {
         if (Keyboard.getEventKeyState()) {
            locked = true;
         } else {
            locked = updated = false;
         }
      }

   }

   public static EntityPlayerSP getLocalPlayer() {
      return MC.player;
   }

   public static InventoryPlayer getInventory() {
      return getLocalPlayer().inventory;
   }

   public static <T> boolean isInRange(Collection<T> list, int index) {
      return list != null && index >= 0 && index < list.size();
   }

   public static List<ItemStack> getShulkerContents(ItemStack stack) {
      NonNullList<ItemStack> contents = NonNullList.withSize(27, ItemStack.EMPTY);
      NBTTagCompound compound = stack.getTagCompound();

      if (compound != null && compound.hasKey("BlockEntityTag", 10)) {
         NBTTagCompound tags = compound.getCompoundTag("BlockEntityTag");

         if (tags.hasKey("Items", 9)) {
            ItemStackHelper.loadAllItems(tags, contents);
         }
      }

      return contents;
   }

   static class ShulkerInventory implements IInventory {
      private final List<ItemStack> contents;

      public ShulkerInventory(List<ItemStack> contents) {
         this.contents = contents;
      }

      public int getSizeInventory() {
         return contents.size();
      }

      public boolean isEmpty() {
         return contents.isEmpty();
      }

      public ItemStack getStackInSlot(int index) {
         return contents.get(index);
      }

      public ItemStack decrStackSize(int index, int count) {
         throw new UnsupportedOperationException();
      }

      public ItemStack removeStackFromSlot(int index) {
         throw new UnsupportedOperationException();
      }

      public void setInventorySlotContents(int index, ItemStack stack) {
         throw new UnsupportedOperationException();
      }

      public int getInventoryStackLimit() {
         return 27;
      }

      public void markDirty() {
      }

      public boolean isUsableByPlayer(EntityPlayer player) {
         return false;
      }

      public void openInventory(EntityPlayer player) {
      }

      public void closeInventory(EntityPlayer player) {
      }

      public boolean isItemValidForSlot(int index, ItemStack stack) {
         return index > 0 && index < contents.size() && contents.get(index).equals(stack);
      }

      public int getField(int id) {
         return 0;
      }

      public void setField(int id, int value) {
      }

      public int getFieldCount() {
         return 0;
      }

      public void clear() {
      }

      public String getName() {
         return "";
      }

      public boolean hasCustomName() {
         return false;
      }

      public ITextComponent getDisplayName() {
         return new TextComponentString("");
      }
   }

   static class ShulkerContainer extends Container {
      public ShulkerContainer(ShulkerViewerMod.ShulkerInventory inventory, int size) {
         for(int i = 0; i < size; ++i) {
            int x = i % 9 * 18;
            int y = (i / 9 + 1) * 18 + 1;
            addSlotToContainer(new Slot(inventory, i, x, y));
         }

      }

      public boolean canInteractWith(EntityPlayer playerIn) {
         return false;
      }
   }

   static class GuiShulkerViewer extends GuiContainer implements Comparable<ShulkerViewerMod.GuiShulkerViewer> {
      private final ItemStack parentShulker;
      private final int priority;
      public int posX = 0;
      public int posY = 0;

      public GuiShulkerViewer(Container inventorySlotsIn, ItemStack parentShulker, int priority) {
         super(inventorySlotsIn);
         this.parentShulker = parentShulker;
         this.priority = priority;
         mc = Globals.MC;
         fontRenderer = mc.fontRenderer;
         width = mc.displayWidth;
         height = mc.displayHeight;
         xSize = 176;
         ySize = 76;
      }

      public ItemStack getParentShulker() {
         return parentShulker;
      }

      public int getPosX() {
         return posX;
      }

      public int getPosY() {
         return posY;
      }

      public int getWidth() {
         return xSize;
      }

      public int getHeight() {
         return ySize;
      }

      public void drawScreen(int mouseX, int mouseY, float partialTicks) {
         int DEPTH = 500;
         int x = posX;
         int y = posY;
         GlStateManager.enableTexture2D();
         GlStateManager.disableLighting();
         GlStateManager.color(1.0F, 1.0F, 1.0F, !ShulkerViewerMod.isLocked() ? ShulkerViewerMod.tooltipOpacity.getValue() / 255.0F : ShulkerViewerMod.lockedOpacity.getValue() / 255.0F);
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
         mc.getTextureManager().bindTexture(ShulkerViewerMod.SHULKER_GUI_TEXTURE);
         ShulkerViewerMod.drawTexturedRect(x, y, 0, 0, 176, 16, 500);
         ShulkerViewerMod.drawTexturedRect(x, y + 16, 0, 16, 176, 54, 500);
         ShulkerViewerMod.drawTexturedRect(x, y + 16 + 54, 0, 160, 176, 6, 500);
         GlStateManager.disableDepth();
         FontUtils.drawStringWithShadow(parentShulker.getDisplayName(), (float)(x + 8), (float)(y + 6), Colors.WHITE);
         GlStateManager.enableDepth();
         RenderHelper.enableGUIStandardItemLighting();
         GlStateManager.enableRescaleNormal();
         GlStateManager.enableColorMaterial();
         GlStateManager.enableLighting();
         Slot hoveringOver = null;
         int rx = x + 8;
         int ry = y - 1;

         for (Slot slot : inventorySlots.inventorySlots) {
            if (slot.getHasStack()) {
               int px = rx + slot.xPos;
               int py = ry + slot.yPos;
               mc.getRenderItem().zLevel = 501.0F;
               mc.getRenderItem().renderItemAndEffectIntoGUI(slot.getStack(), px, py);
               mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, slot.getStack(), px, py, null);
               mc.getRenderItem().zLevel = 0.0F;
               if (isPointInRegion(px, py, 16, 16, mouseX, mouseY)) {
                  hoveringOver = slot;
               }
            }
         }

         GlStateManager.disableLighting();
         if (hoveringOver != null) {
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            GlStateManager.colorMask(true, true, true, false);
            drawGradientRect(rx + hoveringOver.xPos, ry + hoveringOver.yPos, rx + hoveringOver.xPos + 16, ry + hoveringOver.yPos + 16, -2130706433, -2130706433);
            GlStateManager.colorMask(true, true, true, true);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.pushMatrix();
            ShulkerViewerMod.isModGeneratedToolTip = true;
            renderToolTip(hoveringOver.getStack(), mouseX + 8, mouseY + 8);
            ShulkerViewerMod.isModGeneratedToolTip = false;
            GlStateManager.popMatrix();
            GlStateManager.enableDepth();
         }

         if (isPointInRegion(posX, posY, getWidth(), getHeight(), mouseX, mouseY)) {
            ShulkerViewerMod.isMouseInShulkerGui = true;
         }

         GlStateManager.disableBlend();
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      }

      protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
      }

      public int compareTo(ShulkerViewerMod.GuiShulkerViewer o) {
         return Integer.compare(priority, o.priority);
      }
   }
}
