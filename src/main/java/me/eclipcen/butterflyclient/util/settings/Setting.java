package me.eclipcen.butterflyclient.util.settings;

public class Setting<T> {
   private final String name;
   private final String desc;
   public T value;
   private T min;
   private T max;
   private T inc;

   public Setting(String name, T value) {
      this(name, "", value);
   }

   public Setting(String name, String desc, T value) {
      this.name = name;
      this.desc = desc;
      this.value = value;
   }

   public Setting(String name, T value, T min, T max, T inc) {
      this(name, "", value, min, max, inc);
   }

   public Setting(String name, String desc, T value, T min, T max, T inc) {
      this.name = name;
      this.desc = desc;
      this.value = value;
      this.min = min;
      this.max = max;
      this.inc = inc;
   }

   public T getValue() {
      return value;
   }

   public void setValue(T value) {
      if (min != null && max != null) {
         Number val = (Number) value;
         this.value = (T) val;
      } else {
         this.value = value;
      }

   }

   public int getEnum(String input) {
      for(int i = 0; i < value.getClass().getEnumConstants().length; ++i) {
         Enum<?> e = (Enum<?>) value.getClass().getEnumConstants()[i];
         if (e.name().equalsIgnoreCase(input)) {
            return i;
         }
      }

      return -1;
   }

   public void setEnumValue(String value) {
      for (Enum<?> e : ((Enum<?>) this.value).getClass().getEnumConstants()) {
         if (e.name().equalsIgnoreCase(value)) {
            this.value = (T) e;
         }
      }
   }


   public T getMin() {
      return min;
   }

   public T getMax() {
      return max;
   }

   public T getInc() {
      return inc;
   }

   public String getName() {
      return name;
   }

   public String getDesc() {
      return desc;
   }
}
