package net.labymod.addons.modcompat.v1_20_2;

import net.labymod.api.addon.entrypoint.Entrypoint;
import net.labymod.api.models.addon.annotation.AddonEntryPoint;
import net.labymod.api.models.version.Version;
import net.labymod.api.util.logging.Logging;

@AddonEntryPoint
public class EntrypointTest implements Entrypoint {

  private static final Logging LOGGER = Logging.create(EntrypointTest.class);

  @Override
  public void initialize(Version version) {
    LOGGER.warn("Hello from 1.20.2");
  }
}
