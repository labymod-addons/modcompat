package net.labymod.addons.modcompat.replaymod.launch;

import net.labymod.addons.modcompat.hook.AddonHooks;
import net.labymod.addons.modcompat.mod.fix.ModFixEntrypoint;
import net.labymod.addons.modcompat.replaymod.listener.IngameMenuListener;
import net.labymod.addons.modcompat.replaymod.listener.IngameOverlayListener;
import net.labymod.addons.modcompat.replaymod.listener.MainMenuListener;
import net.labymod.addons.modcompat.replaymod.settings.ReplayModSettingsConverter;
import net.labymod.addons.modcompat.replaymod.settings.config.ReplayModHookConfig;
import net.labymod.api.Laby;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.settings.Setting;
import net.labymod.api.configuration.settings.SettingHandler;
import net.labymod.api.configuration.settings.type.RootSettingRegistry;
import net.labymod.api.models.addon.annotation.AddonEntryPoint;
import net.labymod.api.models.addon.annotation.AddonEntryPoint.Point;
import net.labymod.api.models.version.Version;

@AddonEntryPoint(value = Point.ENABLE)
public class ReplayModEnableEntrypoint extends ModFixEntrypoint {

  public static final String MOD_ID = "replaymod";

  public ReplayModEnableEntrypoint() {
    super(MOD_ID);
  }

  @Override
  public void initialize(Version version) {
    if (!super.isModLoaded()) {
      return;
    }

    Laby.labyAPI().eventBus().registerListener(new IngameMenuListener());
    Laby.labyAPI().eventBus().registerListener(new MainMenuListener());
    Laby.labyAPI().eventBus().registerListener(new IngameOverlayListener());

    RootSettingRegistry addonSettings = AddonHooks.instance().getAddonSettings(MOD_ID);
    if (addonSettings == null) {
      return;
    }

    ReplayModSettingsConverter converter = new ReplayModSettingsConverter();

    Config config = AddonHooks.instance().registerSubSettings(MOD_ID, ReplayModHookConfig.class);
    addonSettings.addSettings(converter.convertCoreSettings(config));

    addonSettings.findSetting((CharSequence) "openReplayViewer")
        .ifPresent(setting -> setting.asElement().setHandler(new SettingHandler() {
          @Override
          public void created(Setting setting) {
          }

          @Override
          public void initialized(Setting setting) {
          }

          @Override
          public boolean isEnabled(Setting setting) {
            return !Laby.labyAPI().minecraft().isIngame();
          }
        }));
  }
}
