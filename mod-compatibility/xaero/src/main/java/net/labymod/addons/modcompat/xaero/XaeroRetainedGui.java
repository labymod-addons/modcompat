package net.labymod.addons.modcompat.xaero;

import net.labymod.api.Laby;
import net.labymod.api.client.gui.screen.state.retained.RetainedGui;

/**
 * Xaero's minimap, in-world waypoints and world map draw while the gui is built, partly outside
 * the gui render state. A frame that draws the kept gui again skips that, so they flicker. Each
 * draw makes the next frame build the gui too, which keeps it built on every frame while Xaero
 * draws and leaves it kept otherwise.
 */
public final class XaeroRetainedGui {

  private XaeroRetainedGui() {
  }

  /** Called where Xaero draws. Only a draw during a gui build needs the next frame built. */
  public static void onDraw() {
    RetainedGui retainedGui = Laby.references().retainedGui();
    if (retainedGui.frameTime().isCatchingUp()) {
      retainedGui.invalidate();
    }
  }
}
