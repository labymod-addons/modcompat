package net.labymod.addons.modcompat.configuration.settings;

import java.util.function.Supplier;
import net.labymod.api.client.component.Component;
import net.labymod.api.configuration.settings.type.SettingElement;

public class CustomNameSettingElement extends SettingElement {

  private final Supplier<Component> componentSupplier;

  public CustomNameSettingElement(String id, Supplier<Component> componentSupplier) {
    super(id, null, null, new String[0]);
    this.componentSupplier = componentSupplier;
  }

  @Override
  public Component displayName() {
    return this.componentSupplier.get();
  }
}
