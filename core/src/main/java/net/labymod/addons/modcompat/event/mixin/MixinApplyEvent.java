package net.labymod.addons.modcompat.event.mixin;

import net.labymod.api.event.Event;
import net.labymod.api.event.Phase;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public final class MixinApplyEvent implements Event {

  private final Phase phase;
  private final String targetClassName;
  private final ClassNode targetClass;
  private final String mixinClassName;
  private final IMixinInfo mixinInfo;

  public MixinApplyEvent(
      Phase phase,
      String targetClassName,
      ClassNode targetClass,
      String mixinClassName,
      IMixinInfo mixinInfo
  ) {
    this.phase = phase;
    this.targetClassName = targetClassName;
    this.targetClass = targetClass;
    this.mixinClassName = mixinClassName;
    this.mixinInfo = mixinInfo;
  }

  public Phase phase() {
    return this.phase;
  }

  public String getTargetClassName() {
    return this.targetClassName;
  }

  public ClassNode getTargetClass() {
    return this.targetClass;
  }

  public String getMixinClassName() {
    return this.mixinClassName;
  }

  public IMixinInfo getMixinInfo() {
    return this.mixinInfo;
  }
}
