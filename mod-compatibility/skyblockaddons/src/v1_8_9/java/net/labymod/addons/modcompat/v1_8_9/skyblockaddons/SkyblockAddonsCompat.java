package net.labymod.addons.modcompat.v1_8_9.skyblockaddons;

import java.util.function.Consumer;
import net.labymod.addons.modcompat.hook.AddonHooks;
import net.labymod.addons.modcompat.v1_8_9.skyblockaddons.configuration.SkyblockAddonsHookConfiguration;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.configuration.settings.type.RootSettingRegistry;

public class SkyblockAddonsCompat {

  public static final String ADDON_ID = "skyblockaddons_loader";

  public static final HudWidgetCategory SKYBLOCK_ADDONS_CATEGORY =
      new HudWidgetCategory("skyblockaddons");

  public static boolean isFeatureIntegration() {
    SkyblockAddonsHookConfiguration config = AddonHooks.instance()
        .getSubSettings(SkyblockAddonsHookConfiguration.class);
    return config != null && config.featureIntegration().get();
  }

  public static void addonSettings(Consumer<RootSettingRegistry> consumer) {
    RootSettingRegistry registry = AddonHooks.instance().getAddonSettings(ADDON_ID);
    if (registry != null) {
      consumer.accept(registry);
    }
  }
}
