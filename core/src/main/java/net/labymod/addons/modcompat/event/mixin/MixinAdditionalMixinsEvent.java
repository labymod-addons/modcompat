package net.labymod.addons.modcompat.event.mixin;

import java.util.List;
import net.labymod.api.event.Event;

public record MixinAdditionalMixinsEvent(List<String> additionalMixins) implements Event {

  public void addAdditionalMixin(String name) {
    this.additionalMixins.add(name);
  }
}
