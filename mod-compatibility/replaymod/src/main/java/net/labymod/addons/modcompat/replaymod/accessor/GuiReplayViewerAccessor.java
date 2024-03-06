package net.labymod.addons.modcompat.replaymod.accessor;

import com.replaymod.replay.ReplayModReplay;
import com.replaymod.replay.gui.screen.GuiReplayViewer;
import net.labymod.addons.modcompat.hook.AddonHooks;
import net.labymod.addons.modcompat.replaymod.launch.ReplayModEnableEntrypoint;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.screen.ScreenWrapper;
import net.labymod.api.configuration.settings.Setting;

public class GuiReplayViewerAccessor {

  public static GuiReplayViewer create() {
    GuiReplayViewer replayViewer = new GuiReplayViewer(ReplayModReplay.instance);

    // Redirect to addon settings, if present
    Setting addonSettings = AddonHooks.instance()
        .getAddonSettings(ReplayModEnableEntrypoint.ADDON_ID);
    if (addonSettings != null) {
      replayViewer.settingsButton.onClick(() -> Laby.labyAPI().showSetting(addonSettings));
    }

    // Properly open previous screen
    ScreenWrapper previousScreen = Laby.labyAPI().minecraft().minecraftWindow().currentScreen();
    replayViewer.cancelButton.onClick(
        () -> Laby.labyAPI().minecraft().minecraftWindow().displayScreen(previousScreen)
    );

    return replayViewer;
  }
}
