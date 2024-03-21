package net.labymod.addons.modcompat.v1_8_9.skyblockaddons.configuration.settings;

import codes.biscuit.skyblockaddons.config.ConfigValues;
import codes.biscuit.skyblockaddons.core.Feature;
import codes.biscuit.skyblockaddons.utils.EnumUtils.ChromaMode;
import codes.biscuit.skyblockaddons.utils.EnumUtils.TextStyle;
import java.util.List;
import net.labymod.addons.modcompat.configuration.settings.SettingAnnotationCreator;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.settings.Setting;

public class SkyblockAddonsGeneralSettings {

  public static List<Setting> create(Config config) {
    return List.of(
        SkyblockAddonsSettingCreator.create(
            Feature.TEXT_STYLE,
            config,
            ConfigValues::getTextStyle,
            ConfigValues::setTextStyle,
            TextStyle.STYLE_ONE,
            SettingAnnotationCreator.createDropdown()
        ),
        SkyblockAddonsSettingCreator.create(
            Feature.WARNING_TIME,
            config,
            ConfigValues::getWarningSeconds,
            ConfigValues::setWarningSeconds,
            4,
            SettingAnnotationCreator.createSlider(1.0F, 1.0F, 99.0F)
        ),
        SkyblockAddonsSettingCreator.create(
            Feature.CHROMA_SPEED,
            config,
            configValues -> configValues.getChromaSpeed().getValue(),
            (configValues, value) -> configValues.getChromaSpeed().setValue(value),
            6.0F,
            SettingAnnotationCreator.createSlider(0.5F, 0.5F, 20.0F)
        ),
        SkyblockAddonsSettingCreator.create(
            Feature.CHROMA_MODE,
            config,
            ConfigValues::getChromaMode,
            ConfigValues::setChromaMode,
            ChromaMode.FADE,
            SettingAnnotationCreator.createDropdown()
        ),
        SkyblockAddonsSettingCreator.create(
            Feature.CHROMA_SIZE,
            config,
            configValues -> configValues.getChromaSize().getValue(),
            (configValues, value) -> configValues.getChromaSize().setValue(value),
            30.0F,
            SettingAnnotationCreator.createSlider(1.0F, 1.0F, 100.0F)
        ),
        SkyblockAddonsSettingCreator.create(
            Feature.TURN_ALL_FEATURES_CHROMA,
            config,
            configValues -> {
              for (Feature feature : Feature.values()) {
                if (feature.getGuiFeatureData() != null
                    && feature.getGuiFeatureData().getDefaultColor() != null
                    && !configValues.getChromaFeatures().contains(feature)) {
                  return false;
                }
              }
              return true;
            },
            (configValues, value) -> {
              for (Feature feature : Feature.values()) {
                if (feature.getGuiFeatureData() != null
                    && feature.getGuiFeatureData().getDefaultColor() != null) {
                  if (value) {
                    configValues.getChromaFeatures().add(feature);
                  } else {
                    configValues.getChromaFeatures().remove(feature);
                  }
                }
              }
            },
            false,
            SettingAnnotationCreator.createSwitch(false)
        ),
        SkyblockAddonsSettingCreator.create(
            Feature.CHROMA_SATURATION,
            config,
            configValues -> configValues.getChromaSaturation().getValue(),
            (configValues, value) -> configValues.getChromaSaturation().setValue(value),
            0.75F,
            SettingAnnotationCreator.createSlider(0.01F, 0.0F, 1.0F)
        ),
        SkyblockAddonsSettingCreator.create(
            Feature.CHROMA_BRIGHTNESS,
            config,
            configValues -> configValues.getChromaBrightness().getValue(),
            (configValues, value) -> configValues.getChromaBrightness().setValue(value),
            0.9F,
            SettingAnnotationCreator.createSlider(0.01F, 0.0F, 1.0F)
        ),
        SkyblockAddonsSettingCreator.create(
            Feature.USE_NEW_CHROMA_EFFECT,
            config,
            configValues -> Feature.USE_NEW_CHROMA_EFFECT.isEnabled(),
            (configValues, value) -> Feature.USE_NEW_CHROMA_EFFECT.setEnabled(value),
            true,
            SettingAnnotationCreator.createSwitch(false)
        )
    );
  }
}
