package net.labymod.addons.modcompat.viafabricplus.launch;

import net.labymod.addons.modcompat.gui.ServerListButtonListener;
import net.labymod.addons.modcompat.mod.fix.ModFixEntrypoint;
import net.labymod.addons.modcompat.viafabricplus.ViaFabricPlusAccessor;
import net.labymod.api.Laby;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.models.addon.annotation.AddonEntryPoint;
import net.labymod.api.models.addon.annotation.AddonEntryPoint.Point;
import net.labymod.api.models.version.Version;
import net.labymod.api.service.CustomServiceLoader;
import net.labymod.api.service.CustomServiceLoader.ServiceType;

@AddonEntryPoint(Point.ENABLE)
public class ViaFabricPlusEntrypoint extends ModFixEntrypoint {

  private static final String MOD_ID = "viafabricplus";
  private static final ResourceLocation ICON = ResourceLocation.create(MOD_ID, "icon.png");

  public ViaFabricPlusEntrypoint() {
    super(MOD_ID);
  }

  @Override
  public void initialize(Version version) {
    if (!super.isModLoaded()) {
      return;
    }

    for (ViaFabricPlusAccessor accessor : CustomServiceLoader.load(
        ViaFabricPlusAccessor.class,
        this.getClass().getClassLoader(),
        ServiceType.ADVANCED
    )) {
      Laby.labyAPI().eventBus().registerListener(new ServerListButtonListener(
          "ViaFabricPlus",
          ICON,
          () -> true,
          accessor::openProtocolSelectionScreen
      ));
      break;
    }
  }
}
