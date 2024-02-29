package net.labymod.addons.modcompat.replaymod.settings;


import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.renderer.EntryRenderer;
import net.labymod.api.client.render.font.RenderableComponent;

public class ReplayModEntryRenderer<T> implements EntryRenderer<T> {

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
    return Component.translatable(entry.toString());
  }

  private RenderableComponent toRenderableComponent(T entry, float maxWidth) {
    return RenderableComponent.builder()
        .maxWidth(maxWidth)
        .disableCache()
        .format(this.toComponent(entry));
  }
}
