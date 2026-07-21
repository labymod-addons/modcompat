package net.labymod.addons.modcompat.flashback.launch;

import net.labymod.addons.modcompat.flashback.FlashbackAccessor;
import net.labymod.addons.modcompat.flashback.listener.MainMenuListener;
import net.labymod.addons.modcompat.mod.fix.ModFixEntrypoint;
import net.labymod.api.Laby;
import net.labymod.api.models.addon.annotation.AddonEntryPoint;
import net.labymod.api.models.addon.annotation.AddonEntryPoint.Point;
import net.labymod.api.models.version.Version;
import net.labymod.api.service.CustomServiceLoader;
import net.labymod.api.service.CustomServiceLoader.ServiceType;

@AddonEntryPoint(Point.ENABLE)
public class FlashbackEntrypoint extends ModFixEntrypoint {

  private static final String MOD_ID = "flashback";

  public FlashbackEntrypoint() {
    super(MOD_ID);
  }

  @Override
  public void initialize(Version version) {
    if (!super.isModLoaded()) {
      return;
    }

    for (FlashbackAccessor accessor : CustomServiceLoader.load(
        FlashbackAccessor.class,
        this.getClass().getClassLoader(),
        ServiceType.ADVANCED
    )) {
      Laby.labyAPI().eventBus().registerListener(new MainMenuListener(accessor));
      break;
    }
  }
}
