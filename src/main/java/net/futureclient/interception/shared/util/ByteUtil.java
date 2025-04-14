package net.futureclient.interception.shared.util;

import java.nio.charset.StandardCharsets;

public final class ByteUtil {
   private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.UTF_8);

   public static byte[] hexStringToBytes(String str) {
      byte[] bytes = new byte[str.length() / 2];

      for (int i = 0; i < str.length(); i += 2) {
         bytes[i / 2] = (byte)((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i + 1), 16));
      }

      return bytes;
   }

   public static String bytesToHexString(byte[] bytes) {
      byte[] hexChars = new byte[bytes.length * 2];

      for (int i = 0; i < bytes.length; i++) {
         int v = bytes[i] & 255;
         hexChars[i * 2] = HEX_ARRAY[v >>> 4];
         hexChars[i * 2 + 1] = HEX_ARRAY[v & 15];
      }

      return new String(hexChars, StandardCharsets.UTF_8);
   }
}
