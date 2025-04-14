//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\alias\Documents\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

package me.eclipcen.butterflyclient.module.impl.other;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.event.EventType.Type;
import me.eclipcen.butterflyclient.event.client.ModuleEvent.Start;
import me.eclipcen.butterflyclient.event.client.ModuleEvent.Stop;
import me.eclipcen.butterflyclient.event.client.TickEvent.Pre;
import me.eclipcen.butterflyclient.event.player.PlayerUpdateEvent;
import me.eclipcen.butterflyclient.event.render.RenderEvent.Render2DEvent;
import me.eclipcen.butterflyclient.event.world.WorldEvent.Load;
import me.eclipcen.butterflyclient.module.api.Category;
import me.eclipcen.butterflyclient.module.api.Module;
import me.eclipcen.butterflyclient.module.api.branches.ToggleMod;
import me.eclipcen.butterflyclient.module.impl.service.TickrateRecorder;
import me.eclipcen.butterflyclient.module.impl.service.TickrateRecorder.TickRateData;
import me.eclipcen.butterflyclient.util.client.ItemUtils;
import me.eclipcen.butterflyclient.util.client.Timer;
import me.eclipcen.butterflyclient.util.client.TimerUtils;
import me.eclipcen.butterflyclient.util.colors.Colors;
import me.eclipcen.butterflyclient.util.colors.rainbow.RainbowUtils;
import me.eclipcen.butterflyclient.util.entity.EntityUtils;
import me.eclipcen.butterflyclient.util.entity.MovementUtils;
import me.eclipcen.butterflyclient.util.font.FontUtils;
import me.eclipcen.butterflyclient.util.math.MathUtil;
import me.eclipcen.butterflyclient.util.settings.Setting;
import net.futureclient.eventbus.SubscribeEvent;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;

public class HudMod extends ToggleMod {
   private final Setting<Integer> infoY = new Setting<>("infoY", "cunt", 160, 2, 1080, 1);
   private final Setting<Integer> infoX = new Setting<>("infoX", "how much on the x", 2, 2, 1920, 1);
   private final Setting<Boolean> infoBelowMark = new Setting<>("InfoBelowMark", "Info including hitrange, placerange, and more", true);
   private final Setting<Float> scale = new Setting<>("Scale", "", 0.7F, 0.3F, 1.0F, 0.01F);
   private final Setting<Boolean> armour = new Setting<>("Armour", "renders ur armour", false);
   private final Setting<Boolean> direction = new Setting<>("Direction", "shows where ur facing", true);
   private final Setting<Boolean> rotations = new Setting<>("Rotations", "shows your rotations", true);
   private final Setting<Boolean> netherCoords = new Setting<>("NetherCoords", "shows your nether coords", true);
   private final Setting<Boolean> coords = new Setting<>("Coords", "shows your coords", true);
   private final Setting<Boolean> speed = new Setting<>("Speed", "Shows speed", true);
   private final Setting<Boolean> ping = new Setting<>("Ping", "Shows ping", true);
   private final Setting<Boolean> tps = new Setting<>("TPS", "shows TPS", true);
   private final Setting<Boolean> fps = new Setting<>("FPS", "shows FPS", true);
   private final Setting<Boolean> potions = new Setting<>("Potions", "shows the potions you got", true);
   private final Setting<Boolean> arrayList = new Setting<>("Arraylist", "Show arraylist", true);
   private final Setting<Boolean> modToggle = new Setting<>("ChatModAlerts", "Tells you when a module was toggled in chat", true);
   private final Setting<Boolean> watermark = new Setting<>("Watermark", "pink lemonade", true);
   private final Setting<Boolean> arrayTags = new Setting<>("HudTags", "Shows the extra module information on arraylist", true);
   public static Setting<Boolean> descriptions = new Setting<>("Descriptions", "Shows descriptions in the clickGUI", true);
   public static Setting<Boolean> customFont = new Setting<>("CustomFont", "enables the custom font", true);
   private final Setting<Integer> a = new Setting<>("Alpha", "alpha of HUD and main colours", 255, 0, 255, 1);
   private final Setting<HudMod.CMode> colourMode = new Setting<>("CMode", "Mode of colour", HudMod.CMode.Static);
   private final Setting<HudMod.Order> order = new Setting<>("Order", "Order of arraylist", HudMod.Order.Length);
   private final Setting<HudMod.Side> side = new Setting<>("Place", "Side of the arraylist", HudMod.Side.Top);
   private final Setting<String> dotGod = new Setting<>("DotGod", "watermark name", "DotGod.CC");
   private final Setting<String> watermarkName = new Setting<>("WatermarkName", "water", "Butterfly");
   private final Object2IntMap<Module> modulePositions = new Object2IntOpenHashMap<>();
   private final List<Module> sortedModules = new ArrayList<>();
   private final Timer slideTimer = new Timer();
   private final Random random = new Random();
   private boolean insideWater = false;
   private float targetY = 14.0F;
   private ColoursMod colours = Butterfly.getInstance().getModuleManager().getModule(ColoursMod.class);

