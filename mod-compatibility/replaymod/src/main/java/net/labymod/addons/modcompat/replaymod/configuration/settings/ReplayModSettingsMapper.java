package net.labymod.addons.modcompat.replaymod.configuration.settings;

import net.labymod.addons.modcompat.replaymod.configuration.settings.widget.ReplayModEntryRenderer;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.renderer.EntryRenderer;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.settings.Setting;
import java.util.List;

public interface ReplayModSettingsMapper {

  EntryRenderer<Object> REPLAY_MOD_ENTRY_RENDERER = new ReplayModEntryRenderer<>();

  List<Setting> map(Config config);

}
