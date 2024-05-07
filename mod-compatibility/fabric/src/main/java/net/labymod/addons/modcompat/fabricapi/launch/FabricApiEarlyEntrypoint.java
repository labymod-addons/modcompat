package net.labymod.addons.modcompat.fabricapi.launch;

import net.labymod.api.addon.entrypoint.Entrypoint;
import net.labymod.api.loader.platform.PlatformClassloader.TransformerPhase;
import net.labymod.api.loader.platform.PlatformEnvironment;
import net.labymod.api.models.addon.annotation.AddonEntryPoint;
import net.labymod.api.models.version.Version;

@AddonEntryPoint
public class FabricApiEarlyEntrypoint implements Entrypoint {

  @Override
  public void initialize(Version version) {
    PlatformEnvironment.getPlatformClassloader().registerTransformer(
        TransformerPhase.PRE,
        "net.labymod.addons.modcompat.fabricapi.transformer.FabricApiConnectionMixinTransformer"
    );
    PlatformEnvironment.getPlatformClassloader().registerTransformer(
        TransformerPhase.PRE,
        "net.labymod.addons.modcompat.fabricapi.transformer.FabricApiGameRendererMixinTransformer"
    );
  }
}
