package me.eclipcen.butterflyclient.util.math;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public final class MathUtil {
   private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

   public static String getRounded(float num) {
      return DECIMAL_FORMAT.format((double)num);
   }

   public static String getRounded(double num) {
      return DECIMAL_FORMAT.format(num);
   }

   public static double square(double input) {
      return input * input;
   }

   public static float square(float input) {
      return input * input;
   }

   public static double radToDeg(double rad) {
      return rad * 57.295780181884766D;
   }

   public static double degToRad(double deg) {
      return deg * 0.01745329238474369D;
   }

   public static Vec3d direction(float yaw) {
      return new Vec3d(Math.cos(degToRad(yaw + 90.0F)), 0.0D, Math.sin(degToRad(yaw + 90.0F)));
   }

   public static Vec3d mult(Vec3d factor, Vec3d multiplier) {
      return new Vec3d(factor.x * multiplier.x, factor.y * multiplier.y, factor.z * multiplier.z);
   }

   public static Vec3d mult(Vec3d factor, float multiplier) {
      return new Vec3d(factor.x * (double) multiplier, factor.y * (double) multiplier, factor.z * (double) multiplier);
   }

   public static Vec3d div(Vec3d factor, Vec3d divisor) {
      return new Vec3d(factor.x / divisor.x, factor.y / divisor.y, factor.z / divisor.z);
   }

   public static Vec3d div(Vec3d factor, float divisor) {
      return new Vec3d(factor.x / (double) divisor, factor.y / (double)divisor, factor.z / (double) divisor);
   }

   public static double round(double value, int places) {
      return places < 0 ? value : new BigDecimal(value).setScale(places, RoundingMode.HALF_UP).doubleValue();
   }

   public static float clamp(float val, float min, float max) {
      if (val <= min) {
         val = min;
      }

      if (val >= max) {
         val = max;
      }

      return val;
   }

   public static float wrap(float val) {
      val %= 360.0F;
      if (val >= 180.0F) {
         val -= 360.0F;
      }

      if (val < -180.0F) {
         val += 360.0F;
      }

      return val;
   }

   public static double map(double value, double a, double b, double c, double d) {
      value = (value - a) / (b - a);
      return c + value * (d - c);
   }

   public static double linear(double from, double to, double incline) {
      return from < to - incline ? from + incline : (from > to + incline ? from - incline : to);
   }

   public static double parabolic(double from, double to, double incline) {
      return from + (to - from) / incline;
   }

   public static double getDistance(Vec3d pos, double x, double y, double z) {
      double deltaX = pos.x - x;
      double deltaY = pos.y - y;
      double deltaZ = pos.z - z;
      return MathHelper.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
   }

   public static double getDistance(double startX, double startY, double startZ, double toX, double toY, double toZ) {
      double deltaX = startX - toX;
      double deltaY = startY - toY;
      double deltaZ = startZ - toZ;
      return MathHelper.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
   }

   public static double getDistanceSq(double startX, double startY, double startZ, double toX, double toY, double toZ) {
      double deltaX = startX - toX;
      double deltaY = startY - toY;
      double deltaZ = startZ - toZ;
      return MathHelper.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
   }

   public static double[] calcIntersection(double[] line, double[] line2) {
      double a1 = line[3] - line[1];
      double b1 = line[0] - line[2];
      double c1 = a1 * line[0] + b1 * line[1];
      double a2 = line2[3] - line2[1];
      double b2 = line2[0] - line2[2];
      double c2 = a2 * line2[0] + b2 * line2[1];
      double delta = a1 * b2 - a2 * b1;
      return new double[]{(b2 * c1 - b1 * c2) / delta, (a1 * c2 - a2 * c1) / delta};
   }
}
