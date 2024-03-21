package net.labymod.addons.modcompat.v1_19_3.modmenu;

import com.terraformersmc.modmenu.gui.widget.ModMenuTexturedButtonWidget;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.util.function.Mapper;
import net.labymod.core.client.accessor.gui.ImageButtonAccessor;
import net.minecraft.client.gui.components.AbstractButton;

public class TexturedButtonMapper implements Mapper<AbstractButton, ButtonWidget> {

  @Override
  public ButtonWidget map(AbstractButton source) {
    if (!(source instanceof ModMenuTexturedButtonWidget imageButton)) {
      return null;
    }

    var accessor = (ImageButtonAccessor) imageButton;
    var location = accessor.getResourceLocation();

    // Create icon
    Icon icon = Icon.sprite(
        location,
        accessor.getXTexStart(), accessor.getYTexStart(),
        source.getWidth(),
        source.getHeight(),
        accessor.getTextureWidth(),
        accessor.getTextureHeight()
    );
    icon.setHoverOffset(0, accessor.getYDiffTex());

    // Create button
    ButtonWidget button = new ButtonWidget();
    button.icon().set(icon);

    return button;
  }
}
