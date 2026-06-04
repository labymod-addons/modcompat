package net.labymod.addons.modcompat.acessor;

import net.labymod.api.models.NullableReference;
import net.labymod.api.reference.annotation.Referenceable;

@NullableReference
@Referenceable
public interface ReplayMod {

  void displayViewer();

  boolean isRelayOverlayVisible();

  boolean hasReplayHandler();

  RecordingControls recordingControls();

  interface RecordingControls {

    String getPauseResumeLabel();

    void clickPauseResume();

    String getStartStopLabel();

    void clickStartStop();

    boolean isRecording();

  }

}
