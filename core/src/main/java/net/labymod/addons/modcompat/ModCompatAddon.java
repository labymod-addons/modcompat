package net.labymod.addons.modcompat;

import javax.inject.Singleton;
import net.labymod.addons.modcompat.configuration.ModCompatConfiguration;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
@Singleton
public class ModCompatAddon extends LabyAddon<ModCompatConfiguration> {

  @Override
  protected void enable() {
  }

  @Override
  protected Class<? extends ModCompatConfiguration> configurationClass() {
    return ModCompatConfiguration.class;
  }
}
