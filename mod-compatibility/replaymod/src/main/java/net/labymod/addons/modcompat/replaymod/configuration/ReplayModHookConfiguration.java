package net.labymod.addons.modcompat.replaymod.configuration;

import net.labymod.addons.modcompat.ModCompat;
import net.labymod.addons.modcompat.acessor.ReplayMod;
import net.labymod.addons.modcompat.core.generated.DefaultReferenceStorage;
import net.labymod.addons.modcompat.replaymod.ReplayModUtil;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget.ButtonSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.annotation.Exclude;
import net.labymod.api.util.MethodOrder;

@ConfigName("replaymod")
public class ReplayModHookConfiguration extends Config {

  @Exclude
  private final Object dummy = new Object();

  @ButtonSetting
  @MethodOrder(before = "dummy")
  public void openReplayViewer() {
    ReplayModUtil.displayViewer();
  }
}
