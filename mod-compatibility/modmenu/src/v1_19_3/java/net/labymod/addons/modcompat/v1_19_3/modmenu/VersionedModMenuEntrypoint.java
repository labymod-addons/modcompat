package net.labymod.addons.modcompat.v1_19_3.modmenu;

import net.labymod.api.Laby;
import net.labymod.api.addon.entrypoint.Entrypoint;
import net.labymod.api.models.addon.annotation.AddonEntryPoint;
import net.labymod.api.models.addon.annotation.AddonEntryPoint.Point;
import net.labymod.api.models.version.Version;

@AddonEntryPoint(value = Point.ENABLE)
public class VersionedModMenuEntrypoint implements Entrypoint {

  private static final String BUTTON_CONVERTER_NAME = "Button";

  @SuppressWarnings("unchecked")
  @Override
  public void initialize(Version version) {
    Laby.references().widgetConverterRegistry().findConverter(BUTTON_CONVERTER_NAME)
        .ifPresent(converter -> converter.registerMapper(new TexturedButtonMapper()));
  }
}
