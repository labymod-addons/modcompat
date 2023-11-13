package net.labymod.addons.modcompat.mod.issue;

import net.labymod.api.models.version.VersionCompatibility;

public class DefaultModIssue implements ModIssue {

  private final String name;
  private final String description;
  private final VersionCompatibility affectedVersions;
  private final IssueImpact impact;
  private final boolean playable;
  private final boolean fixed;

  public DefaultModIssue(
      String name,
      String description,
      VersionCompatibility affectedVersions,
      IssueImpact impact,
      boolean playable,
      boolean fixed
  ) {
    this.name = name;
    this.description = description;
    this.affectedVersions = affectedVersions;
    this.impact = impact;
    this.playable = playable;
    this.fixed = fixed;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public String getDescription() {
    return this.description;
  }

  @Override
  public VersionCompatibility affectedVersions() {
    return this.affectedVersions;
  }

  @Override
  public IssueImpact impact() {
    return this.impact;
  }

  @Override
  public boolean isPlayable() {
    return this.playable;
  }

  @Override
  public boolean isFixed() {
    return this.fixed;
  }
}
