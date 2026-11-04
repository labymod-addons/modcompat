package net.labymod.addons.modcompat.gui;

import java.util.List;
import java.util.function.BooleanSupplier;
import net.labymod.addons.modcompat.ModCompatAddon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.util.WidgetUtil;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.entry.FlexibleContentEntry;
import net.labymod.api.client.gui.tooltip.Tooltip;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.gui.screen.ActivityInitializeEvent;

/**
 * Adds a mod's icon button to the first button row of the private server list, the place where
 * the mod adds its button on the vanilla multiplayer screen.
 */
public class ServerListButtonListener {

  private static final String PRIVATE_SERVER_LIST_ID = "labymod:private_server_list";
  private static final String BUTTON_CONTAINER_ID = "button-container";
  private static final String BUTTON_ID = "modcompat-server-list-button";
  private static final String STYLE = "server-list-button.lss";

  private final String name;
  private final ResourceLocation icon;
  private final BooleanSupplier visible;
  private final Runnable action;

  public ServerListButtonListener(
      String name,
      ResourceLocation icon,
      BooleanSupplier visible,
      Runnable action
  ) {
    this.name = name;
    this.icon = icon;
    this.visible = visible;
    this.action = action;
  }

  @Subscribe
  public void onActivityInitialize(ActivityInitializeEvent event) {
    if (!PRIVATE_SERVER_LIST_ID.equals(event.getIdentifier()) || !this.visible.getAsBoolean()) {
      return;
    }

    Activity activity = event.activity();
    FlexibleContentWidget buttonContainer = findButtonContainer(activity.document());
    if (buttonContainer == null) {
      return;
    }

    List<FlexibleContentEntry> rows = buttonContainer.getChildren();
    if (rows.isEmpty() || !(rows.getFirst().childWidget() instanceof FlexibleContentWidget firstRow)) {
      return;
    }

    activity.addStyle(ModCompatAddon.NAMESPACE, STYLE);

    ButtonWidget button = ButtonWidget.icon(Icon.texture(this.icon), this.action::run);
    button.addId(BUTTON_ID);
    button.tooltip(Tooltip.text(Component.text(this.name)));
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
