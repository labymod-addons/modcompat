package net.labymod.addons.modcompat.flashback;

import net.labymod.api.Laby;
import net.labymod.api.client.gui.screen.ScreenInstance;
import net.labymod.api.client.gui.screen.ScreenWrapper;

public interface FlashbackAccessor {

  ScreenWrapper.Factory FACTORY = Laby.references().screenWrapperFactory();

  ScreenInstance createReplayViewerScreen();
}
