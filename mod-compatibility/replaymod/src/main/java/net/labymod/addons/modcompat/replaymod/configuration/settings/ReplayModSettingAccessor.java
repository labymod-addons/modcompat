package net.labymod.addons.modcompat.replaymod.configuration.settings;

import com.replaymod.core.ReplayMod;
import com.replaymod.core.SettingsRegistry.SettingKey;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.accessor.SettingAccessor;
import net.labymod.api.configuration.settings.type.SettingElement;
import net.labymod.api.util.reflection.Reflection;
import org.jetbrains.annotations.Nullable;

public class ReplayModSettingAccessor implements SettingAccessor {

  private static final Field DUMMY_FIELD = Reflection.getField(
      ReplayModSettingAccessor.class,
      "settingKey"
  );

  private final SettingKey<Object> settingKey;
  private final SettingElement setting;
  private final Config config;

  private final ConfigProperty<Object> property;

  public ReplayModSettingAccessor(
      SettingKey<Object> settingKey,
      SettingElement setting,
      Config config
  ) {
    this.settingKey = settingKey;
    this.setting = setting;
    this.config = config;

    this.property = new ConfigProperty<>(this.get());
    this.property.updateDefaultValue(this.settingKey.getDefault());
  }

  @Override
  public Class<?> getType() {
    return this.settingKey.getDefault().getClass();
  }

  @Override
  public @Nullable Type getGenericType() {
    return null;
  }

  @Override
  public Field getField() {
    return DUMMY_FIELD;
  }

  @Override
  public Config config() {
    return this.config;
  }

  @Override
  public <T> void set(T t) {
    var registry = ReplayMod.instance.getSettingsRegistry();
    registry.set(this.settingKey, t);
    registry.save();
    this.property.set(t);
  }

  @Override
  public <T> T get() {
    return (T) ReplayMod.instance.getSettingsRegistry().get(this.settingKey);
  }

  @Override
  public <T> ConfigProperty<T> property() {
    this.property.set(this.get());
    return (ConfigProperty<T>) this.property;
  }

  @Override
  public SettingElement setting() {
    return this.setting;
  }
}
