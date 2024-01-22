package net.labymod.addons.modcompat.replaymod.launch;

import net.labymod.addons.modcompat.mod.fix.ModFixEntrypoint;
import net.labymod.addons.modcompat.replaymod.listener.IngameMenuListener;
import net.labymod.addons.modcompat.replaymod.listener.IngameOverlayListener;
import net.labymod.addons.modcompat.replaymod.listener.MainMenuListener;
import net.labymod.api.Laby;
import net.labymod.api.models.addon.annotation.AddonEntryPoint;
import net.labymod.api.models.addon.annotation.AddonEntryPoint.Point;
import net.labymod.api.models.version.Version;

@AddonEntryPoint(value = Point.ENABLE)
public class ReplayModEnableEntrypoint extends ModFixEntrypoint {

  public ReplayModEnableEntrypoint() {
    super("replaymod");
  }

  @Override
  public void initialize(Version version) {
    if (!super.isModLoaded()) {
      return;
    }

    Laby.labyAPI().eventBus().registerListener(new IngameMenuListener());
    Laby.labyAPI().eventBus().registerListener(new MainMenuListener());
    Laby.labyAPI().eventBus().registerListener(new IngameOverlayListener());
  }
}
