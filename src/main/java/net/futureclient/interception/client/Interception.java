package net.futureclient.interception.client;

public final class Interception {
   private static boolean enabled;
   private static String proxyIp;
   private static int proxyPort;

   public static void enable(String ip, int port) {
      enabled = true;
      proxyIp = ip;
      proxyPort = port;
   }

   public static void disable() {
      enabled = false;
      proxyIp = null;
      proxyPort = 0;
   }

   public static boolean isEnabled() {
      return enabled;
   }

   public static String getProxyIp() {
      return proxyIp;
   }

   public static int getProxyPort() {
      return proxyPort;
   }
}
