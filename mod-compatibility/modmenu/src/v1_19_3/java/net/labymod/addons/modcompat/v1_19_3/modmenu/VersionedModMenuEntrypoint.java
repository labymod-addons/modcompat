package net.labymod.addons.modcompat.v1_19_3.modmenu;

import net.labymod.addons.modcompat.mod.fix.ModFixEntrypoint;
import net.labymod.api.Laby;
import net.labymod.api.models.addon.annotation.AddonEntryPoint;
import net.labymod.api.models.addon.annotation.AddonEntryPoint.Point;
import net.labymod.api.models.version.Version;

@AddonEntryPoint(value = Point.ENABLE)
public class VersionedModMenuEntrypoint extends ModFixEntrypoint {

  private static final String MOD_ID = "modmenu";
  private static final String BUTTON_CONVERTER_NAME = "Button";

  public VersionedModMenuEntrypoint() {
    super(MOD_ID);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void initialize(Version version) {
    if (!super.isModLoaded()) {
      return;
    }

    Laby.references().widgetConverterRegistry().findConverter(BUTTON_CONVERTER_NAME)
        .ifPresent(converter -> converter.registerMapper(new TexturedButtonMapper()));
  }
}
