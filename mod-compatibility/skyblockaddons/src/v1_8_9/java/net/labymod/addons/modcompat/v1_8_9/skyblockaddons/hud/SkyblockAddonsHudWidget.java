package net.labymod.addons.modcompat.v1_8_9.skyblockaddons.hud;

import codes.biscuit.skyblockaddons.SkyblockAddons;
import codes.biscuit.skyblockaddons.config.ConfigValues;
import codes.biscuit.skyblockaddons.core.Feature;
import codes.biscuit.skyblockaddons.gui.SkyblockAddonsGui;
import codes.biscuit.skyblockaddons.gui.buttons.ButtonLocation;
import codes.biscuit.skyblockaddons.utils.ColorCode;
import codes.biscuit.skyblockaddons.utils.Utils;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.labymod.addons.modcompat.v1_8_9.skyblockaddons.SkyblockAddonsCompat;
import net.labymod.addons.modcompat.v1_8_9.skyblockaddons.FeatureDrawContext;
import net.labymod.addons.modcompat.v1_8_9.skyblockaddons.hud.SkyblockAddonsHudWidget.SkyblockAddonsHudWidgetConfig;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.HudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.SimpleHudWidget;
import net.labymod.api.client.gui.hud.position.HudSize;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.color.ColorPickerWidget.ColorPickerSetting;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.CustomTranslation;
import net.labymod.api.configuration.settings.annotation.SettingRequires;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.gui.hud.HudWidgetCreatedEvent;
import net.labymod.api.event.client.gui.hud.HudWidgetDestroyedEvent;
import net.labymod.api.util.Color;
import net.labymod.api.util.bounds.area.RectangleAreaPosition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import org.jetbrains.annotations.NotNull;

public class SkyblockAddonsHudWidget extends SimpleHudWidget<SkyblockAddonsHudWidgetConfig> {

  private static final WorldClient DUMMY_WORLD = Utils.getDummyWorld();
  private static final EntityPlayerSP DUMMY_PLAYER = new EntityPlayerSP(
      Minecraft.getMinecraft(),
      DUMMY_WORLD,
      new NetHandlerPlayClient(
          Minecraft.getMinecraft(),
          null,
          null,
          new GameProfile(UUID.randomUUID(), "dummy")
      ),
      null
  );

  private final SkyblockAddons main = SkyblockAddons.getInstance();
  private final Feature feature;

  public SkyblockAddonsHudWidget(Feature feature) {
    super("skyblockaddons" + feature.getId(), SkyblockAddonsHudWidgetConfig.class);

    super.bindCategory(SkyblockAddonsCompat.SKYBLOCK_ADDONS_CATEGORY);
    super.setIcon(Icon.texture((ResourceLocation) SkyblockAddonsGui.LOGO));
    this.feature = feature;
  }

  @Override
  public @NotNull Component displayName() {
    return Component.text(this.feature.getMessage());
  }

  public Feature feature() {
    return this.feature;
  }

  @Subscribe
  public void onHudWidgetCreated(HudWidgetCreatedEvent event) {
    if (this == event.hudWidget()) {
      this.feature.setEnabled(true);
    }
  }

  @Subscribe
  public void onHudWidgetDestroyed(HudWidgetDestroyedEvent event) {
    if (this == event.hudWidget()) {
      this.feature.setEnabled(false);
    }
  }

