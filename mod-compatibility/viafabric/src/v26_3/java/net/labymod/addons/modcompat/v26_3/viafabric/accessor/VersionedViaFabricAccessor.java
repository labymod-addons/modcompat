package net.labymod.addons.modcompat.v26_3.viafabric.accessor;

import com.viaversion.fabric.ViaFabric;
import com.viaversion.fabric.gui.ViaConfigScreen;
import net.labymod.addons.modcompat.viafabric.ViaFabricAccessor;
import net.labymod.api.service.annotation.AutoService;
import net.minecraft.client.Minecraft;

@AutoService(value = ViaFabricAccessor.class, versionSpecific = true)
public class VersionedViaFabricAccessor implements ViaFabricAccessor {

  @Override
  public boolean isButtonHidden() {
    return ViaFabric.config.isHideButton();
  }

  @Override
  public void openConfigScreen() {
    Minecraft minecraft = Minecraft.getInstance();
    minecraft.gui.setScreen(new ViaConfigScreen(minecraft.gui.screen()));
  }
}
