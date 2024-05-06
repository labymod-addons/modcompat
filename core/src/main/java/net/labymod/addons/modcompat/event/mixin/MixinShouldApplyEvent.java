package net.labymod.addons.modcompat.event.mixin;

import net.labymod.api.event.Event;

public class MixinShouldApplyEvent implements Event {

  private final String targetClassName;
  private final String mixinClassName;
  private boolean shouldApply;

  public MixinShouldApplyEvent(String targetClassName, String mixinClassName, boolean shouldApply) {
    this.targetClassName = targetClassName;
    this.mixinClassName = mixinClassName;
    this.shouldApply = shouldApply;
  }

  public String getTargetClassName() {
    return this.targetClassName;
  }

  public String getMixinClassName() {
    return this.mixinClassName;
  }

  public boolean shouldApply() {
    return this.shouldApply;
  }

  public void setShouldApply(boolean shouldApply) {
    this.shouldApply = shouldApply;
  }
}
