package net.labymod.addons.modcompat.replaymod.launch;

import net.labymod.api.addon.entrypoint.Entrypoint;
import net.labymod.api.models.addon.annotation.AddonEntryPoint;
import net.labymod.api.models.version.Version;

@AddonEntryPoint
public class ReplayModEarlyEntrypoint implements Entrypoint {

  @Override
  public void initialize(Version version) {
    // TODO: 29.11.23 Enable when button replication works
    /*PlatformEnvironment.getPlatformClassloader().registerTransformer(
        TransformerPhase.NORMAL,
        "net.labymod.addons.modcompat.replaymod.ReplayModPauseButtonsTransformer"
    );*/
  }
}
