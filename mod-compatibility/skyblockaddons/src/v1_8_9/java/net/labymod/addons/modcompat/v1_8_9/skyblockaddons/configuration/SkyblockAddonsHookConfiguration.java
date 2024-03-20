package net.labymod.addons.modcompat.v1_8_9.skyblockaddons.configuration;

import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingSection;

@ConfigName("skyblockaddons")
public class SkyblockAddonsHookConfiguration extends Config {

  @SettingSection("integration")
  @SwitchSetting
  private final ConfigProperty<Boolean> featureIntegration = new ConfigProperty<>(true);

  @SettingSection("general")
  private final ConfigProperty<Object> dummy = new ConfigProperty<>(new Object());

  public ConfigProperty<Boolean> featureIntegration() {
    return this.featureIntegration;
  }
}
