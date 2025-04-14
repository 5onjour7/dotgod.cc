package me.eclipcen.butterflyclient.module.api.branches;

import me.eclipcen.butterflyclient.module.api.Category;
import me.eclipcen.butterflyclient.module.api.Module;

public abstract class ServiceMod extends Module {
   public ServiceMod(String name) {
      super(Category.SERVICE, name, "");
   }

   public boolean isEnabled() {
      return true;
   }
}
