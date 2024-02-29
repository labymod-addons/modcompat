package net.labymod.addons.modcompat.replaymod.settings;

import com.replaymod.core.SettingsRegistry.SettingKey;
import net.labymod.api.client.component.Component;
import net.labymod.api.configuration.settings.type.SettingElement;

public class ReplayModSettingElement extends SettingElement {

  private final SettingKey<?> settingKey;

  public ReplayModSettingElement(SettingKey<?> settingKey) {
    super(settingKey.getKey(), null, null, new String[0]);
    this.settingKey = settingKey;
  }

  @Override
  public Component displayName() {
    return Component.translatable(this.settingKey.getDisplayString());
  }
}
