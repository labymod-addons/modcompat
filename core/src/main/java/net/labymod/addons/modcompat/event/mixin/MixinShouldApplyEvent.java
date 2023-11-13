package net.labymod.addons.modcompat.event.mixin;

import net.labymod.api.event.Event;

public class MixinShouldApplyEvent implements Event {

  private boolean shouldApply;

  public MixinShouldApplyEvent(boolean shouldApply) {
    this.shouldApply = shouldApply;
  }

  public boolean shouldApply() {
    return this.shouldApply;
  }

  public void setShouldApply(boolean shouldApply) {
    this.shouldApply = shouldApply;
  }
}
