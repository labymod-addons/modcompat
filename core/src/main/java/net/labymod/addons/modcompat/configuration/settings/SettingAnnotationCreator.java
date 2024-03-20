package net.labymod.addons.modcompat.configuration.settings;

import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.color.ColorPickerWidget.ColorPickerSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;

public class SettingAnnotationCreator {

  public static SwitchSetting createSwitch(boolean hotkey) {
    return new SwitchSetting() {
      @Override
      public boolean hotkey() {
        return hotkey;
      }

      @Override
      public Class<? extends SwitchSetting> annotationType() {
        return SwitchSetting.class;
      }
    };
  }

  public static DropdownSetting createDropdown() {
    return new DropdownSetting() {
      @Override
      public Class<? extends DropdownSetting> annotationType() {
        return DropdownSetting.class;
      }
    };
  }

  public static SliderSetting createSlider(float steps, float min, float max) {
    return new SliderSetting() {
      @Override
      public float steps() {
        return steps;
      }

      @Override
      public float min() {
        return min;
      }

      @Override
      public float max() {
        return max;
      }

      @Override
      public Class<? extends SliderSetting> annotationType() {
        return SliderSetting.class;
      }
    };
  }

  public static ColorPickerSetting createColorPicker(
      boolean alpha, boolean chroma, boolean chromaSpeed
  ) {
    return new ColorPickerSetting() {
      @Override
      public boolean alpha() {
        return alpha;
      }

      @Override
      public boolean removeAlpha() {
        return !alpha;
      }

      @Override
      public boolean chroma() {
        return chroma;
      }

      @Override
      public boolean chromaSpeed() {
        return chromaSpeed;
      }

      @Override
      public boolean removeChromaSpeed() {
        return !chromaSpeed;
      }

      @Override
      public Class<? extends ColorPickerSetting> annotationType() {
        return ColorPickerSetting.class;
      }
    };
  }
}