  @Override
  public void load(SkyblockAddonsHudWidgetConfig config) {
    super.load(config);

    // Sync position if the widget has not been created before
    if (!config.isEnabled() && config.getX() == 0 && config.getY() == 0) {
      ConfigValues configValues = SkyblockAddons.getInstance().getConfigValues();

      float x = configValues.getRelativeCoords(this.feature).getX();
      float y = configValues.getRelativeCoords(this.feature).getY();

      // Gather dummy size information
      HudSize hudSize = new HudSize(config);
      this.updateSize(null, true, hudSize);

      float shiftX = hudSize.getActualWidth() / 2.0F;
      float shiftY = hudSize.getActualHeight() / 2.0F;

      RectangleAreaPosition areaPosition;
      switch (configValues.getAnchorPoint(this.feature)) {
        case TOP_LEFT:
          areaPosition = RectangleAreaPosition.TOP_LEFT;
          x -= shiftX;
          y -= shiftY;
          break;
        case TOP_RIGHT:
          areaPosition = RectangleAreaPosition.TOP_RIGHT;
          x += shiftX;
          y -= shiftY;
          break;
        case BOTTOM_LEFT:
          areaPosition = RectangleAreaPosition.BOTTOM_LEFT;
          x -= shiftX;
          y += shiftY;
          break;
        case BOTTOM_RIGHT:
          areaPosition = RectangleAreaPosition.BOTTOM_RIGHT;
          x += shiftX;
          y += shiftY;
          break;
        case BOTTOM_MIDDLE:
          areaPosition = RectangleAreaPosition.BOTTOM_CENTER;
          y += shiftY;
          break;
        default:
          throw new IllegalArgumentException();
      }

      config.setAreaIdentifier(areaPosition);

      config.setX(x);
      config.setY(y);

      config.scale().set(configValues.getGuiScale(this.feature));
    }

    // Sync changes from hud widget settings to the feature
    ColorCode defaultColor = this.feature.getDefaultColor();

    config.color().visibilitySupplier(() -> defaultColor != null);
    config.chroma().visibilitySupplier(() -> defaultColor != null);

    if (defaultColor != null) {
      config.color().updateDefaultValue(Color.of(defaultColor.getColor()));
      config.color().addChangeListener(
          value -> this.main.getConfigValues().setColor(this.feature, value.get())
      );
      config.chroma().addChangeListener(
          value -> this.main.getConfigValues().setChroma(this.feature, value)
      );
    }
  }

  @Override
  public void render(
      Stack stack,
      MutableMouse mouse,
      float partialTicks,
      boolean isEditorContext,
      HudSize size
  ) {
    FeatureDrawContext featureDrawContext = FeatureDrawContext.get();
    featureDrawContext.setDrawnFeature(this.feature);
    featureDrawContext.setStack(stack);
    featureDrawContext.setNoRender(stack == null);

    Minecraft minecraft = Minecraft.getMinecraft();
    ButtonLocation buttonLocation = isEditorContext ? new ButtonLocation(this.feature) : null;

    this.preFeatureRender(minecraft);
    this.feature.draw(1.0F, minecraft, buttonLocation);
    this.postFeatureRender(minecraft);

    size.set(
        (float) featureDrawContext.getWidth(),
        (float) featureDrawContext.getHeight()
    );

    featureDrawContext.reset();
  }

  private void preFeatureRender(Minecraft minecraft) {
    // Some of the features render entities, which requires the client player
    if (minecraft.thePlayer == null) {
      // Load class to initialize TileEntityRendererDispatcher
      var ignored = TileEntityRendererDispatcher.instance;
      minecraft.thePlayer = DUMMY_PLAYER;
    }
    if (minecraft.theWorld == null) {
      minecraft.theWorld = DUMMY_WORLD;
    }
  }

  private void postFeatureRender(Minecraft minecraft) {
    if (minecraft.thePlayer == DUMMY_PLAYER) {
      minecraft.thePlayer = null;
    }
    if (minecraft.theWorld == DUMMY_WORLD) {
      minecraft.theWorld = null;
    }
  }

  @Override
  public boolean isVisibleInGame() {
    if (this.feature == Feature.SKELETON_BAR
        && !this.main.getInventoryUtils().isWearingSkeletonHelmet()) {
      return false;
    }

    if (this.feature == Feature.HEALTH_UPDATES
        && this.main.getPlayerListener().getActionBarParser().getHealthUpdate() == null) {
      return false;
    }

    boolean darkAuctionTimer = this.feature == Feature.DARK_AUCTION_TIMER
        && this.main.getConfigValues().isEnabled(Feature.SHOW_DARK_AUCTION_TIMER_IN_OTHER_GAMES);
    boolean farmEventTimer = this.feature == Feature.FARM_EVENT_TIMER
        && this.main.getConfigValues().isEnabled(Feature.SHOW_FARM_EVENT_TIMER_IN_OTHER_GAMES);

    return this.main.getUtils().isOnSkyblock() || darkAuctionTimer || farmEventTimer;
  }

  public static class SkyblockAddonsHudWidgetConfig extends HudWidgetConfig {

    @ColorPickerSetting
    @SettingRequires(value = "chroma", invert = true)
    @CustomTranslation("skyblockaddons.hudWidget.color")
    private final ConfigProperty<Color> color = new ConfigProperty<>(Color.BLACK);

    @SwitchSetting
    @CustomTranslation("skyblockaddons.hudWidget.chroma")
    private final ConfigProperty<Boolean> chroma = new ConfigProperty<>(false);

    public ConfigProperty<Color> color() {
      return this.color;
    }

    public ConfigProperty<Boolean> chroma() {
      return this.chroma;
    }
  }
}
