package net.labymod.addons.modcompat.v1_19_2.modmenu.accessor;

import com.terraformersmc.modmenu.gui.ModsScreen;
import javax.inject.Singleton;
import net.labymod.addons.modcompat.acessor.ModMenuAccessor;
import net.labymod.api.client.gui.screen.ScreenInstance;
import net.labymod.api.models.Implements;
import net.labymod.v1_19_2.client.gui.screen.VersionedScreenWrapper;
import net.minecraft.client.Minecraft;

@Singleton
@Implements(ModMenuAccessor.class)
public class VersionedModMenuAccessor implements ModMenuAccessor {

  @Override
  public ScreenInstance createScreen() {
    return new VersionedScreenWrapper(new ModsScreen(Minecraft.getInstance().screen));
  }
}
