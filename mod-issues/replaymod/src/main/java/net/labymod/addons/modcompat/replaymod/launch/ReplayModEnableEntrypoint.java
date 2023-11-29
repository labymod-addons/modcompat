package net.labymod.addons.modcompat.replaymod.launch;

import net.labymod.api.addon.entrypoint.Entrypoint;
import net.labymod.api.models.addon.annotation.AddonEntryPoint;
import net.labymod.api.models.addon.annotation.AddonEntryPoint.Point;
import net.labymod.api.models.version.Version;

@AddonEntryPoint(value = Point.ENABLE)
public class ReplayModEnableEntrypoint implements Entrypoint {

  @Override
  public void initialize(Version version) {
    // TODO: 29.11.23 Fix
    // Laby.labyAPI().eventBus().registerListener(new IngameMenuListener());
  }
}
