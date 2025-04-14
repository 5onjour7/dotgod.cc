package me.eclipcen.butterflyclient.wrapper;

import net.minecraft.util.Session;
import net.minecraft.util.Timer;

public interface IMinecraft {
   void setRightClickDelayTimer(int var1);

   Timer getTimer();

   void setSession(Session var1);
}
