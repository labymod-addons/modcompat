package net.labymod.addons.modcompat.mod.fix;

import net.labymod.api.addon.entrypoint.Entrypoint;
import net.labymod.api.models.version.Version;
import net.labymod.api.modloader.ModLoader;
import net.labymod.api.modloader.ModLoaderRegistry;
import net.labymod.api.modloader.mod.ModInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("UnstableApiUsage")
public abstract class ModFixEntrypoint implements Entrypoint {

  private ModInfo modInfo;
  private ModLoader modLoader;
  private boolean modLoaded = false;

  protected ModFixEntrypoint(@NotNull String modId) {
    // Search for mod loader that loaded the target mod
    for (ModLoader loader : ModLoaderRegistry.instance().values()) {
      this.modInfo = loader.getModInfo(modId);
      if (this.modInfo != null) {
        this.modLoader = loader;
        this.modLoaded = true;
        break;
      }
    }
  }

  public @Nullable ModInfo getModInfo() {
    return this.modInfo;
  }

  public @Nullable ModLoader getModLoader() {
    return this.modLoader;
  }

  public boolean isModLoaded() {
    return this.modLoaded;
  }

  @Override
  public void initialize(Version version) {
    // Only initialize fixer if the target mod is loaded
    if (this.modLoaded) {
      this.initializeFixer(version);
    }
  }

  protected abstract void initializeFixer(Version version);
}
