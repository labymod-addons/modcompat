package net.labymod.addons.modcompat.mod;

import net.labymod.addons.modcompat.mod.issue.ModIssue;

import java.util.Collection;

public interface IncompatibleMod {

  String getId();

  String getName();

  String getDescription();

  String getWebsite();

  Collection<String> getModLoaders();

  Collection<? extends ModIssue> getIssues();
}
