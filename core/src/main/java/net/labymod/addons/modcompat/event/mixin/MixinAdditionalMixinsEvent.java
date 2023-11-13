package net.labymod.addons.modcompat.event.mixin;

import java.util.List;
import net.labymod.api.event.Event;

public final class MixinAdditionalMixinsEvent implements Event {

  private final List<String> additionalMixins;

  public MixinAdditionalMixinsEvent(List<String> additionalMixins) {
    this.additionalMixins = additionalMixins;
  }

  public void addAdditionalMixin(String name) {
    this.additionalMixins.add(name);
  }

  public List<String> getAdditionalMixins() {
    return this.additionalMixins;
  }
}
