package net.labymod.addons.modcompat.replaymod;

import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.widget.AbstractWidget;
import net.labymod.api.client.gui.screen.widget.widgets.activity.Document;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.gui.screen.ActivityInitializeEvent;

public class IngameMenuListener {

  private boolean recordingPaused;
  private boolean recordingStopped;

  @Subscribe
  public void onActivityInitialize(ActivityInitializeEvent event) {
    String identifier = event.getIdentifier();

    Activity activity = event.activity();
    if (identifier.equals("labymod:ingame_menu_overlay")) {
      this.addButtons(activity);
    }
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private void addButtons(Activity activity) {
    Document document = activity.document();

    AbstractWidget<ButtonWidget> widget
        = (AbstractWidget<ButtonWidget>) document.getChild("container").getChildren().get(0);

    ButtonWidget buttonTogglePause = ButtonWidget.i18nMinecraft("replaymod.gui.recording.pause");
    buttonTogglePause.setEnabled(!this.recordingStopped);
    buttonTogglePause.setPressable(() -> {
      this.recordingPaused = !this.recordingPaused;
      buttonTogglePause.updateComponent(Component.text(Laby.labyAPI().minecraft().getTranslation(
          "replaymod.gui.recording." + (this.recordingPaused ? "resume" : "pause")
      )));
    });
    buttonTogglePause.addId("toggle-pause-button");

    ButtonWidget buttonToggleStop = ButtonWidget.i18nMinecraft("replaymod.gui.recording.stop");
    buttonToggleStop.setPressable(() -> {
      this.recordingStopped = !this.recordingStopped;
      buttonToggleStop.updateComponent(Component.text(Laby.labyAPI().minecraft().getTranslation(
          "replaymod.gui.recording." + (this.recordingStopped ? "start" : "stop")
      )));
      buttonToggleStop.setEnabled(!this.recordingStopped);
    });
    buttonToggleStop.addId("toggle-stop-button");

    widget.addChildInitialized(buttonToggleStop);
    widget.addChildInitialized(buttonTogglePause);
  }
}
