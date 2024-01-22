package net.labymod.addons.modcompat.replaymod.accessor;

import com.replaymod.lib.de.johni0702.minecraft.gui.element.GuiButton;
import com.replaymod.recording.ReplayModRecording;
import com.replaymod.recording.gui.GuiRecordingControls;
import com.replaymod.recording.handler.ConnectionEventHandler;
import java.lang.reflect.Field;
import net.labymod.api.util.reflection.Reflection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GuiRecordingControlsAccessor {

  private final GuiRecordingControls guiRecordingControls;

  private GuiRecordingControlsAccessor(@NotNull GuiRecordingControls guiRecordingControls) {
    this.guiRecordingControls = guiRecordingControls;
  }

  public static @Nullable GuiRecordingControlsAccessor getInstance() {
    ReplayModRecording instance = ReplayModRecording.instance;
    if (instance == null) {
      return null;
    }

    ConnectionEventHandler eventHandler = instance.getConnectionEventHandler();
    if (eventHandler == null) {
      return null;
    }

    try {
      Field field = eventHandler.getClass().getDeclaredField("guiControls");
      field.setAccessible(true);

      GuiRecordingControls guiRecordingControls = Reflection.invokeGetterField(eventHandler, field);
      if (guiRecordingControls == null) {
        return null;
      }

      return new GuiRecordingControlsAccessor(guiRecordingControls);
    } catch (NoSuchFieldException exception) {
      return null;
    }
  }

  public GuiButton getPauseResumeButton() {
    try {
      Field field = this.guiRecordingControls.getClass().getDeclaredField("buttonPauseResume");
      field.setAccessible(true);

      return Reflection.invokeGetterField(this.guiRecordingControls, field);
    } catch (NoSuchFieldException exception) {
      throw new IllegalStateException("Failed to access button. Did ReplayMod change?", exception);
    }
  }

  public GuiButton getStartStopButton() {
    try {
      Field field = this.guiRecordingControls.getClass().getDeclaredField("buttonStartStop");
      field.setAccessible(true);

      return Reflection.invokeGetterField(this.guiRecordingControls, field);
    } catch (NoSuchFieldException exception) {
      throw new IllegalStateException("Failed to access button. Did ReplayMod change?", exception);
    }
  }

  public boolean isRecording() {
    return !this.guiRecordingControls.isStopped();
  }
}
