package net.labymod.addons.modcompat.replaymod;

import net.labymod.core.client.gui.navigation.elements.MultiplayerNavigationElement;

public class ReplayMultiplayerNavigationElement extends MultiplayerNavigationElement {

  @Override
  public boolean isVisible() {
    // Hide multiplayer tab when viewing a replay to prevent the player from joining a server
    return !ReplayModUtil.hasReplayHandler();
  }
}
