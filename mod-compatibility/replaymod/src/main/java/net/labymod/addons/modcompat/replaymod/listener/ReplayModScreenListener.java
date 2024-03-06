package net.labymod.addons.modcompat.replaymod.listener;

import net.labymod.api.Laby;
import net.labymod.api.client.gfx.pipeline.RenderAttributes;
import net.labymod.api.client.gfx.pipeline.RenderAttributesStack;
import net.labymod.api.client.gui.screen.ScreenWrapper;
import net.labymod.api.event.Phase;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.render.ScreenRenderEvent;

public class ReplayModScreenListener {

  private static final Class<?> REPLAY_MOD_SCREEN_CLASS;

  static {
    Class<?> replayModScreenClass;
    try {
      replayModScreenClass = Class.forName(
          "com.replaymod.lib.de.johni0702.minecraft.gui.container.AbstractGuiScreen$MinecraftGuiScreen"
      );
    } catch (ClassNotFoundException ignored) {
      replayModScreenClass = null;
    }
    REPLAY_MOD_SCREEN_CLASS = replayModScreenClass;
  }

  @Subscribe
  public void onScreenRender(ScreenRenderEvent event) {
    ScreenWrapper wrapper = Laby.labyAPI().minecraft().minecraftWindow().currentScreen();
    if (wrapper == null) {
      return;
    }

    if (wrapper.getVersionedScreen().getClass().equals(REPLAY_MOD_SCREEN_CLASS)) {
      RenderAttributesStack stack = Laby.references()
          .renderEnvironmentContext()
          .renderAttributesStack();

      // Force vanilla font in Replay Mod screens, as other fonts lead to visual issues
      if (event.phase() == Phase.PRE) {
        RenderAttributes attributes = stack.pushAndGet();
        attributes.setForceVanillaFont(true);
        attributes.apply();
      } else {
        stack.pop();
      }
    }
  }
}