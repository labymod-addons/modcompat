package net.labymod.addons.modcompat.v1_8_9.skyblockaddons.configuration.settings.widget;

import codes.biscuit.skyblockaddons.utils.EnumUtils.BackpackStyle;
import codes.biscuit.skyblockaddons.utils.EnumUtils.ChromaMode;
import codes.biscuit.skyblockaddons.utils.EnumUtils.PowerOrbDisplayStyle;
import codes.biscuit.skyblockaddons.utils.EnumUtils.TextStyle;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.renderer.EntryRenderer;
import net.labymod.api.client.render.font.RenderableComponent;

public class SkyblockAddonsEntryRenderer<T> implements EntryRenderer<T> {

  @Override
  public Widget createEntryWidget(T entry) {
    return ComponentWidget.component(this.toComponent(entry));
  }

  @Override
  public float getWidth(T entry, float maxWidth) {
    return this.toRenderableComponent(entry, maxWidth).getWidth();
  }

  @Override
  public float getHeight(T entry, float maxWidth) {
    return this.toRenderableComponent(entry, maxWidth).getHeight();
  }

  private Component toComponent(T entry) {
    if (entry instanceof TextStyle textStyle) {
      return Component.text(textStyle.getMessage());
    }
    if (entry instanceof ChromaMode chromaMode) {
      return Component.text(chromaMode.getMessage());
    }
    if (entry instanceof PowerOrbDisplayStyle powerOrbDisplayStyle) {
      return Component.text(powerOrbDisplayStyle.getMessage());
    }
    if (entry instanceof BackpackStyle backpackStyle) {
      return Component.text(backpackStyle.getMessage());
    }
    return Component.text(entry.toString());
  }

  private RenderableComponent toRenderableComponent(T entry, float maxWidth) {
    return RenderableComponent.builder()
        .maxWidth(maxWidth)
        .disableCache()
        .format(this.toComponent(entry));
  }
}
