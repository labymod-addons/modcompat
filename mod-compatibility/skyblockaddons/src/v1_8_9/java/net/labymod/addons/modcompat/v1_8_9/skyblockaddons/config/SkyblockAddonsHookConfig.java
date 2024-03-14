package net.labymod.addons.modcompat.v1_8_9.skyblockaddons.config;

import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.property.ConfigProperty;

@ConfigName("skyblockaddons")
public class SkyblockAddonsHookConfig extends Config {

  @SwitchSetting
  private final ConfigProperty<Boolean> featureIntegration = new ConfigProperty<>(true);

  public ConfigProperty<Boolean> featureIntegration() {
    return this.featureIntegration;
  }
}
