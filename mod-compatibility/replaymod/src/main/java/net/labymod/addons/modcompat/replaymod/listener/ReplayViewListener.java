package net.labymod.addons.modcompat.replaymod.listener;

import com.replaymod.replay.ReplayHandler;
import com.replaymod.replay.ReplayModReplay;
import net.labymod.addons.modcompat.replaymod.accessor.MinecraftTimerAccessor;
import net.labymod.api.Laby;
import net.labymod.api.client.Minecraft;
import net.labymod.api.client.component.Component;
import net.labymod.api.configuration.labymod.main.laby.IngameConfig;
import net.labymod.api.event.Phase;
import net.labymod.api.event.Priority;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.render.GameRenderEvent;
import net.labymod.api.event.client.render.overlay.IngameOverlayRenderEvent;
import net.labymod.api.event.labymod.labyconnect.session.chat.LabyConnectChatDropdownInitializeEvent;
import net.labymod.api.util.time.ModernTickDeltaTimer;

public class ReplayViewListener {

  private final ModernTickDeltaTimer deltaTimer = new ModernTickDeltaTimer();

  private boolean hudWidgetsEnabled;
  private boolean advancedChatEnabled;
  private float prevTickDelta;

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
  public void onGameRender(GameRenderEvent event) {
    if (ReplayModReplay.instance.getReplayHandler() == null) {
      return;
    }

    if (event.phase() == Phase.PRE) {
      this.deltaTimer.advanceTime();
    }

    Minecraft minecraft = Laby.labyAPI().minecraft();
    if (minecraft.minecraftWindow().currentScreen() == null
        || !minecraft.minecraftWindow().currentScreen().isPauseScreen()) {
      return;
    }

    MinecraftTimerAccessor timerAccessor = (MinecraftTimerAccessor) minecraft;
    if (event.phase() == Phase.PRE) {
      // When a replay is paused, the tick delta is always 0, so restore it temporarily
      // for the screen animations in the pause menu to work properly
      this.prevTickDelta = minecraft.getTickDelta();
      timerAccessor.setTickDelta(this.deltaTimer.getTickDelta());
    } else {
      timerAccessor.setTickDelta(this.prevTickDelta);
    }
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
