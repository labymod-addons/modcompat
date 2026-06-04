package net.labymod.addons.modcompat.replaymod.listener;

import net.labymod.addons.modcompat.ModCompat;
import net.labymod.addons.modcompat.ModCompatAddon;
import net.labymod.addons.modcompat.acessor.ReplayMod;
import net.labymod.addons.modcompat.acessor.ReplayMod.RecordingControls;
import net.labymod.addons.modcompat.core.generated.DefaultReferenceStorage;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.activity.Document;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.gui.screen.ActivityInitializeEvent;

public class IngameMenuListener {

  private static final String INGAME_MENU_ID = "labymod:ingame_menu_overlay";

  @Subscribe
  public void onActivityInitialize(ActivityInitializeEvent event) {
    var references = (DefaultReferenceStorage) ModCompat.instance().references();
    ReplayMod replayMod = references.getReplayMod();
    if (replayMod == null) {
      return;
    }

    RecordingControls recordingControls = replayMod.recordingControls();
    if (recordingControls == null || replayMod.hasReplayHandler()) {
      return;
    }

    if (INGAME_MENU_ID.equals(event.getIdentifier())) {
      this.addButtons(event.activity(), recordingControls);
    }
  }

  private void addButtons(Activity activity, RecordingControls recordingControls) {
    activity.addStyle(ModCompatAddon.NAMESPACE, "replaymod/ingame-menu.lss");
    Document document = activity.document();

    if (!(document.getChild("container") instanceof DivWidget widget)) {
      return;
    }

    DivWidget buttonContainer = new DivWidget().addId("replaymod-button-container");

    // Button to pause or resume recording
    String pauseResumeLabel = recordingControls.getPauseResumeLabel();
    ButtonWidget buttonTogglePause = ButtonWidget.text(pauseResumeLabel)
        .addId("toggle-pause-button");
    buttonTogglePause.setEnabled(recordingControls.isRecording());
    buttonTogglePause.setPressable(() -> {
      // Delegate to on click of ReplayMod
      recordingControls.clickPauseResume();
      // Update label
      buttonTogglePause.updateComponent(Component.text(pauseResumeLabel));
    });
    buttonContainer.addChild(buttonTogglePause);

    String startStopLabel = recordingControls.getStartStopLabel();
    ButtonWidget buttonToggleStop = ButtonWidget.text(startStopLabel).addId("toggle-stop-button");
    buttonToggleStop.setPressable(() -> {
      // Delegate to on click of ReplayMod
      recordingControls.clickStartStop();
      // Update label
      buttonToggleStop.updateComponent(Component.text(startStopLabel));
      // Disable pause button if recording is stopped
      buttonTogglePause.setEnabled(recordingControls.isRecording());
    });
    buttonContainer.addChild(buttonToggleStop);

    widget.addChildInitialized(buttonContainer);
  }
}
