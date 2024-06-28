package net.labymod.addons.modcompat.modmenu.launch;

import net.labymod.addons.modcompat.ModCompat;
import net.labymod.addons.modcompat.core.generated.DefaultReferenceStorage;
import net.labymod.addons.modcompat.hook.AddonHooks;
import net.labymod.addons.modcompat.mod.fix.ModFixEntrypoint;
import net.labymod.addons.modcompat.modmenu.ModMenuButtonTextureMapper;
import net.labymod.addons.modcompat.modmenu.configuration.ModMenuHookConfiguration;
import net.labymod.api.Laby;
import net.labymod.api.configuration.settings.Setting;
import net.labymod.api.configuration.settings.SettingHandler;
import net.labymod.api.configuration.settings.type.RootSettingRegistry;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.addon.lifecycle.GlobalAddonPostEnableEvent;
import net.labymod.api.models.addon.annotation.AddonEntryPoint;
import net.labymod.api.models.addon.annotation.AddonEntryPoint.Point;
import net.labymod.api.models.version.Version;

@AddonEntryPoint(Point.LOAD)
public class ModMenuEntrypoint extends ModFixEntrypoint {

  public static final String MOD_ID = "modmenu";
  private static final String LABYFABRIC_ADDON_ID = "labyfabric";

  public ModMenuEntrypoint() {
    super(MOD_ID);
  }

  @Override
  public void initialize(Version version) {
    if (super.isModLoaded()) {
      Laby.labyAPI().eventBus().registerListener(new ModMenuButtonTextureMapper());
    }
    Laby.labyAPI().eventBus().registerListener(this);
  }

  @Subscribe
  public void registerModMenuSetting(GlobalAddonPostEnableEvent event) {
    if (!LABYFABRIC_ADDON_ID.equals(event.addonInfo().getNamespace())) {
      return;
    }

    RootSettingRegistry addonSettings = AddonHooks.instance().getAddonSettings(LABYFABRIC_ADDON_ID);
    if (addonSettings != null) {
      boolean modMenuAvailable = super.isModLoaded()
          && ModCompat.instance().references() instanceof DefaultReferenceStorage references
          && references.getModMenuAccessor() != null;

      AddonHooks.instance()
          .registerSubSettings(LABYFABRIC_ADDON_ID, ModMenuHookConfiguration.class);

      addonSettings.findSetting((CharSequence) "openModMenu").ifPresent(
          setting ->
              setting.asElement().setHandler(new SettingHandler() {
                @Override
                public void created(Setting setting) {
                }

                @Override
                public void initialized(Setting setting) {
                }

                @Override
                public boolean isEnabled(Setting setting) {
                  return modMenuAvailable;
                }
              })
      );
    }
  }
}
