package net.labymod.addons.modcompat.v1_8_9.skyblockaddons.event;

import codes.biscuit.skyblockaddons.core.Feature;
import net.labymod.api.event.Event;

public record SkyblockAddonsFeatureToggleEvent(Feature feature) implements Event {

}