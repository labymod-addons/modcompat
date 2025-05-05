package net.labymod.addons.modcompat.replaymod.listener;

import com.replaymod.replay.ReplayModReplay;
import net.labymod.addons.modcompat.ModCompatAddon;
import net.labymod.addons.modcompat.replaymod.accessor.GuiRecordingControlsAccessor;
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
    GuiRecordingControlsAccessor accessor = GuiRecordingControlsAccessor.getInstance();
    if (accessor == null || ReplayModReplay.instance.getReplayHandler() != null) {
      return;
    }

    if (INGAME_MENU_ID.equals(event.getIdentifier())) {
      this.addButtons(event.activity(), accessor);
    }
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private void addButtons(Activity activity, GuiRecordingControlsAccessor accessor) {
    activity.addStyle(ModCompatAddon.NAMESPACE, "replaymod/ingame-menu.lss");
    Document document = activity.document();

    if (!(document.getChild("container") instanceof DivWidget widget)) {
      return;
    }

    DivWidget buttonContainer = new DivWidget().addId("replaymod-button-container");

    // Button to pause or resume recording
    ButtonWidget buttonTogglePause = ButtonWidget.text(accessor.getPauseResumeButton().getLabel())
        .addId("toggle-pause-button");
    buttonTogglePause.setEnabled(accessor.isRecording());
    buttonTogglePause.setPressable(() -> {
      // Delegate to on click of ReplayMod
      accessor.getPauseResumeButton().getOnClick().run();
      // Update label
      buttonTogglePause.updateComponent(
          Component.text(accessor.getPauseResumeButton().getLabel())
      );
    });
    buttonContainer.addChild(buttonTogglePause);

    ButtonWidget buttonToggleStop = ButtonWidget.text(accessor.getStartStopButton().getLabel())
        .addId("toggle-stop-button");
    buttonToggleStop.setPressable(() -> {
      // Delegate to on click of ReplayMod
      accessor.getStartStopButton().getOnClick().run();
      // Update label
      buttonToggleStop.updateComponent(
          Component.text(accessor.getStartStopButton().getLabel())
      );
      // Disable pause button if recording is stopped
      buttonTogglePause.setEnabled(accessor.isRecording());
    });
    buttonContainer.addChild(buttonToggleStop);

    widget.addChildInitialized(buttonContainer);
  }
}
