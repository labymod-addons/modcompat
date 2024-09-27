package net.labymod.addons.modcompat.v1_21_1.modmenu.acessor;

import com.terraformersmc.modmenu.gui.ModsScreen;
import net.labymod.addons.modcompat.acessor.ModMenuAccessor;
import net.labymod.api.client.gui.screen.ScreenInstance;
import net.labymod.api.models.Implements;
import net.labymod.v1_21_1.client.gui.screen.VersionedScreenWrapper;
import net.minecraft.client.Minecraft;

import javax.inject.Singleton;

@Singleton
@Implements(ModMenuAccessor.class)
public class VersionedModMenuAccessor implements ModMenuAccessor {

  @Override
  public ScreenInstance createScreen() {
    return new VersionedScreenWrapper(new ModsScreen(Minecraft.getInstance().screen));
  }
}
