package net.labymod.addons.modcompat.replaymod.listener;

import com.replaymod.replay.ReplayHandler;
import com.replaymod.replay.ReplayModReplay;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.configuration.labymod.main.laby.IngameConfig;
import net.labymod.api.event.Phase;
import net.labymod.api.event.Priority;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.render.overlay.IngameOverlayRenderEvent;
import net.labymod.api.event.labymod.labyconnect.session.chat.LabyConnectChatDropdownInitializeEvent;

public class ReplayViewListener {

  private boolean hudWidgetsEnabled;
  private boolean advancedChatEnabled;

  @Subscribe(value = Priority.FIRST)
  public void onPreIngameOverlayRender(IngameOverlayRenderEvent event) {
    if (event.phase() != Phase.PRE) {
      return;
    }

    IngameConfig ingameConfig = Laby.labyAPI().config().ingame();

    // Save enabled state for hud widgets and advanced chat
    this.hudWidgetsEnabled = ingameConfig.hudWidgets().get();
    this.advancedChatEnabled = ingameConfig.advancedChat().enabled().get();

    // Make sure that both is displayed when viewing replay
    ReplayHandler replayHandler = ReplayModReplay.instance.getReplayHandler();
    if (replayHandler != null && replayHandler.getOverlay().isVisible()) {
      ingameConfig.hudWidgets().set(false);
      ingameConfig.advancedChat().enabled().set(false);
    }
  }

  @Subscribe(value = Priority.LATEST)
  public void onPostIngameOverlayRender(IngameOverlayRenderEvent event) {
    if (event.phase() != Phase.POST) {
      return;
    }

    IngameConfig ingameConfig = Laby.labyAPI().config().ingame();

    // Restore enabled state for hud widgets and advanced chat
    ingameConfig.hudWidgets().set(this.hudWidgetsEnabled);
    ingameConfig.advancedChat().enabled().set(this.advancedChatEnabled);
  }

  @Subscribe
  public void onChatDropdownInitialize(LabyConnectChatDropdownInitializeEvent event) {
    if (ReplayModReplay.instance.getReplayHandler() == null) {
      return;
    }

    // Remove join server button, currently viewing a replay
    event.menu().entries().removeIf(
        entry -> Component.translatable("labymod.activity.multiplayer.joinServer")
            .equals(entry.getText())
    );
  }
}
