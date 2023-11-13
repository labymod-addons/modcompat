package net.labymod.addons.modcompat.mod;

import net.labymod.api.service.Registry;

public interface IncompatibleModRegistry extends Registry<IncompatibleMod> {

  boolean isModPlayable(String modId);
}
