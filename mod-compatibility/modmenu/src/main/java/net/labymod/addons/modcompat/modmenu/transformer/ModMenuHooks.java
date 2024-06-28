package net.labymod.addons.modcompat.modmenu.transformer;

import java.nio.file.Path;
import net.labymod.api.modloader.ModLoaderId;
import net.labymod.api.modloader.ModLoaderRegistry;

public class ModMenuHooks {

  public static Path getGameDir() {
    var fabricLoader = ModLoaderRegistry.instance().getById(ModLoaderId.FABRIC);
    if (fabricLoader == null) {
      throw new IllegalStateException("ModMenu was loaded without Fabric Loader");
    }
    for (Path modDirectoryPath : fabricLoader.getModDirectoryPaths()) {
      return modDirectoryPath.getParent();
    }
    throw new IllegalStateException("No mod directory paths provided");
  }
}
