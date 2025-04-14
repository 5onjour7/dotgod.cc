package me.eclipcen.butterflyclient.module.impl.misc;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import me.eclipcen.butterflyclient.event.EventType;
import me.eclipcen.butterflyclient.event.packet.PacketEvent;
import me.eclipcen.butterflyclient.event.player.PlayerUpdateEvent;
import me.eclipcen.butterflyclient.module.api.Category;
import me.eclipcen.butterflyclient.module.api.branches.ToggleMod;
import me.eclipcen.butterflyclient.module.impl.misc.announcer.MessagePrefixes;
import me.eclipcen.butterflyclient.module.impl.misc.announcer.QueuedTask;
import me.eclipcen.butterflyclient.module.impl.misc.announcer.TaskType;
import me.eclipcen.butterflyclient.module.impl.misc.announcer.impl.TaskBasic;
import me.eclipcen.butterflyclient.module.impl.misc.announcer.impl.TaskMove;
import me.eclipcen.butterflyclient.util.client.Timer;
import me.eclipcen.butterflyclient.util.settings.Setting;
import net.futureclient.eventbus.SubscribeEvent;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketPlayerListItem.Action;
import net.minecraft.network.play.server.SPacketPlayerListItem.AddPlayerData;

public class AnnouncerMod extends ToggleMod {
   private final Setting<Boolean> leave = new Setting<>("Leave", "on player leave", true);
   private final Setting<Boolean> join = new Setting<>("Join", "on player join", true);
   private final Setting<Boolean> mined = new Setting<>("Mine", "Announce when you mine", true);
   private final Setting<Boolean> placed = new Setting<>("Place", "Announce when you place", true);
   private final Setting<Boolean> walk = new Setting<>("Walk", "Announce when you walk", true);
   private final Setting<Integer> delay = new Setting<>("Delay", "delay in MS", 11000, 500, 50000, 500);
   private final Setting<AnnouncerMod.Mode> mode = new Setting<>("Mode", "how the message is sent", AnnouncerMod.Mode.Broadcast);
   private final Queue<QueuedTask> toSend = new ConcurrentLinkedQueue<>();
   private final Timer timer = new Timer();

   public AnnouncerMod() {
      super(Category.MISC, "Announcer", "Announces shit in chat");
   }

   public String getHUDTag() {
      return mode.getValue().name();
   }

   @SubscribeEvent
   public void onUpdate(PlayerUpdateEvent event) {
      if (event.getType() == EventType.Type.PRE) {
         if (timer.passed(delay.getValue().longValue())) {
            timer.reset();

            if (toSend.isEmpty()) {
               return;
            }

            QueuedTask task = toSend.poll();
            String msg = task.getMessage();
            if (msg != null) {
               sendMessage(msg);
            }
         }

         if (walk.getValue()) {
            Iterator<QueuedTask> iterator = toSend.iterator();
            boolean hasMoveValue = false;

            while (iterator.hasNext()) {
               if (iterator.next() instanceof TaskMove) {
                  hasMoveValue = true;
               }
            }

            if (!hasMoveValue) {
               toSend.add(new TaskMove(TaskType.WALK));
            }
         }

      }
   }
   
   @SubscribeEvent
   public void onPacket(PacketEvent.Receive.Pre event) {
      if (mc.player != null) {
         if (event.getPacket() instanceof SPacketPlayerListItem) {
            SPacketPlayerListItem packet = event.getPacket();
            if (packet.getEntries().size() == 1) {
               if (packet.getAction() == Action.ADD_PLAYER) {
                  for (AddPlayerData data : packet.getEntries()) {
                     if (!data.getProfile().getId().equals(mc.player.getGameProfile().getId()) && join.getValue()) {
                        QueuedTask task = new TaskBasic(TaskType.JOIN, MessagePrefixes.getMessage(TaskType.JOIN, data.getProfile().getName()));

                        if (timer.passed(2000L)) {
                           timer.reset();
                           String msg = task.getMessage();

                           if (msg != null) {
                              sendMessage(msg);
                           }
                        }
                     }
                  }
               } else if (packet.getAction() == Action.REMOVE_PLAYER) {
                  for (AddPlayerData datax : packet.getEntries()) {
                     if (!datax.getProfile().getId().equals(mc.player.getGameProfile().getId()) && leave.getValue()) {
                        QueuedTask task = new TaskBasic(TaskType.LEAVE, MessagePrefixes.getMessage(TaskType.LEAVE, datax.getProfile().getName()));

                        if (timer.passed(2000L)) {
                           timer.reset();
                           String msg = task.getMessage();
                           if (msg != null) {

                              sendMessage(msg);
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }
   
   private void sendMessage(String message) {
      if (mode.getValue() == AnnouncerMod.Mode.Private) {
         printChatMessage(message);
      } else {
         mc.player.sendChatMessage(message);
      }

   }

   private enum Mode {
      Broadcast,
      Private
   }
}
