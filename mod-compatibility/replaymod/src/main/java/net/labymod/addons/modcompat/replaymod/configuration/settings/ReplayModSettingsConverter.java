package net.labymod.addons.modcompat.replaymod.configuration.settings;

import com.replaymod.core.ReplayMod;
import com.replaymod.core.SettingsRegistry.MultipleChoiceSettingKey;
import com.replaymod.core.SettingsRegistry.SettingKey;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import net.labymod.addons.modcompat.configuration.settings.CustomNameSettingElement;
import net.labymod.addons.modcompat.configuration.settings.SettingAnnotationCreator;
import net.labymod.addons.modcompat.replaymod.configuration.settings.widget.ReplayModEntryRenderer;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.renderer.EntryRenderer;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.settings.Setting;
import net.labymod.api.configuration.settings.SettingInfo;
import net.labymod.api.configuration.settings.accessor.SettingAccessor;
import net.labymod.api.configuration.settings.type.SettingElement;

public class ReplayModSettingsConverter {

  private final EntryRenderer<Object> REPLAY_MOD_ENTRY_RENDERER = new ReplayModEntryRenderer<>();

  @SuppressWarnings("unchecked")
  public List<Setting> convertCoreSettings(Config config) {
    List<Setting> settings = new ArrayList<>();

    var settingsRegistry = ReplayMod.instance.getSettingsRegistry();
    for (SettingKey<?> key : settingsRegistry.getSettings()) {
      if (key.getDisplayString() == null) {
        continue;
      }

      Annotation annotation;
      if (key.getDefault() instanceof Boolean) {
        annotation = SettingAnnotationCreator.createSwitch(false);
      } else if (key instanceof MultipleChoiceSettingKey<?>) {
        annotation = SettingAnnotationCreator.createDropdown();
      } else {
        continue;
      }

      SettingElement element = new CustomNameSettingElement(
          key.getKey(),
          () -> Component.translatable(key.getDisplayString())
      );

      SettingInfo<?> settingInfo = new SettingInfo<>(config, null);
      SettingAccessor accessor = new ReplayModSettingAccessor(
          (SettingKey<Object>) key, element, config
      );

      Widget[] widgets = Laby.labyAPI().widgetRegistry()
          .createWidgets(element, annotation, settingInfo, accessor);

      // Special handling for dropdowns
      if (widgets.length > 0
          && widgets[0] instanceof DropdownWidget<?> dropdownWidget
          && key instanceof MultipleChoiceSettingKey<?> choiceKey
      ) {
        for (Object choice : choiceKey.getChoices()) {
          ((DropdownWidget<Object>) dropdownWidget).add(choice);
        }
        ((DropdownWidget<Object>) dropdownWidget).setEntryRenderer(REPLAY_MOD_ENTRY_RENDERER);
      }

      element.setWidgets(widgets);
      element.setAccessor(accessor);

      settings.add(element);
    }

    return settings;
  }
}
