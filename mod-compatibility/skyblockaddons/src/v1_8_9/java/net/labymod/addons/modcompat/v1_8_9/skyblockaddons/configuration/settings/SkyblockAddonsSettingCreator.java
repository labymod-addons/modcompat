package net.labymod.addons.modcompat.v1_8_9.skyblockaddons.configuration.settings;

import codes.biscuit.skyblockaddons.core.Feature;
import codes.biscuit.skyblockaddons.utils.EnumUtils.FeatureSetting;
import java.lang.annotation.Annotation;
import net.labymod.addons.modcompat.configuration.settings.CustomNameSettingElement;
import net.labymod.addons.modcompat.v1_8_9.skyblockaddons.configuration.settings.SkyblockAddonsSettingAccessor.ValueGetter;
import net.labymod.addons.modcompat.v1_8_9.skyblockaddons.configuration.settings.SkyblockAddonsSettingAccessor.ValueSetter;
import net.labymod.addons.modcompat.v1_8_9.skyblockaddons.configuration.settings.widget.SkyblockAddonsEntryRenderer;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.renderer.EntryRenderer;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.settings.Setting;
import net.labymod.api.configuration.settings.SettingInfo;
import net.labymod.api.configuration.settings.type.SettingElement;

public class SkyblockAddonsSettingCreator {

  private static final EntryRenderer<Object> ENTRY_RENDERER = new SkyblockAddonsEntryRenderer<>();

  public static <T> Setting create(
      Feature feature,
      Config config,
      ValueGetter<T> valueGetter,
      ValueSetter<T> valueSetter,
      T defaultValue,
      Annotation settingAnnotation
  ) {
    var setting = new CustomNameSettingElement(
        String.valueOf(feature.getId()),
        () -> Component.text(feature.getMessage())
    );
    initSetting(setting, config, valueGetter, valueSetter, defaultValue, settingAnnotation);
    return setting;
  }

  public static <T> Setting create(
      FeatureSetting featureSetting,
      Config config,
      ValueGetter<T> valueGetter,
      ValueSetter<T> valueSetter,
      T defaultValue,
      Annotation settingAnnotation
  ) {
    var setting = new CustomNameSettingElement(
        featureSetting.name(),
        () -> Component.text(featureSetting.getMessage())
    );
    initSetting(setting, config, valueGetter, valueSetter, defaultValue, settingAnnotation);
    return setting;
  }

  private static <T> void initSetting(
      SettingElement setting,
      Config config,
      ValueGetter<T> valueGetter,
      ValueSetter<T> valueSetter,
      T defaultValue,
      Annotation settingAnnotation
  ) {
    SettingInfo<?> settingInfo = new SettingInfo<>(config, null);
    var accessor = new SkyblockAddonsSettingAccessor<>(
        config,
        setting,
        valueGetter,
        valueSetter,
        defaultValue
    );

    Widget[] widgets = Laby.labyAPI().widgetRegistry()
        .createWidgets(setting, settingAnnotation, settingInfo, accessor);

    // Special handling for dropdowns
    if (widgets.length > 0 && widgets[0] instanceof DropdownWidget<?> dropdownWidget) {
      ((DropdownWidget<Object>) dropdownWidget).setEntryRenderer(ENTRY_RENDERER);
    }

    setting.setWidgets(widgets);
    setting.setAccessor(accessor);
  }
}
