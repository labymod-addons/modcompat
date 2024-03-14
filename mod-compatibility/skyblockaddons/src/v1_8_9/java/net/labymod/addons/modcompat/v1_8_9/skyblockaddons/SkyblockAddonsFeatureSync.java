package net.labymod.addons.modcompat.v1_8_9.skyblockaddons;

import codes.biscuit.skyblockaddons.SkyblockAddons;
import codes.biscuit.skyblockaddons.config.ConfigValues;
import codes.biscuit.skyblockaddons.core.Feature;
import codes.biscuit.skyblockaddons.utils.ColorCode;
import net.labymod.addons.modcompat.v1_8_9.skyblockaddons.accessor.ConfigValuesAccessor;
import net.labymod.addons.modcompat.v1_8_9.skyblockaddons.hud.SkyblockAddonsHudWidget;
import net.labymod.addons.modcompat.v1_8_9.skyblockaddons.hud.SkyblockAddonsHudWidget.SkyblockAddonsHudWidgetConfig;
import net.labymod.api.Laby;
import net.labymod.api.LabyAPI;
import net.labymod.api.client.gui.hud.HudWidgetRegistry;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.client.gui.hud.hudwidget.HudWidget;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.widget.widgets.navigation.tab.Tab;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.lifecycle.GameTickEvent;
import net.labymod.api.util.Color;
import net.labymod.api.util.KeyValue;
import net.labymod.core.client.gui.hud.overlay.HudWidgetOverlay;
import net.labymod.core.client.gui.screen.activity.activities.labymod.LabyModActivity;
import net.labymod.core.client.gui.screen.activity.activities.labymod.child.WidgetsEditorActivity;

public class SkyblockAddonsFeatureSync {

  public static final HudWidgetCategory SKYBLOCK_ADDONS_CATEGORY =
      new HudWidgetCategory("skyblockaddons");

  private final SkyblockAddons main = SkyblockAddons.getInstance();

  public SkyblockAddonsFeatureSync() {
    Laby.labyAPI().hudWidgetRegistry().categoryRegistry().register(SKYBLOCK_ADDONS_CATEGORY);
  }

  public void registerHudWidgets() {
    HudWidgetRegistry hudWidgetRegistry = Laby.labyAPI().hudWidgetRegistry();

    for (Feature guiFeature : Feature.getGuiFeatures()) {
      if (guiFeature.getGuiFeatureData() == null
          || guiFeature.getGuiFeatureData().getDrawType() == null) {
        continue;
      }

      HudWidget<?> hudWidget = new SkyblockAddonsHudWidget(guiFeature);
      if (hudWidgetRegistry.getById(hudWidget.getId()) == null) {
        hudWidgetRegistry.register(hudWidget);
      }
    }
  }

  public void unregisterHudWidgets() {
    LabyAPI labyAPI = Laby.labyAPI();

    labyAPI.hudWidgetRegistry()
        .unregister(value -> value.getValue() instanceof SkyblockAddonsHudWidget);
    labyAPI.ingameOverlay().getActivity(HudWidgetOverlay.class).ifPresent(Activity::reload);

    // TODO: This fixes that unregistered widgets can still be selected in the editor. Should probably be fixed in LabyMod instead
    LabyModActivity labyModActivity = LabyModActivity.getFromNavigationRegistry();
    if (labyModActivity != null) {
      for (KeyValue<Tab> element : labyModActivity.getElements()) {
        if (element.getKey().equals("widgets")) {
          if (element.getValue().provideScreen() instanceof WidgetsEditorActivity editor) {
            editor.renderer().selectionRenderer().unselectAll();
          }
        }
      }
    }
  }

  @Subscribe
  public void onTick(GameTickEvent ignored) {
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

      // Sync color
      ColorCode defaultColor = feature.getDefaultColor();
      if (defaultColor != null) {
        ConfigValues configValues = this.main.getConfigValues();

        Integer color = ((ConfigValuesAccessor) configValues).getConfigColor(feature);
        config.color().set(Color.of(color == null ? defaultColor.getColor() : color));

        config.chroma().set(configValues.getChromaFeatures().contains(feature));
      }
    }
  }
}
