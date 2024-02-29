package net.labymod.addons.modcompat.replaymod.settings.config;

import com.replaymod.replay.ReplayModReplay;
import com.replaymod.replay.gui.screen.GuiReplayViewer;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget.ButtonSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.annotation.Exclude;
import net.labymod.api.util.MethodOrder;

@ConfigName("replaymod")
public class ReplayModHookConfig extends Config {

  @Exclude
  private final Object dummy = new Object();

  @ButtonSetting
  @MethodOrder(before = "dummy")
  public void openReplayViewer() {
    new GuiReplayViewer(ReplayModReplay.instance).display();
  }
}
