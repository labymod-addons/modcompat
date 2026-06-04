package net.labymod.addons.modcompat.replaymod;

import net.labymod.addons.modcompat.ModCompat;
import net.labymod.addons.modcompat.acessor.ReplayMod;
import net.labymod.addons.modcompat.core.generated.DefaultReferenceStorage;

public final class ReplayModUtil {

  private ReplayModUtil() {
  }

  public static void displayViewer() {
    var references = (DefaultReferenceStorage) ModCompat.instance().references();
    ReplayMod replayMod = references.getReplayMod();
    if (replayMod != null) {
      replayMod.displayViewer();
    }
  }

  public static boolean isRelayOverlayVisible() {
    var references = (DefaultReferenceStorage) ModCompat.instance().references();
    ReplayMod replayMod = references.getReplayMod();
    return replayMod != null && replayMod.isRelayOverlayVisible();
  }

  public static boolean hasReplayHandler() {
    var references = (DefaultReferenceStorage) ModCompat.instance().references();
    ReplayMod replayMod = references.getReplayMod();
    return replayMod != null && replayMod.hasReplayHandler();
  }
}
