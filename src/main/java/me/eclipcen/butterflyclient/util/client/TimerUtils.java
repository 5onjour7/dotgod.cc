package me.eclipcen.butterflyclient.util.client;

import me.eclipcen.butterflyclient.util.Globals;
import me.eclipcen.butterflyclient.wrapper.IMinecraft;
import me.eclipcen.butterflyclient.wrapper.ITimer;

public class TimerUtils implements Globals {
   public static float getDefaultSpeed() {
      return 50.0F;
   }

   public static double getTimerSpeed() {
      net.minecraft.util.Timer timer = ((IMinecraft) MC).getTimer();
      return getDefaultSpeed() / ((ITimer) timer).getTickLength();
   }
}
