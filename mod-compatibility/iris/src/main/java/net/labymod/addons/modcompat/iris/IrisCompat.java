package net.labymod.addons.modcompat.iris;

import net.labymod.addons.modcompat.iris.api.DefaultIrisApiWrapper;
import net.labymod.addons.modcompat.iris.api.IrisApiWrapper;
import net.labymod.api.modloader.ModLoader;
import net.labymod.api.modloader.ModLoaderId;
import net.labymod.api.modloader.ModLoaderRegistry;

public class IrisCompat {

  public static final String MOD_ID = "iris";

  private static final IrisApiWrapper API_WRAPPER = new DefaultIrisApiWrapper();
  private static final IrisApiWrapper EMPTY_WRAPPER = new IrisApiWrapper() {
    @Override
    public boolean isShadowPass() {
      return false;
    }

    @Override
    public boolean isShaderActive() {
      return false;
    }
  };

  public static boolean isIrisAvailable() {
    ModLoader fabricLoader = ModLoaderRegistry.instance().getById(ModLoaderId.FABRIC);
    return fabricLoader != null && fabricLoader.isModLoaded(MOD_ID);
  }

  public static IrisApiWrapper api() {
    return IrisCompat.isIrisAvailable() ? API_WRAPPER : EMPTY_WRAPPER;
  }
}
