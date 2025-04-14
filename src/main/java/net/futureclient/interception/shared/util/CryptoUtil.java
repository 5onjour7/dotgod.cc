package net.futureclient.interception.shared.util;

import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class CryptoUtil {
   private static final int BITS_IN_BYTE = 8;
   public static final int AES_128_KEY_LENGTH = 16;

   public static byte[] encrypt(byte[] bytes, byte[] key, byte[] iv) throws Exception {
      SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
      IvParameterSpec ivParameter = new IvParameterSpec(iv);
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
      cipher.init(1, secretKey, ivParameter);
      return cipher.doFinal(bytes);
   }

   public static byte[] decrypt(byte[] bytes, byte[] key, byte[] iv) throws Exception {
      SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
      IvParameterSpec ivParameter = new IvParameterSpec(iv);
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
      cipher.init(2, secretKey, ivParameter);
      return cipher.doFinal(bytes);
   }

   private static SecureRandom getSecureRandom() {
      return new SecureRandom();
   }

   public static byte[] generateBytes(int length) {
      byte[] bytes = new byte[length];
      getSecureRandom().nextBytes(bytes);
      return bytes;
   }
}
