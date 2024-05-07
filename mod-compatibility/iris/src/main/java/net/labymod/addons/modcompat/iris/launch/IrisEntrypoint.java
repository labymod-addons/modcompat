package net.labymod.addons.modcompat.iris.launch;

import net.labymod.addons.modcompat.iris.IrisCompat;
import net.labymod.addons.modcompat.iris.IrisShaderListener;
import net.labymod.addons.modcompat.mod.fix.ModFixEntrypoint;
import net.labymod.api.Laby;
import net.labymod.api.models.addon.annotation.AddonEntryPoint;
import net.labymod.api.models.addon.annotation.AddonEntryPoint.Point;
import net.labymod.api.models.version.Version;

@AddonEntryPoint(Point.LOAD)
public class IrisEntrypoint extends ModFixEntrypoint {

  public IrisEntrypoint() {
    super(IrisCompat.MOD_ID);
  }

  @Override
  public void initialize(Version version) {
    if (super.isModLoaded()) {
      Laby.labyAPI().eventBus().registerListener(new IrisShaderListener());
    }
  }
}
