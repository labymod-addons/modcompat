package net.labymod.addons.modcompat.v1_8_9.skyblockaddons.hud;

import codes.biscuit.skyblockaddons.SkyblockAddons;
import codes.biscuit.skyblockaddons.config.ConfigValues;
import codes.biscuit.skyblockaddons.core.Feature;
import codes.biscuit.skyblockaddons.gui.buttons.ButtonLocation;
import codes.biscuit.skyblockaddons.utils.Utils;
import com.mojang.authlib.GameProfile;
import java.lang.reflect.Field;
import java.util.UUID;
import net.labymod.addons.modcompat.v1_8_9.skyblockaddons.VersionedSkyblockAddonsEntrypoint;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.HudWidget;
import net.labymod.api.client.gui.hud.hudwidget.HudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.SimpleHudWidget;
import net.labymod.api.client.gui.hud.position.HudSize;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.gui.hud.HudWidgetCreatedEvent;
import net.labymod.api.event.client.gui.hud.HudWidgetDestroyedEvent;
import net.labymod.api.util.bounds.area.RectangleAreaPosition;
import net.labymod.api.util.reflection.Reflection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

public class SkyblockAddonsHudWidget extends SimpleHudWidget<HudWidgetConfig> {

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
    super("skyblockaddons" + feature.getId(), HudWidgetConfig.class);
    super.bindCategory(VersionedSkyblockAddonsEntrypoint.SKYBLOCK_ADDONS_CATEGORY);
    this.feature = feature;

    // TODO: add option to change display name of hud widget in LabyMod?
    try {
      Field field = HudWidget.class.getDeclaredField("displayName");
      Reflection.invokeSetterField(
          this,
          field,
          Component.text(feature.getMessage())
      );
    } catch (NoSuchFieldException exception) {
      throw new RuntimeException(exception);
    }
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
  public void load(HudWidgetConfig config) {
    super.load(config);

    // Sync position if the widget has not been created before
    if (!config.isEnabled() && config.getX() == 0 && config.getY() == 0) {
      ConfigValues configValues = SkyblockAddons.getInstance().getConfigValues();

      RectangleAreaPosition areaPosition = switch (configValues.getAnchorPoint(this.feature)) {
        case TOP_LEFT -> RectangleAreaPosition.TOP_LEFT;
        case TOP_RIGHT -> RectangleAreaPosition.TOP_RIGHT;
        case BOTTOM_LEFT -> RectangleAreaPosition.BOTTOM_LEFT;
        case BOTTOM_RIGHT -> RectangleAreaPosition.BOTTOM_RIGHT;
        case BOTTOM_MIDDLE -> RectangleAreaPosition.BOTTOM_CENTER;
      };

      config.setAreaIdentifier(areaPosition);

      config.setX(configValues.getRelativeCoords(this.feature).getX());
      config.setY(configValues.getRelativeCoords(this.feature).getY());
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
    Minecraft minecraft = Minecraft.getMinecraft();
    ButtonLocation buttonLocation = isEditorContext ? new ButtonLocation(this.feature) : null;

    // TODO: Call render without stack too and disable rendering to get size info
    if (stack != null) {
      if (isEditorContext && this.feature == Feature.DEFENCE_ICON) {
        this.main.getRenderListener().drawIcon(1.0F, minecraft, buttonLocation);
      } else {
        this.preFeatureRender(minecraft);
        this.feature.draw(1.0F, minecraft, buttonLocation);
        this.postFeatureRender(minecraft);
      }

      // TODO: Obtain size without button location, otherwise the size is only known in the editor
      if (buttonLocation != null) {
        size.set(
            buttonLocation.getBoxXTwo() - buttonLocation.getBoxXOne(),
            buttonLocation.getBoxYTwo() - buttonLocation.getBoxYOne()
        );
      }
    }
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
}
