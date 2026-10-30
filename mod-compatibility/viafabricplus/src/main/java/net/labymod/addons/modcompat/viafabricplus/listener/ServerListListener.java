package net.labymod.addons.modcompat.viafabricplus.listener;

import java.util.List;
import net.labymod.addons.modcompat.viafabricplus.ViaFabricPlusAccessor;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.util.WidgetUtil;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.entry.FlexibleContentEntry;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.gui.screen.ActivityInitializeEvent;

public class ServerListListener {

  private static final String PRIVATE_SERVER_LIST_ID = "labymod:private_server_list";
  private static final String BUTTON_CONTAINER_ID = "button-container";
  private static final ResourceLocation ICON = ResourceLocation.create(
      "viafabricplus",
      "icon.png"
  );

  private final ViaFabricPlusAccessor accessor;

  public ServerListListener(ViaFabricPlusAccessor accessor) {
    this.accessor = accessor;
  }

  @Subscribe
  public void onActivityInitialize(ActivityInitializeEvent event) {
    if (!PRIVATE_SERVER_LIST_ID.equals(event.getIdentifier())) {
      return;
    }

    FlexibleContentWidget buttonContainer = findButtonContainer(event.activity().document());
    if (buttonContainer == null) {
      return;
    }

    List<FlexibleContentEntry> rows = buttonContainer.getChildren();
    if (rows.isEmpty() || !(rows.get(0).childWidget() instanceof FlexibleContentWidget firstRow)) {
      return;
    }

    ButtonWidget button = ButtonWidget.icon(
        Icon.texture(ICON),
        this.accessor::openProtocolSelectionScreen
    );
    button.setHoverComponent(Component.text("ViaFabricPlus"));
    firstRow.addContentInitialized(button);
  }

  // getChildRecursive stops at WrappedWidgets like FlexibleContentEntry, so unwrap them here
  private static FlexibleContentWidget findButtonContainer(Widget widget) {
    Widget unwrapped = WidgetUtil.unwrapWidget(widget);
    if (unwrapped instanceof FlexibleContentWidget container
        && container.hasId(BUTTON_CONTAINER_ID)) {
      return container;
    }

    for (Widget child : unwrapped.getChildren()) {
      FlexibleContentWidget found = findButtonContainer(child);
      if (found != null) {
        return found;
      }
    }
    return null;
  }
}
