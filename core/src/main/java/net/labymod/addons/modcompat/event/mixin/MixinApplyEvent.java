package net.labymod.addons.modcompat.event.mixin;

import net.labymod.api.event.Event;
import net.labymod.api.event.Phase;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public record MixinApplyEvent(
    Phase phase,
    String targetClassName,
    ClassNode targetClass,
    String mixinClassName,
    IMixinInfo mixinInfo
) implements Event {

}
