package net.labymod.addons.modcompat.hook;

import net.labymod.api.configuration.loader.ConfigAccessor;
import net.labymod.api.configuration.loader.ConfigProvider;

public class ModCompatConfigProvider<C extends ConfigAccessor> extends ConfigProvider<C> {

  private final Class<C> type;

  public ModCompatConfigProvider(Class<C> type) {
    this.type = type;
  }

  @Override
  public Class<C> getType() {
    return this.type;
  }
}
