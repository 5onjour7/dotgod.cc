package me.eclipcen.butterflyclient.command.api;

import me.eclipcen.butterflyclient.util.Globals;

public abstract class Command implements Globals {
   private final String name;
   private String[] aliases;
   private int minLength = 0;
   private int maxLength = Integer.MAX_VALUE;

   public Command(String name) {
      this.name = name;
   }

   public Command(String name, int minLength) {
      this.name = name;
      this.minLength = minLength;
   }

   public Command(String name, int minLength, int maxLength) {
      this.name = name;
      this.minLength = minLength;
      this.maxLength = maxLength;
   }

   public Command(String name, String[] aliases) {
      this.name = name;
      this.aliases = aliases;
   }

   public Command(String name, String[] aliases, int minLength) {
      this.name = name;
      this.aliases = aliases;
      this.minLength = minLength;
   }

   public Command(String name, String[] aliases, int minLength, int maxLength) {
      this.name = name;
      this.aliases = aliases;
      this.minLength = minLength;
      this.maxLength = maxLength;
   }

   public abstract void execute(String[] var1);

   public String getSuggestion(String[] args) {
      return getName();
   }

   public String[] getAliases() {
      return aliases;
   }

   public String getName() {
      return name;
   }

   public int getMinLength() {
      return minLength;
   }

   public int getMaxLength() {
      return maxLength;
   }
}
