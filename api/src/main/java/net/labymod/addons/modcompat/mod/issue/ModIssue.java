package net.labymod.addons.modcompat.mod.issue;

import net.labymod.api.models.version.VersionCompatibility;

public interface ModIssue {

  String getName();

  String getDescription();

  VersionCompatibility affectedVersions();

  IssueImpact impact();

  boolean isPlayable();

  boolean isFixed();
}
