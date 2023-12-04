package net.labymod.addons.modcompat.replaymod.listener;

import com.replaymod.replay.ReplayModReplay;
import com.replaymod.replay.gui.screen.GuiReplayViewer;
import net.labymod.addons.modcompat.ModCompatAddon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.widget.AbstractWidget;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.WrappedWidget;
import net.labymod.api.client.gui.screen.widget.widgets.activity.Document;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.gui.screen.ActivityInitializeEvent;

import java.util.ArrayList;
import java.util.List;

public class MainMenuListener {

  private static final String MAIN_MENU_ID = "labymod:main_menu";
  private static final ResourceLocation REPLAY_VIEWER_ICON = ResourceLocation.create(
      "replaymod",
      "logo_button.png"
  );

  @Subscribe
  public void onActivityInitialize(ActivityInitializeEvent event) {
    if (MAIN_MENU_ID.equals(event.getIdentifier())) {
      this.addButton(event.activity());
    }
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private void addButton(Activity activity) {
    Document document = activity.document();

    activity.addStyle(ModCompatAddon.NAMESPACE, "replaymod/main-menu.lss");

    List<Widget> widgets = new ArrayList<>();
    document.traverse(widgets, widget -> widget.hasId("buttons"));

    if (!widgets.isEmpty()) {
      AbstractWidget widget = this.getWidget(widgets.get(0));

      ButtonWidget buttonWidget = ButtonWidget.icon(Icon.texture(REPLAY_VIEWER_ICON));
      buttonWidget.setHoverComponent(Component.translatable("replaymod.gui.replayviewer"));
      buttonWidget.setPressable(() -> new GuiReplayViewer(ReplayModReplay.instance).display());
      buttonWidget.addId("icon-button", "replay-viewer-button");

      widget.addChildInitialized(buttonWidget);
    }
  }

  @SuppressWarnings("rawtypes")
  private AbstractWidget getWidget(Widget widget) {
    if (widget instanceof WrappedWidget wrappedWidget) {
      return this.getWidget(wrappedWidget.childWidget());
    }

    return (AbstractWidget) widget;
  }
}
