package net.labymod.addons.modcompat.flashback.listener;

import net.labymod.addons.modcompat.flashback.FlashbackAccessor;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.gui.screen.MainMenuInitializeEvent;

public class MainMenuListener {

  private static final ResourceLocation REPLAY_VIEWER_ICON = ResourceLocation.create(
      "flashback",
      "icon.png"
  );

  private final FlashbackAccessor accessor;

  public MainMenuListener(FlashbackAccessor accessor) {
    this.accessor = accessor;
  }

  @Subscribe
  public void onMainMenuInitialize(MainMenuInitializeEvent event) {
    event.addIconButton(
        Icon.texture(REPLAY_VIEWER_ICON),
        Component.translatable("flashback.select_replay"),
        this::displayReplayViewer
    );
  }

  private void displayReplayViewer() {
    Laby.labyAPI().minecraft().minecraftWindow()
        .displayScreen(this.accessor.createReplayViewerScreen());
  }
}
