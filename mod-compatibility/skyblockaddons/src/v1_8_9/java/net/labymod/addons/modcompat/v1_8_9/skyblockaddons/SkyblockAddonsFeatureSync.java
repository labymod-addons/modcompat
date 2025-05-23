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
import net.labymod.api.client.gui.hud.hudwidget.HudWidget;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.widget.widgets.navigation.tab.Tab;
import net.labymod.api.event.Phase;
import net.labymod.api.event.Priority;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.lifecycle.GameTickEvent;
import net.labymod.api.event.client.render.overlay.IngameOverlayRenderEvent;
import net.labymod.api.event.labymod.config.ConfigurationSaveEvent;
import net.labymod.api.util.Color;
import net.labymod.api.util.KeyValue;
import net.labymod.core.client.gui.hud.overlay.HudWidgetOverlay;
import net.labymod.core.client.gui.screen.activity.activities.labymod.LabyModActivity;
import net.labymod.core.client.gui.screen.activity.activities.labymod.child.WidgetsEditorActivity;

public class SkyblockAddonsFeatureSync {

  private final LabyAPI labyAPI = Laby.labyAPI();
  private final SkyblockAddons main = SkyblockAddons.getInstance();

  private boolean customPlayerListEnabled;

  public SkyblockAddonsFeatureSync() {
    this.labyAPI.hudWidgetRegistry().categoryRegistry().register(
        SkyblockAddonsCompat.SKYBLOCK_ADDONS_CATEGORY
    );
  }

  public void registerHudWidgets() {
    HudWidgetRegistry hudWidgetRegistry = this.labyAPI.hudWidgetRegistry();

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
    for (HudWidget<?> widget : this.labyAPI.hudWidgetRegistry().values()) {
      if (widget instanceof SkyblockAddonsHudWidget) {
        this.labyAPI.hudWidgetRegistry().unregister(widget.getId());
      }
      if (widget.getParent() instanceof SkyblockAddonsHudWidget) {
        widget.updateParent(null);
      }
      if (widget.getChild() instanceof SkyblockAddonsHudWidget) {
        widget.updateChild(null);
      }
    }
    this.labyAPI.ingameOverlay().getActivity(HudWidgetOverlay.class).ifPresent(Activity::reload);

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

  @Subscribe(Priority.LATEST)
  public void onIngameOverlayRender(IngameOverlayRenderEvent event) {
    // Prevent two player lists being rendered when the compact tab list is enabled
    if (!this.main.getUtils().isOnSkyblock() || !Feature.COMPACT_TAB_LIST.isEnabled()) {
      return;
    }

    var customPlayerList = this.labyAPI.config().multiplayer().customPlayerList();

    if (event.phase() == Phase.PRE) {
      // Disable custom player list as the compact tab list is enabled and the user is on SkyBlock
      this.customPlayerListEnabled = customPlayerList.get();
      customPlayerList.set(false);
    } else if (event.phase() == Phase.POST) {
      // Restore state after rendering
      customPlayerList.set(this.customPlayerListEnabled);
    }
  }

  @Subscribe
  public void onTick(GameTickEvent ignored) {
    for (HudWidget<?> hudWidget : this.labyAPI.hudWidgetRegistry().values()) {
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

  @Subscribe
  public void onConfigurationSave(ConfigurationSaveEvent event) {
    this.main.getConfigValues().saveConfig();
  }
}
