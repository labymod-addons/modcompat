package net.labymod.addons.modcompat.event.mixin;

import net.labymod.api.event.Event;

public final class MixinLoadEvent implements Event {

  private final String mixinPackage;

  public MixinLoadEvent(String mixinPackage) {
    this.mixinPackage = mixinPackage;
  }

  public String getMixinPackage() {
    return this.mixinPackage;
  }
}
