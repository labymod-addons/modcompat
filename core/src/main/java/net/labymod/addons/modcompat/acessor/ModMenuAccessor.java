package net.labymod.addons.modcompat.acessor;

import net.labymod.api.client.gui.screen.ScreenInstance;
import net.labymod.api.reference.annotation.Referenceable;
import org.jetbrains.annotations.Nullable;

// TODO: It should be possible that this class is in the mod-compatibility module
@Nullable
@Referenceable
public interface ModMenuAccessor {

  ScreenInstance createScreen();
}
