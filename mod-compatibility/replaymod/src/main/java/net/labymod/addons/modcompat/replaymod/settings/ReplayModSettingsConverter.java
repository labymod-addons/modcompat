package net.labymod.addons.modcompat.replaymod.settings;

import com.replaymod.core.ReplayMod;
import com.replaymod.core.SettingsRegistry.MultipleChoiceSettingKey;
import com.replaymod.core.SettingsRegistry.SettingKey;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.settings.Setting;
import net.labymod.api.configuration.settings.SettingInfo;
import net.labymod.api.configuration.settings.accessor.SettingAccessor;
import net.labymod.api.configuration.settings.type.SettingElement;

public class ReplayModSettingsConverter {

  public List<Setting> convertCoreSettings(Config config) {
    List<Setting> settings = new ArrayList<>();

    var settingsRegistry = ReplayMod.instance.getSettingsRegistry();
    for (SettingKey<?> key : settingsRegistry.getSettings()) {
      if (key.getDisplayString() == null) {
        continue;
      }

      Annotation annotation;
      if (key.getDefault() instanceof Boolean) {
        annotation = new SwitchSetting() {
          @Override
          public boolean hotkey() {
            return false;
          }

          @Override
          public Class<? extends Annotation> annotationType() {
            return SwitchSetting.class;
          }
        };
      } else if (key instanceof MultipleChoiceSettingKey<?>) {
        annotation = new DropdownSetting() {

          @Override
          public Class<? extends Annotation> annotationType() {
            return DropdownSetting.class;
          }
        };
      } else {
        continue;
      }

      // TODO: Fix translations
      SettingElement element = new SettingElement(
          key.getKey(),
          null,
          key.getDisplayString(),
          new String[0]
      );

      SettingInfo<?> settingInfo = new SettingInfo<>(config, null);
      SettingAccessor accessor = new ReplayModSettingAccessor(
          (SettingKey<Object>) key, element, config
      );

      Widget[] widgets = Laby.labyAPI().widgetRegistry()
          .createWidgets(element, annotation, settingInfo, accessor);

      if (widgets[0] instanceof DropdownWidget<?> dropdownWidget) {
        for (Object choice : ((MultipleChoiceSettingKey<?>) key).getChoices()) {
          ((DropdownWidget<Object>) dropdownWidget).add(choice);
        }
        dropdownWidget.setEntryRenderer(new ReplayModEntryRenderer<>());
      }

      element.setWidgets(widgets);
      element.setAccessor(accessor);

      settings.add(element);
    }

    return settings;
  }
}
