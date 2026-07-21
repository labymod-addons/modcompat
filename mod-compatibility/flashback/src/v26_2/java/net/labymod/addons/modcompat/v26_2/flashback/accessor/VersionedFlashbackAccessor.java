package net.labymod.addons.modcompat.v26_2.flashback.accessor;

import com.moulberry.flashback.screen.select_replay.SelectReplayScreen;
import net.labymod.addons.modcompat.flashback.FlashbackAccessor;
import net.labymod.api.client.gui.screen.ScreenInstance;
import net.labymod.api.service.annotation.AutoService;
import net.minecraft.client.Minecraft;

@AutoService(value = FlashbackAccessor.class, versionSpecific = true)
public class VersionedFlashbackAccessor implements FlashbackAccessor {

  @Override
  public ScreenInstance createReplayViewerScreen() {
    return FACTORY.create(new SelectReplayScreen(Minecraft.getInstance().gui.screen()));
  }
}
