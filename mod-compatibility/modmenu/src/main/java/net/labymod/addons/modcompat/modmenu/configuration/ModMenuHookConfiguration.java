package net.labymod.addons.modcompat.modmenu.configuration;

import net.labymod.addons.modcompat.ModCompat;
import net.labymod.addons.modcompat.acessor.ModMenuAccessor;
import net.labymod.addons.modcompat.core.generated.DefaultReferenceStorage;
import net.labymod.addons.modcompat.modmenu.launch.ModMenuEntrypoint;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget.ButtonSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.annotation.Exclude;
import net.labymod.api.modloader.ModLoader;
import net.labymod.api.modloader.ModLoaderId;
import net.labymod.api.modloader.ModLoaderRegistry;
import net.labymod.api.util.MethodOrder;

@ConfigName("modmenu")
public class ModMenuHookConfiguration extends Config {

  @Exclude
  private transient final ModMenuAccessor accessor;

  public ModMenuHookConfiguration() {
    ModLoader fabricLoader = ModLoaderRegistry.instance().getById(ModLoaderId.FABRIC);
    boolean modMenuLoaded =
        fabricLoader != null && fabricLoader.isModLoaded(ModMenuEntrypoint.MOD_ID);

    this.accessor = modMenuLoaded
        ? ((DefaultReferenceStorage) ModCompat.instance().references()).getModMenuAccessor()
        : null;
  }

  @ButtonSetting
  @MethodOrder(after = "accessor")
  public void openModMenu() {
    if (this.accessor != null) {
      Laby.labyAPI().minecraft().minecraftWindow().displayScreen(this.accessor.createScreen());
    }
  }
}
