package net.labymod.addons.modcompat;

import net.labymod.addons.modcompat.mod.IncompatibleModRegistry;
import net.labymod.api.reference.ReferenceStorageAccessor;
import org.jetbrains.annotations.ApiStatus.Internal;

public class ModCompat {

  private static final ModCompat INSTANCE = new ModCompat();

  private ReferenceStorageAccessor references;
  private IncompatibleModRegistry incompatibleModRegistry;

  private ModCompat() {
  }

  public static void setReferences(ReferenceStorageAccessor references) {
    if (INSTANCE.references != null) {
      throw new IllegalStateException("References already set");
    }
    INSTANCE.references = references;
  }

  @Internal
  public static void setModRegistry(IncompatibleModRegistry incompatibleModRegistry) {
    if (INSTANCE.incompatibleModRegistry != null) {
      throw new IllegalStateException("Mod registry already set");
    }
    INSTANCE.incompatibleModRegistry = incompatibleModRegistry;
  }

  public static ModCompat instance() {
    return INSTANCE;
  }

  public IncompatibleModRegistry incompatibleModRegistry() {
    return this.incompatibleModRegistry;
  }

  public ReferenceStorageAccessor references() {
    return this.references;
  }
}
