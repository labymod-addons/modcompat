package net.labymod.addons.modcompat.tweakeroo.launch;

import net.labymod.api.addon.entrypoint.Entrypoint;
import net.labymod.api.loader.platform.PlatformClassloader.TransformerPhase;
import net.labymod.api.loader.platform.PlatformEnvironment;
import net.labymod.api.models.addon.annotation.AddonEntryPoint;
import net.labymod.api.models.version.Version;

@AddonEntryPoint
public class TweakerooEarlyEntrypoint implements Entrypoint {

  @Override
  public void initialize(Version version) {
    PlatformEnvironment.getPlatformClassloader().registerTransformer(
        TransformerPhase.NORMAL,
        "net.labymod.addons.modcompat.tweakeroo.transformer.TweakerooViewBobTransformer"
    );
  }
}
