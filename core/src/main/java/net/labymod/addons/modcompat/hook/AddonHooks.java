package net.labymod.addons.modcompat.hook;

import java.util.HashMap;
import java.util.Map;
import net.labymod.addons.modcompat.ModCompatAddon;
import net.labymod.api.Laby;
import net.labymod.api.configuration.loader.ConfigAccessor;
import net.labymod.api.configuration.loader.impl.JsonConfigLoader;
import net.labymod.api.configuration.settings.Setting;
import net.labymod.api.configuration.settings.type.RootSettingRegistry;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.labymod.config.ConfigurationSaveEvent;
import net.labymod.core.addon.DefaultAddonService;
import org.jetbrains.annotations.Nullable;

public class AddonHooks {

  private static final AddonHooks INSTANCE = new AddonHooks();

  private final Map<String, RootSettingRegistry> addonSettingsCache;
  private final Map<Class<? extends ConfigAccessor>, ModCompatConfigProvider<? extends ConfigAccessor>> configProviders;

  public AddonHooks() {
    this.addonSettingsCache = new HashMap<>();
    this.configProviders = new HashMap<>();
  }

  public static AddonHooks instance() {
    return INSTANCE;
  }

  @Subscribe
  public void onConfigurationSave(ConfigurationSaveEvent event) {
    for (ModCompatConfigProvider<? extends ConfigAccessor> provider : this.configProviders.values()) {
      provider.safeSave();
    }
  }

  public <C extends ConfigAccessor> C registerSubSettings(String addonId, Class<C> configClass) {
    RootSettingRegistry settings = this.getAddonSettings(addonId);
    if (settings == null) {
      return null;
    }

    C subSettings = this.getSubSettings(configClass);
    if (subSettings != null) {
      return subSettings;
    }

    var provider = new ModCompatConfigProvider<>(configClass);
    this.configProviders.put(configClass, provider);

    C config = provider.safeLoad(JsonConfigLoader.createDefault());
    settings.addSettings(config);

    return config;
  }

  public <C extends ConfigAccessor> C getSubSettings(Class<C> configClass) {
    if (this.configProviders.containsKey(configClass)) {
      var provider = this.configProviders.get(configClass);
      if (provider.getType() == configClass) {
        //noinspection unchecked
        return (C) provider.get();
      }
      throw new IllegalArgumentException("Config class mismatch");
    }
    return null;
  }

  public @Nullable RootSettingRegistry getAddonSettings(String addonId) {
    if (DefaultAddonService.getInstance().getAddon(addonId).isEmpty()) {
      // Addon is not loaded, which means no settings are available
      if (!Laby.labyAPI().labyModLoader().isAddonDevelopmentEnvironment()) {
        return null;
      }
      // In development environment, use the mod compat settings so that mod addons are not required
      addonId = ModCompatAddon.NAMESPACE;
    }

    return this.addonSettingsCache.computeIfAbsent(addonId, id -> {
      for (Setting setting : Laby.labyAPI().coreSettingRegistry().values()) {
        if (setting instanceof RootSettingRegistry settingRegistry
            && settingRegistry.isAddon()
            && settingRegistry.getNamespace().equals(id)) {
          return settingRegistry;
        }
      }
      return null;
    });
  }
}
