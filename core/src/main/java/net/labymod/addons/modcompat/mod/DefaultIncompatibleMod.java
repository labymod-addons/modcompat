package net.labymod.addons.modcompat.mod;

import java.util.Collection;
import net.labymod.addons.modcompat.mod.issue.DefaultModIssue;

public class DefaultIncompatibleMod implements IncompatibleMod {

  private final String id;
  private final String name;
  private final String description;
  private final String website;
  private final Collection<String> modLoaders;
  private final Collection<DefaultModIssue> issues;

  public DefaultIncompatibleMod(
      String id,
      String name,
      String description,
      String website,
      Collection<String> modLoaders,
      Collection<DefaultModIssue> issues
  ) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.website = website;
    this.modLoaders = modLoaders;
    this.issues = issues;
  }

  @Override
  public String getId() {
    return this.id;
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
  public String getWebsite() {
    return this.website;
  }

  @Override
  public Collection<String> getModLoaders() {
    return this.modLoaders;
  }

  @Override
  public Collection<DefaultModIssue> getIssues() {
    return this.issues;
  }
}
