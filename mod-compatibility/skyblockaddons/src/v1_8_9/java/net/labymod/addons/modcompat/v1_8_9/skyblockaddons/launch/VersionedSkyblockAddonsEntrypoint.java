package net.labymod.addons.modcompat.v1_8_9.skyblockaddons.launch;

import net.labymod.addons.modcompat.mod.fix.ModFixEntrypoint;
import net.labymod.addons.modcompat.v1_8_9.skyblockaddons.SkyblockAddonsFeatureSync;
import net.labymod.api.Laby;
import net.labymod.api.models.addon.annotation.AddonEntryPoint;
import net.labymod.api.models.version.Version;

@AddonEntryPoint(value = AddonEntryPoint.Point.ENABLE)
public class VersionedSkyblockAddonsEntrypoint extends ModFixEntrypoint {

  public VersionedSkyblockAddonsEntrypoint() {
    super("skyblockaddons");
  }

  @Override
  public void initialize(Version version) {
    if (!super.isModLoaded()) {
      return;
    }

    Laby.labyAPI().eventBus().registerListener(new SkyblockAddonsFeatureSync());
  }
}
