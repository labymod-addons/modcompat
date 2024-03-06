package net.labymod.addons.modcompat.replaymod.launch;

import net.labymod.addons.modcompat.hook.AddonHooks;
import net.labymod.addons.modcompat.mod.fix.ModFixEntrypoint;
import net.labymod.addons.modcompat.replaymod.ReplayMultiplayerNavigationElement;
import net.labymod.addons.modcompat.replaymod.listener.IngameMenuListener;
import net.labymod.addons.modcompat.replaymod.listener.MainMenuListener;
import net.labymod.addons.modcompat.replaymod.listener.ReplayModScreenListener;
import net.labymod.addons.modcompat.replaymod.listener.ReplayViewListener;
import net.labymod.addons.modcompat.replaymod.settings.ReplayModSettingsConverter;
import net.labymod.addons.modcompat.replaymod.settings.config.ReplayModHookConfig;
import net.labymod.api.Laby;
import net.labymod.api.LabyAPI;
import net.labymod.api.client.gui.navigation.NavigationRegistry;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.settings.type.RootSettingRegistry;
import net.labymod.api.event.EventBus;
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

    LabyAPI labyAPI = Laby.labyAPI();

    EventBus eventBus = labyAPI.eventBus();
    eventBus.registerListener(new IngameMenuListener());
    eventBus.registerListener(new MainMenuListener());
    eventBus.registerListener(new ReplayViewListener());
    eventBus.registerListener(new ReplayModScreenListener());

    // Replace multiplayer tab with tab that hides when in a replay
    NavigationRegistry navigationRegistry = labyAPI.navigationService();
    navigationRegistry.unregister("multiplayer");
    navigationRegistry.registerAfter(
        "singleplayer",
        "multiplayer",
        new ReplayMultiplayerNavigationElement()
    );

    // Hook into addon settings, if present
    RootSettingRegistry addonSettings = AddonHooks.instance().getAddonSettings(MOD_ID);
    if (addonSettings != null) {
      ReplayModSettingsConverter converter = new ReplayModSettingsConverter();

      Config config = AddonHooks.instance().registerSubSettings(MOD_ID, ReplayModHookConfig.class);
      addonSettings.addSettings(converter.convertCoreSettings(config));

      // Replay viewer should only be opened when not ingame to prevent issues
      addonSettings.findSetting((CharSequence) "openReplayViewer")
          .ifPresent(setting -> setting.asElement().setVisibleSupplier(
              () -> !Laby.labyAPI().minecraft().isIngame())
          );
    }
  }
}
