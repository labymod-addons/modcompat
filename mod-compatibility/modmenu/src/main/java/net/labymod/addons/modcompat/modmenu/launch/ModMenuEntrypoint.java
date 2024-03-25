package net.labymod.addons.modcompat.modmenu.launch;

import net.labymod.addons.modcompat.mod.fix.ModFixEntrypoint;
import net.labymod.addons.modcompat.modmenu.ModMenuButtonTextureMapper;
import net.labymod.api.Laby;
import net.labymod.api.models.addon.annotation.AddonEntryPoint;
import net.labymod.api.models.version.Version;

@AddonEntryPoint(AddonEntryPoint.Point.ENABLE)
public class ModMenuEntrypoint extends ModFixEntrypoint {

  private static final String MOD_ID = "modmenu";

  public ModMenuEntrypoint() {
    super(MOD_ID);
  }

  @Override
  public void initialize(Version version) {
    if (!super.isModLoaded()) {
      return;
    }
    Laby.labyAPI().eventBus().registerListener(new ModMenuButtonTextureMapper());
  }
}
