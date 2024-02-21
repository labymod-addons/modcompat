package net.labymod.addons.modcompat.v1_8_9.skyblockaddons;

import codes.biscuit.skyblockaddons.SkyblockAddons;
import codes.biscuit.skyblockaddons.config.ConfigValues;
import codes.biscuit.skyblockaddons.core.Feature;
import codes.biscuit.skyblockaddons.utils.ColorCode;
import net.labymod.addons.modcompat.v1_8_9.skyblockaddons.accessor.ConfigValuesAccessor;
import net.labymod.addons.modcompat.v1_8_9.skyblockaddons.hud.SkyblockAddonsHudWidget;
import net.labymod.addons.modcompat.v1_8_9.skyblockaddons.hud.SkyblockAddonsHudWidget.SkyblockAddonsHudWidgetConfig;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.client.gui.hud.hudwidget.HudWidget;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.lifecycle.GameTickEvent;

public class SkyblockAddonsFeatureSync {

  public static final HudWidgetCategory SKYBLOCK_ADDONS_CATEGORY =
      new HudWidgetCategory("skyblockaddons");

  private final SkyblockAddons main = SkyblockAddons.getInstance();

  public SkyblockAddonsFeatureSync() {
    Laby.labyAPI().hudWidgetRegistry().categoryRegistry().register(SKYBLOCK_ADDONS_CATEGORY);

    for (Feature guiFeature : Feature.getGuiFeatures()) {
      if (guiFeature.getGuiFeatureData() == null
          || guiFeature.getGuiFeatureData().getDrawType() == null) {
        continue;
      }
      HudWidget<?> hudWidget = new SkyblockAddonsHudWidget(guiFeature);
      Laby.labyAPI().hudWidgetRegistry().register(hudWidget);
    }
  }

  @Subscribe
  public void onTick(GameTickEvent event) {
    for (HudWidget<?> hudWidget : Laby.labyAPI().hudWidgetRegistry().values()) {
      if (!(hudWidget instanceof SkyblockAddonsHudWidget skyblockAddonsHudWidget)) {
        continue;
      }

      SkyblockAddonsHudWidgetConfig config = skyblockAddonsHudWidget.getConfig();
      if (config == null) {
        continue;
      }

      Feature feature = skyblockAddonsHudWidget.feature();

      // Sync SkyblockAddons feature enabled state with the hud widget
      config.setEnabled(feature.isEnabled());

      // TODO: There is a problem with displaying the correct color in the editor
      // Sync color
      ColorCode defaultColor = feature.getDefaultColor();
      if (defaultColor != null) {
        ConfigValues configValues = this.main.getConfigValues();

        Integer color = ((ConfigValuesAccessor) configValues).getConfigColor(feature);
        config.color().set(color == null ? defaultColor.getColor() : color);

        config.chroma().set(configValues.getChromaFeatures().contains(feature));
      }
    }
  }
}
