package net.labymod.addons.modcompat.v1_8_9;

import net.labymod.addons.modcompat.hook.AddonHooks;
import net.labymod.addons.modcompat.v1_8_9.skyblockaddons.config.SkyblockAddonsHookConfig;

public class SkyblockAddonsCompat {

  public static final String ADDON_ID = "skyblockaddons_loader";

  public static boolean isFeatureIntegration() {
    SkyblockAddonsHookConfig config = AddonHooks.instance()
        .getSubSettings(SkyblockAddonsHookConfig.class);
    return config != null && config.featureIntegration().get();
  }
}