   public HudMod() {
      super(Category.OTHER, "HUD", "Heads up display");
   }

   @SubscribeEvent
   public void onWorldLoad(Load event) {
      if (colours == null) {
         colours = Butterfly.getInstance().getModuleManager().getModule(ColoursMod.class);
      }
   }

   @SubscribeEvent
   public void onUpdate(PlayerUpdateEvent event) {
      if (event.getType() == Type.POST) {
         insideWater = mc.player.isInsideOfMaterial(Material.WATER);
      }
   }

   @SubscribeEvent
   public void onTick(Pre event) {
      if (mc.player != null) {
         if (sortedModules.isEmpty()) {
            sortedModules.addAll(Butterfly.getInstance().getModuleManager().getMods());
         }

         switch (order.getValue()) {
            case Length:
               sortedModules.sort(Comparator.comparingInt(mod -> -FontUtils.getStringWidth(getModuleName(mod))));
               break;
            case ABC:
               sortedModules.sort(Comparator.comparing(this::getModuleName));
         }
      }
   }

   @SubscribeEvent
   public void onRenderScreen(Render2DEvent event) {
      if (mc.player != null) {
         int scaledHeight = (int)event.getScreenHeight();
         int scaledWidth = (int)event.getScreenWidth();
         if (watermark.getValue()) {
            String welcome = "Hello " + mc.player.getName() + " :^)";
            FontUtils.drawStringWithShadow(watermarkName.getValue() + " v2.2.3", 2.0F, 11.0F, getColor());
            FontUtils.drawStringWithShadow(welcome, (scaledWidth - FontUtils.getStringWidth(welcome)) / 2.0F, 2.0F, getColor());
            FontUtils.drawStringWithShadow(dotGod.getValue(), 2.0F, 150.0F, getColor());
         }

         if (infoBelowMark.getValue()) {
            boolean isInRange = false;
            boolean isInPlaceRange = false;
            boolean isBreakableFeet = false;
            FontUtils.drawStringWithShadow(
                    "HTR", infoX.getValue(), infoY.getValue(), isInRange ? getColorGreen() : getColorRed()
            );
            FontUtils.drawStringWithShadow(
                    "PLR", infoX.getValue(), infoY.getValue() + 9, isInPlaceRange ? getColorGreen() : getColorRed()
            );
            int totems = 0;

            for (int i = 0; i <= 45; i++) {
               if (mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) {
                  totems++;
               }
            }

            if (mc.player.inventory.getItemStack().getItem() == Items.TOTEM_OF_UNDYING) {
               totems++;
            }

            FontUtils.drawStringWithShadow(
                    Integer.toString(totems), infoX.getValue(), infoY.getValue() + 18, totems != 0 ? getColorGreen() : getColorRed()
            );
            int ping = EntityUtils.getPing(mc.player);
            FontUtils.drawStringWithShadow(
                    "PING " + ping, infoX.getValue(), infoY.getValue() + 27, ping <= 100 ? getColorGreen() : getColorRed()
            );
            FontUtils.drawStringWithShadow(
                    "LBY", infoX.getValue(), infoY.getValue() + 36, isBreakableFeet ? getColorGreen() : getColorRed()
            );
         }

         float ySpeed = 22.0F / (Minecraft.getDebugFPS() >> 2);

         if (mc.currentScreen instanceof GuiChat) {
            if (targetY < 14.0F) {
               targetY = Math.min(targetY + ySpeed, 14.0F);
            }
         } else if (targetY > 0.0F) {
            targetY = Math.max(targetY - ySpeed, 0.0F);
         }

         float ix = side.getValue() == HudMod.Side.Top ? targetY : 0.0F;
         float j = targetY;

         if (potions.getValue()) {
            for (PotionEffect effect : mc.player.getActivePotionEffects()) {
               Potion potion = effect.getPotion();
               String potionString = I18n.format(potion.getName())
                       + (effect.getAmplifier() > 0 ? " " + (effect.getAmplifier() + 1) + ": " : ": ")
                       + "\u00A77"
                       + Potion.getPotionDurationString(effect, 1.0F);

               FontUtils.drawStringWithShadow(
                       potionString,
                       scaledWidth - FontUtils.getStringWidth(potionString) - 2,
                       side.getValue() == HudMod.Side.Top ? scaledHeight - 2 - (ix += 9.0F) : 2.0F + (ix += 9.0F) - 9.0F,
                       Colors.changeAlpha(potion.getLiquidColor(), a.getValue())
               );
            }
         }

         if (speed.getValue()) {
            String currentSpeed = MathUtil.getRounded(MovementUtils.getPlayerSpeed() * TimerUtils.getTimerSpeed());
            String speed = "Speed: \u00A7f" + currentSpeed + "km/h";
            FontUtils.drawStringWithShadow(
                    speed,
                    scaledWidth - FontUtils.getStringWidth(speed) - 2,
                    side.getValue() == HudMod.Side.Top ? scaledHeight - 2 - (ix += 9.0F) : 2.0F + (ix += 9.0F) - 9.0F,
                    getColor()
            );
         }

         if (ping.getValue()) {
            int pingNum = EntityUtils.getPing(mc.player);
            String ping = "Ping: \u00A7f" + pingNum + "ms";
            FontUtils.drawStringWithShadow(
                    ping,
                    scaledWidth - FontUtils.getStringWidth(ping) - 2,
                    side.getValue() == HudMod.Side.Top ? scaledHeight - 2 - (ix += 9.0F) : 2.0F + (ix += 9.0F) - 9.0F,
                    getColor()
            );
         }

         if (tps.getValue()) {
            String tps = "TPS: \u00A7f" + getTPS();
            FontUtils.drawStringWithShadow(
                    tps,
                    scaledWidth - FontUtils.getStringWidth(tps) - 2,
                    side.getValue() == HudMod.Side.Top ? scaledHeight - 2 - (ix += 9.0F) : 2.0F + (ix += 9.0F) - 9.0F,
                    getColor()
            );
         }

         if (fps.getValue()) {
            String fps = "FPS: \u00A7f" + Minecraft.getDebugFPS();
            FontUtils.drawStringWithShadow(
                    fps,
                    scaledWidth - FontUtils.getStringWidth(fps) - 2,
                    side.getValue() == HudMod.Side.Top ? scaledHeight - 2 - (ix + 9.0F) : 2.0F + ix + 9.0F - 9.0F,
                    getColor()
            );
         }

         if (coords.getValue()) {
            StringBuilder coordBuilder = new StringBuilder(
                    "XYZ: \u00A7f"
                            + MathUtil.getRounded(mc.player.posX)
                            + "\u00A77, \u00A7f"
                            + MathUtil.getRounded(mc.player.posY)
                            + "\u00A77, \u00A7f"
                            + MathUtil.getRounded(mc.player.posZ)
            );
            if (netherCoords.getValue()) {
               coordBuilder.append(" \u00A77(\u00A7f")
                       .append(MathUtil.getRounded(getDimensionCoord(mc.player.posX)))
                       .append("\u00A77, \u00A7f")
                       .append(MathUtil.getRounded(getDimensionCoord(mc.player.posZ)))
                       .append("\u00A77)");
            }

            if (direction.getValue()) {
               coordBuilder.append(" \u00A77[\u00A7f").append(getFacing()).append("\u00A77]");
            }

            FontUtils.drawStringWithShadow(coordBuilder.toString(), 2.0F, scaledHeight - 2 - (j += 9.0F), getColor());
         }

         if (rotations.getValue()) {
            String yaw = "Yaw: \u00A7f" + MathUtil.getRounded(MathHelper.wrapDegrees(mc.player.rotationYaw));
            String pitch = "Pitch: \u00A7f" + MathUtil.getRounded(mc.player.rotationPitch);
            
            float var22;
            FontUtils.drawStringWithShadow(pitch, 2.0F, scaledHeight - 2 - (var22 = j + 9.0F), getColor());
            FontUtils.drawStringWithShadow(yaw, 2.0F, scaledHeight - 2 - (var22 + 9.0F), getColor());
         }

         if (armour.getValue()) {
            int middle = scaledWidth >> 1;
            int y = scaledHeight - 40;

            if (!mc.player.isCreative()) {
               y -= insideWater ? 25 : 15;
               if (mc.player.isRiding()) {
                  y -= 10;
               }
            }

            float scale = this.scale.getValue();
            GlStateManager.pushMatrix();

            for (int index = 3; index >= 0; index--) {
               ItemStack stack = mc.player.inventory.armorInventory.get(index);

               if (!stack.isEmpty()) {
                  int x = middle - 76 + (9 - index - 1) * 18 + 2;

                  GlStateManager.enableDepth();
                  mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
                  mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, stack, x, y, "");
                  GlStateManager.disableLighting();
                  GlStateManager.disableDepth();

                  if (stack.isItemStackDamageable()) {
                     float maxDamage = stack.getMaxDamage();
                     int damage = (int)((maxDamage - stack.getItemDamage()) / maxDamage * 100.0F);
                     String damageString = damage + "%";

                     float posX = (x + 8 - FontUtils.getStringWidth(damageString) * scale / 2.0F - 1.0F) / scale;
                     float posY = (y - 6) / scale;

                     GlStateManager.pushMatrix();
                     GlStateManager.scale(scale, scale, 0.0F);
                     FontUtils.drawStringWithShadow(damageString, posX + 1.0F, posY, ItemUtils.getColourFromDurability(stack));
                     GlStateManager.popMatrix();
                  }
               }
            }

            GlStateManager.popMatrix();
         }

         if (arrayList.getValue()) {
            int posY = 2;
            int rainbowOffset = -100;

            for (Module module : sortedModules) {
               if (module.isDrawn()) {
                  modulePositions.putIfAbsent(module, -8);

                  int modulePosition = modulePositions.get(module);
                  int renderPos = scaledWidth - modulePosition - 2;

                  String moduleName = getModuleName(module);

                  if (module.isEnabled()) {

                     FontUtils.drawStringWithShadow(
                             moduleName,
                             renderPos,
                             side.getValue() == HudMod.Side.Top ? posY : scaledHeight - (mc.currentScreen instanceof GuiChat ? 14 : 0) - posY - 9,
                             colourMode.getValue() == HudMod.CMode.Rainbow
                                     ? Colors.changeAlpha(RainbowUtils.getRainbowColorOffset(rainbowOffset).getRGB(), a.getValue())
                                     : getModuleColor(module)
                     );

                     if (modulePosition < FontUtils.getStringWidth(moduleName) && slideTimer.passed(10L)) {
                        modulePosition += FontUtils.getStringWidth(moduleName) / 12 + 1;
                        modulePositions.put(module, modulePosition);
                        slideTimer.reset();
                     }

                     if (modulePosition > FontUtils.getStringWidth(moduleName)) {
                        modulePositions.put(module, modulePosition - 1);
                     }

                     rainbowOffset += 100;
                     posY += 9;
                  } else if (modulePosition > -8) {
                     if (slideTimer.passed(10L)) {
                        modulePositions.put(module, modulePosition - (FontUtils.getStringWidth(moduleName) / 12 + 1));
                        slideTimer.reset();
                     }

                     FontUtils.drawStringWithShadow(
                             moduleName,
                             renderPos,
                             side.getValue() == HudMod.Side.Top ? posY : scaledHeight - posY - 9,
                             colourMode.getValue() == HudMod.CMode.Rainbow
                                     ? Colors.changeAlpha(RainbowUtils.getRainbowColorOffset(rainbowOffset).getRGB(), a.getValue())
                                     : getModuleColor(module)
                     );

                     rainbowOffset += 100;
                     posY += 9;
                  }
               }
            }
         }
      }
   }

   @SubscribeEvent
   public void onModuleStart(Start event) {
      if (arrayList.getValue() && colourMode.getValue() == HudMod.CMode.Random) {
         Color color = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
         event.getModule().setRandomColor(color);
      }

      if (modToggle.getValue()) {
         printChatMessageWithDelete("\u00A73" + event.getModule().getModName() + " \u00A7dwas \u00A7aenabled");
      }
   }

   @SubscribeEvent
   public void onModuleStop(Stop event) {
      if (modToggle.getValue()) {
         printChatMessageWithDelete("\u00A73" + event.getModule().getModName() + " \u00A7dwas \u00A7cdisabled");
      }
   }

   private String getModuleName(Module module) {
      return arrayTags.getValue() && !module.getHUDTag().isEmpty() ? module.getModName() + " \u00A77[\u00A7f" + module.getHUDTag() + "\u00A77]" : module.getModName();
   }

   private int getModuleColor(Module module) {
      if (colourMode.getValue() == HudMod.CMode.Category) {
         return Colors.changeAlpha(module.getModCategory().getColor().getRGB(), a.getValue());
      } else if (colourMode.getValue() == HudMod.CMode.Random) {
         if (module.getRandomColor() == null) {
            Color color = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
            module.setRandomColor(color);
         }

         return module.getRandomColor().getRGB();
      } else {
         return Colors.changeAlpha(colours.global.getValue().getRGB(), a.getValue());
      }
   }

   private double getDimensionCoord(double coord) {
      return mc.player.dimension == 0 ? coord * 0.125 : coord * 8.0;
   }

   private String getFacing() {
      Entity entity = mc.getRenderViewEntity() == null ? mc.player : mc.getRenderViewEntity();
      EnumFacing enumfacing = entity.getHorizontalFacing();
      String s = "Invalid";

      switch (enumfacing) {
         case NORTH:
            s = "-Z";
            break;
         case SOUTH:
            s = "+Z";
            break;
         case WEST:
            s = "-X";
            break;
         case EAST:
            s = "+X";
      }

      return s;
   }

   private int getColor() {
      return colourMode.getValue() == HudMod.CMode.Rainbow
              ? Colors.changeAlpha(RainbowUtils.getRainbowColor().getRGB(), a.getValue())
              : (a.getValue() & 0xFF) << 24
              | (colours.global.getValue().getRed() & 0xFF) << 16
              | (colours.global.getValue().getGreen() & 0xFF) << 8
              | colours.global.getValue().getBlue() & 0xFF;
   }

   private int getColorGreen() {
      return 8453123;
   }

   private int getColorRed() {
      return 14224393;
   }

   private String getTPS() {
      TickRateData data = TickrateRecorder.getTickData();
      return data.getSampleSize() <= 0 ? "0.00" : MathUtil.getRounded(data.getPoint().getAverage());
   }

   private enum CMode {
      Static,
      Category,
      Random,
      Rainbow
   }

   private enum Order {
      Length,
      ABC
   }

   private enum Side {
      Top,
      Bottom
   }
}
