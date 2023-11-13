package net.labymod.addons.modcompat;

import net.labymod.addons.modcompat.mod.IncompatibleModRegistry;
import org.jetbrains.annotations.ApiStatus.Internal;

public class ModCompat {

  private static final ModCompat INSTANCE = new ModCompat();

  private IncompatibleModRegistry incompatibleModRegistry;

  private ModCompat() {
  }

  @Internal
  public static void init(IncompatibleModRegistry incompatibleModRegistry) {
    if (INSTANCE.incompatibleModRegistry != null) {
      throw new IllegalStateException("Already initialized");
    }
    INSTANCE.incompatibleModRegistry = incompatibleModRegistry;
  }

  public static ModCompat instance() {
    return INSTANCE;
  }

  public IncompatibleModRegistry incompatibleModRegistry() {
    return this.incompatibleModRegistry;
  }
}
