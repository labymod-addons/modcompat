package net.labymod.addons.modcompat.modmenu;

import java.util.Map;
import net.labymod.api.client.resources.ResourceLocation;

public class ModMenuTextures {

  private static final Map<ResourceLocation, ResourceLocation> TEXTURE_MAP = Map.of(
      // Mods Icon
      ResourceLocation.create("modmenu", "textures/gui/mods_button.png"),
      ResourceLocation.create("modcompat", "modmenu/textures/mods_button.png"),
      // Filter Icon
      ResourceLocation.create("modmenu", "textures/gui/filters_button.png"),
      ResourceLocation.create("modcompat", "modmenu/textures/filters_button.png"),
      // Configure Icon
      ResourceLocation.create("modmenu", "textures/gui/configure_button.png"),
      ResourceLocation.create("modcompat", "modmenu/textures/configure_button.png")
  );

  public static ResourceLocation getTexture(ResourceLocation location) {
    return TEXTURE_MAP.getOrDefault(location, location);
  }
}
