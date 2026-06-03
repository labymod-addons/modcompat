package net.labymod.addons.modcompat.v26_1.accessor;

import com.replaymod.replay.ReplayModReplay;
import com.replaymod.replay.gui.screen.GuiReplayViewer;
import net.labymod.addons.modcompat.acessor.ReplayMod;
import net.labymod.addons.modcompat.hook.AddonHooks;
import net.labymod.addons.modcompat.replaymod.launch.ReplayModEntrypoint;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.screen.ScreenWrapper;
import net.labymod.api.configuration.settings.Setting;
import net.labymod.api.models.Implements;
import javax.inject.Singleton;

@Singleton
@Implements(ReplayMod.class)
public class VersionedReplayMod implements ReplayMod {

  @Override
  public void displayViewer() {
    GuiReplayViewer viewer = new GuiReplayViewer(ReplayModReplay.instance);

    Setting addonSettings = AddonHooks.instance().getAddonSettings(ReplayModEntrypoint.ADDON_ID);
    if (addonSettings != null) {
      viewer.settingsButton.onClick(() -> Laby.labyAPI().showSetting(addonSettings));
    }

    ScreenWrapper prevScreen = Laby.labyAPI().minecraft().minecraftWindow().currentScreen();
    viewer.cancelButton.onClick(() -> Laby.labyAPI().minecraft().minecraftWindow().displayScreen(prevScreen));

    viewer.display();
  }
}
