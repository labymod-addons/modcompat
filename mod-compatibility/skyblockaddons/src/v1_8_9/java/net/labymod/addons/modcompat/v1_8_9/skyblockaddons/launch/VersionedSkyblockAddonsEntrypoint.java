package net.labymod.addons.modcompat.v1_8_9.skyblockaddons.launch;

import net.labymod.addons.modcompat.hook.AddonHooks;
import net.labymod.addons.modcompat.mod.fix.ModFixEntrypoint;
import net.labymod.addons.modcompat.v1_8_9.skyblockaddons.SkyblockAddonsCompat;
import net.labymod.addons.modcompat.v1_8_9.skyblockaddons.SkyblockAddonsFeatureSync;
import net.labymod.addons.modcompat.v1_8_9.skyblockaddons.configuration.SkyblockAddonsHookConfiguration;
import net.labymod.addons.modcompat.v1_8_9.skyblockaddons.configuration.settings.SkyblockAddonsGeneralSettings;
import net.labymod.api.Laby;
import net.labymod.api.configuration.settings.Setting;
import net.labymod.api.configuration.settings.type.AbstractSetting;
import net.labymod.api.models.addon.annotation.AddonEntryPoint;
import net.labymod.api.models.addon.annotation.AddonEntryPoint.Point;
import net.labymod.api.models.version.Version;

@AddonEntryPoint(Point.ENABLE)
public class VersionedSkyblockAddonsEntrypoint extends ModFixEntrypoint {

  public VersionedSkyblockAddonsEntrypoint() {
    super("skyblockaddons");
  }

  @Override
  public void initialize(Version version) {
    if (!super.isModLoaded()) {
      return;
    }

    SkyblockAddonsFeatureSync featureSync = new SkyblockAddonsFeatureSync();
    Laby.labyAPI().eventBus().registerListener(featureSync);

    SkyblockAddonsHookConfiguration config = AddonHooks.instance()
        .registerSubSettings(SkyblockAddonsCompat.ADDON_ID, SkyblockAddonsHookConfiguration.class);
    if (config != null) {
      if (config.featureIntegration().get()) {
        featureSync.registerHudWidgets();
      }

      config.featureIntegration().addChangeListener(value -> {
        if (value) {
          featureSync.registerHudWidgets();
        } else {
          featureSync.unregisterHudWidgets();
        }
      });

      SkyblockAddonsCompat.addonSettings(settings -> {
        for (Setting generalSetting : SkyblockAddonsGeneralSettings.create(config)) {
          if (!(generalSetting instanceof AbstractSetting setting)) {
            continue;
          }
          setting.setParent(settings);
          settings.registerBefore("integration", setting);
        }
      });
    }
  }
}
