package net.labymod.addons.modcompat.mod;

import net.labymod.addons.modcompat.mod.issue.ModIssue;
import net.labymod.api.Laby;
import net.labymod.api.service.DefaultRegistry;

public class DefaultIncompatibleModRegistry
    extends DefaultRegistry<IncompatibleMod>
    implements IncompatibleModRegistry {

  @Override
  public boolean isModPlayable(String modId) {
    IncompatibleMod incompatibleMod = this.getById(modId);
    if (incompatibleMod == null) {
      // Not registered as incompatible
      return true;
    }

    for (ModIssue issue : incompatibleMod.getIssues()) {
      if (!issue.affectedVersions().isCompatible(Laby.labyAPI().labyModLoader().version())) {
        // The issue is not relevant in this Minecraft version
        continue;
      }

      if (!issue.isPlayable() && !issue.isFixed()) {
        // There is at least one unfixed issue which affects playability
        return false;
      }
    }

    return true;
  }
}
