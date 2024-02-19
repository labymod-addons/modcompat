package net.labymod.addons.modcompat.v1_8_9.skyblockaddons;

import codes.biscuit.skyblockaddons.core.Feature;
import net.labymod.addons.modcompat.mod.fix.ModFixEntrypoint;
import net.labymod.addons.modcompat.v1_8_9.skyblockaddons.hud.SkyblockAddonsHudWidget;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.client.gui.hud.hudwidget.HudWidget;
import net.labymod.api.client.gui.hud.hudwidget.HudWidgetConfig;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.lifecycle.GameTickEvent;
import net.labymod.api.models.addon.annotation.AddonEntryPoint;
import net.labymod.api.models.version.Version;

@AddonEntryPoint(value = AddonEntryPoint.Point.ENABLE)
public class VersionedSkyblockAddonsEntrypoint extends ModFixEntrypoint {

  public static final HudWidgetCategory SKYBLOCK_ADDONS_CATEGORY =
      new HudWidgetCategory("skyblockaddons");

  public VersionedSkyblockAddonsEntrypoint() {
    super("skyblockaddons");
  }

  @Override
  public void initialize(Version version) {
    if (!super.isModLoaded()) {
      return;
    }

    Laby.labyAPI().eventBus().registerListener(this);
    Laby.labyAPI().hudWidgetRegistry().categoryRegistry().register(SKYBLOCK_ADDONS_CATEGORY);

    for (Feature guiFeature : Feature.getGuiFeatures()) {
      if (guiFeature.getGuiFeatureData() == null
          || guiFeature.getGuiFeatureData().getDrawType() == null) {
        continue;
      }
      HudWidget<HudWidgetConfig> hudWidget = new SkyblockAddonsHudWidget(guiFeature);
      Laby.labyAPI().hudWidgetRegistry().register(hudWidget);
    }
  }

  @Subscribe
  public void onTick(GameTickEvent event) {
    for (HudWidget<?> hudWidget : Laby.labyAPI().hudWidgetRegistry().values()) {
      HudWidgetConfig config = hudWidget.getConfig();
      if (config != null && hudWidget instanceof SkyblockAddonsHudWidget skyblockAddonsHudWidget) {
        // Sync SkyblockAddons feature enabled state with the hud widget
        config.setEnabled(skyblockAddonsHudWidget.feature().isEnabled());
      }
    }
  }
}
