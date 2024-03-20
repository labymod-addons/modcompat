package net.labymod.addons.modcompat.v1_8_9.skyblockaddons.configuration.settings;

import codes.biscuit.skyblockaddons.SkyblockAddons;
import codes.biscuit.skyblockaddons.config.ConfigValues;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.accessor.SettingAccessor;
import net.labymod.api.configuration.settings.type.SettingElement;
import net.labymod.api.util.reflection.Reflection;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unchecked")
public class SkyblockAddonsSettingAccessor<T> implements SettingAccessor {

  private static final Field DUMMY_FIELD = Reflection.getField(
      SkyblockAddonsSettingAccessor.class,
      "configValues"
  );

  private final ConfigValues configValues = SkyblockAddons.getInstance().getConfigValues();

  private final Config config;
  private final SettingElement setting;
  private final ValueGetter<T> valueGetter;
  private final ValueSetter<T> valueSetter;

  private final Class<T> type;
  private final ConfigProperty<T> property;

  public SkyblockAddonsSettingAccessor(
      Config config,
      SettingElement setting,
      ValueGetter<T> valueGetter,
      ValueSetter<T> valueSetter,
      T defaultValue
  ) {
    this.config = config;
    this.setting = setting;
    this.valueGetter = valueGetter;
    this.valueSetter = valueSetter;

    this.type = (Class<T>) defaultValue.getClass();
    this.property = new ConfigProperty<>(this.get());
    this.property.updateDefaultValue(defaultValue);
  }

  @Override
  public Class<?> getType() {
    return this.type;
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
  public <K> void set(K value) {
    this.valueSetter.set(this.configValues, (T) value);
    this.property.set((T) value);
  }

  @Override
  public <K> K get() {
    return (K) this.valueGetter.get(this.configValues);
  }

  @Override
  public <K> ConfigProperty<K> property() {
    this.property.set(this.get());
    return (ConfigProperty<K>) this.property;
  }

  @Override
  public SettingElement setting() {
    return this.setting;
  }

  public interface ValueSetter<T> {

    void set(ConfigValues configValues, T value);
  }

  public interface ValueGetter<T> {

    T get(ConfigValues configValues);
  }
}
