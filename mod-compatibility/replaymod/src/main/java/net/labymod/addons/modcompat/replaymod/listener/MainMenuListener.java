package net.labymod.addons.modcompat.replaymod.listener;

import net.labymod.addons.modcompat.replaymod.ReplayModUtil;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.gui.screen.MainMenuInitializeEvent;

public class MainMenuListener {

  private static final ResourceLocation REPLAY_VIEWER_ICON = ResourceLocation.create(
      "replaymod",
      "logo_button.png"
  );

  @Subscribe
  public void onMainMenuInitialize(MainMenuInitializeEvent event) {
    event.addIconButton(
        Icon.texture(REPLAY_VIEWER_ICON),
        Component.translatable("replaymod.gui.replayviewer"),
        ReplayModUtil::displayViewer
    );
  }
}
