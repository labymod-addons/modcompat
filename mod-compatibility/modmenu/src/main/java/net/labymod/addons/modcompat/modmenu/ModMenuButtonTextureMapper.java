package net.labymod.addons.modcompat.modmenu;

import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.gui.screen.VanillaWidgetReplacementEvent;

public class ModMenuButtonTextureMapper {

  @Subscribe
  public void onWidgetReplacement(VanillaWidgetReplacementEvent event) {
    if (event.getWidget() instanceof ButtonWidget buttonWidget) {
      Icon icon = buttonWidget.icon().get();
      if (icon != null) {
        ResourceLocation mappedLocation = ModMenuTextures.getTexture(icon.getResourceLocation());
        if (mappedLocation != null) {
          buttonWidget.icon().set(Icon.texture(mappedLocation));
        }
      }
    }
  }
}
