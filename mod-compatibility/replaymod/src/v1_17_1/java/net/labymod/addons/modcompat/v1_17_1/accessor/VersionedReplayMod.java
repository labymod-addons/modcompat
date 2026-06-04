package net.labymod.addons.modcompat.v1_17_1.accessor;

import com.replaymod.lib.de.johni0702.minecraft.gui.element.GuiButton;
import com.replaymod.recording.ReplayModRecording;
import com.replaymod.recording.gui.GuiRecordingControls;
import com.replaymod.recording.handler.ConnectionEventHandler;
import com.replaymod.replay.ReplayHandler;
import com.replaymod.replay.ReplayModReplay;
import com.replaymod.replay.gui.screen.GuiReplayViewer;
import net.labymod.addons.modcompat.acessor.ReplayMod;
import net.labymod.addons.modcompat.hook.AddonHooks;
import net.labymod.addons.modcompat.replaymod.launch.ReplayModEntrypoint;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.screen.ScreenWrapper;
import net.labymod.api.client.gui.window.Window;
import net.labymod.api.configuration.settings.Setting;
import net.labymod.api.models.Implements;
import net.labymod.api.util.reflection.Reflection;
import javax.inject.Singleton;
import java.lang.reflect.Field;

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

    Window window = Laby.labyAPI().minecraft().minecraftWindow();
    ScreenWrapper prevScreen = window.currentScreen();
    viewer.cancelButton.onClick(() -> window.displayScreen(prevScreen));

    viewer.display();
  }

  @Override
  public boolean isRelayOverlayVisible() {
    ReplayHandler handler = ReplayModReplay.instance.getReplayHandler();
    return handler != null && handler.getOverlay().isVisible();
  }

  @Override
  public boolean hasReplayHandler() {
    return ReplayModReplay.instance.getReplayHandler() != null;
  }

  @Override
  public RecordingControls recordingControls() {
    return VersionedRecordingControls.create();
  }

  static class VersionedRecordingControls implements RecordingControls {

    private final GuiRecordingControls controls;
    private final GuiButton pauseResumeButton;
    private final GuiButton startStopButton;

    private VersionedRecordingControls(GuiRecordingControls controls) {
      this.controls = controls;
      this.pauseResumeButton = this.getPauseResumeButton();
      this.startStopButton = this.getStartStopButton();
    }

    public static RecordingControls create() {
      ReplayModRecording recording = ReplayModRecording.instance;
      if (recording == null) {
        return null;
      }

      ConnectionEventHandler handler = recording.getConnectionEventHandler();
      if (handler == null) {
        return null;
      }

      try {
        Field field = handler.getClass().getDeclaredField("guiControls");
        field.setAccessible(true);

        GuiRecordingControls controls = Reflection.invokeGetterField(handler, field);
        if (controls == null) {
          return null;
        }

        return new VersionedRecordingControls(controls);
      } catch (ReflectiveOperationException exception) {
        return null;
      }
    }

    @Override
    public String getPauseResumeLabel() {
      return this.pauseResumeButton.getLabel();
    }

    @Override
    public void clickPauseResume() {
      this.pauseResumeButton.getOnClick().run();
    }

    @Override
    public String getStartStopLabel() {
      return this.startStopButton.getLabel();
    }

    @Override
    public void clickStartStop() {
      this.startStopButton.getOnClick().run();
    }

    @Override
    public boolean isRecording() {
      return !this.controls.isStopped();
    }

    private GuiButton getPauseResumeButton() {
      return this.getButton("buttonPauseResume");
    }

    private GuiButton getStartStopButton() {
      return this.getButton("buttonStartStop");
    }

    private GuiButton getButton(String name) {
      try {
        Field field = this.controls.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return Reflection.invokeGetterField(this.controls, field);
      } catch (ReflectiveOperationException exception) {
        throw new IllegalStateException("Unable to access button field: " + name, exception);
      }
    }
  }
}
