package net.labymod.addons.modcompat.v1_17_1.modmenu.accessor;

import com.terraformersmc.modmenu.gui.ModsScreen;
import javax.inject.Singleton;
import net.labymod.addons.modcompat.acessor.ModMenuAccessor;
import net.labymod.api.client.gui.screen.ScreenInstance;
import net.labymod.api.models.Implements;
import net.labymod.v1_17_1.client.gui.screen.VersionedScreenWrapper;
import net.minecraft.client.Minecraft;

@Singleton
@Implements(ModMenuAccessor.class)
public class VersionedModMenuAccessor implements ModMenuAccessor {

  @Override
  public ScreenInstance createScreen() {
    return FACTORY.create(new ModsScreen(Minecraft.getInstance().screen));
  }
}
