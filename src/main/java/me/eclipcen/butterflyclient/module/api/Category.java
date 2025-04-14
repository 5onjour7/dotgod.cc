package me.eclipcen.butterflyclient.module.api;

import java.awt.Color;

public enum Category {
   RENDER("Render", "stuff on the screen", new Color(255, 124, 191)),
   COMBAT("Combat", "elite combate advantages", new Color(172, 64, 231)),
   MISC("Misc", "Misc utility mods", new Color(76, 64, 226)),
   MOVEMENT("Movement", "Movement mods", new Color(227, 25, 136)),
   PLAYER("Player", "Mods that change player behaviour", new Color(38, 217, 163)),
   SERVICE("Service", "Background mods", new Color(0, 0, 0)),
   OTHER("Other", "Always on mods", new Color(255, 255, 255));

   private final String Name;
   private final String description;
   private final Color color;

   Category(String name, String description, Color color) {
      this.Name = name;
      this.description = description;
      this.color = color;
   }

   public String getName() {
      return this.Name;
   }

   public String getDescription() {
      return this.description;
   }

   public Color getColor() {
      return this.color;
   }
}
