package net.labymod.addons.modcompat.viafabric.launch;

import net.labymod.addons.modcompat.gui.ServerListButtonListener;
import net.labymod.addons.modcompat.mod.fix.ModFixEntrypoint;
import net.labymod.addons.modcompat.viafabric.ViaFabricAccessor;
import net.labymod.api.Laby;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.models.addon.annotation.AddonEntryPoint;
import net.labymod.api.models.addon.annotation.AddonEntryPoint.Point;
import net.labymod.api.models.version.Version;
import net.labymod.api.service.CustomServiceLoader;
import net.labymod.api.service.CustomServiceLoader.ServiceType;

@AddonEntryPoint(Point.ENABLE)
public class ViaFabricEntrypoint extends ModFixEntrypoint {

  private static final String MOD_ID = "viafabric";
  private static final ResourceLocation ICON = ResourceLocation.create(MOD_ID, "textures/logo.png");

  public ViaFabricEntrypoint() {
    super(MOD_ID);
  }

  @Override
  public void initialize(Version version) {
    if (!super.isModLoaded()) {
      return;
    }

    for (ViaFabricAccessor accessor : CustomServiceLoader.load(
        ViaFabricAccessor.class,
        this.getClass().getClassLoader(),
        ServiceType.ADVANCED
    )) {
      Laby.labyAPI().eventBus().registerListener(new ServerListButtonListener(
          "ViaFabric",
          ICON,
          () -> !accessor.isButtonHidden(),
          accessor::openConfigScreen
      ));
      break;
    }
  }
}
