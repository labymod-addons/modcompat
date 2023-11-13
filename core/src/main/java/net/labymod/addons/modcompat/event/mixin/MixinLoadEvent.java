package net.labymod.addons.modcompat.event.mixin;

import net.labymod.api.event.Event;

public record MixinLoadEvent(String mixinPackage) implements Event {

}
