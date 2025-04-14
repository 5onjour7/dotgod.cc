package me.eclipcen.butterflyclient.module.impl.misc.announcer;

import java.util.HashMap;
import java.util.Random;

public class MessagePrefixes {
   private static final HashMap<TaskType, MessagePrefixes.MessageMaker> messageMakers = new HashMap<>();

   public static String getMessage(TaskType type, String... args) {
      return " " + messageMakers.get(type).getMessage(args);
   }

   static {
      messageMakers.put(
              TaskType.JOIN,
              new MessagePrefixes.MessageMaker(
                      new String[]{
                              "I feel like %s0 has made a huge mistake",
                              "why? %s0",
                              "you have made a grave mistake %s0",
                              "welcome to this shithole %s0",
                              "welcome to my house %s0",
                              "assalamu alaikum %s0"
                      }
              )
      );
      messageMakers.put(
              TaskType.LEAVE,
              new MessagePrefixes.MessageMaker(
                      new String[]{
                              "EZ LOG %s0!",
                              "keep logging pussy, %s0",
                              "Uh, BYE! %s0",
                              "yeah fuck off %s0 you fucking pussy",
                              "and dont you fucking come back %s0",
                              "LOL %s0 stupid dog logging out!"
                      }
              )
      );
      messageMakers.put(
              TaskType.BREAK,
              new MessagePrefixes.MessageMaker(new String[]{"I just mined %s1 %s0 thanks to DotGod.CC!", "I just zoinked %s1 %s0 thanks to DotGod.CC!"})
      );
      messageMakers.put(
              TaskType.PLACE,
              new MessagePrefixes.MessageMaker(
                      new String[]{"I just placed %s1 %s0 thanks to DotGod.CC!", "I just made a really small base with %s1 %s0 thanks to DotGod.CC!"}
              )
      );
      messageMakers.put(TaskType.EAT, new MessagePrefixes.MessageMaker(new String[]{"I just munched a %s0 thanks to DotGod.CC!"}));
      messageMakers.put(
              TaskType.WALK,
              new MessagePrefixes.MessageMaker(
                      new String[]{
                              "I just flew %s0 meters like a butterfly thanks to DotGod.CC!",
                              "Я только что пролетел %s0 метров как моль с помощью DotGod.CC!",
                              "!DotGod.CC ﻞﻀﻔﺑ ﺔﺷﺍﺮﻔﻛ ﺎﺜﻳﺪﺣ ﺭﺎﺘﻣﺃ %s0 ﺕﺮﻃ ﺎﻧﺃ",
                              "Ich bin gerade %s0 Meter geflogen wie ein Schmetterling danke an DotGod.CC!",
                              "Lensin juuri %s0 metriä kuin perhonen DotGod.CC ansiosta!",
                              "Acabo de volar %s0 metros como una mariposa gracias a DotGod.CC",
                              "Právě jsem uletěl %s0 metrů jako motýl pomocí DotGod.CC!",
                              "Właśnie przeleciałem %s0 metrów jak motylek, dzięki DotGod.CC!"
                      }
              )
      );
   }

   private static class MessageMaker {
      public String[] messages;

      public MessageMaker(String[] messages) {
         this.messages = messages;
      }

      public String getMessage(String... values) {
         String toReturn = messages[(new Random()).nextInt(messages.length)];

         for (int i = 0; i < values.length; ++i) {
            toReturn = toReturn.replace("%s" + i, values[i]);
         }

         return toReturn;
      }
   }
}
