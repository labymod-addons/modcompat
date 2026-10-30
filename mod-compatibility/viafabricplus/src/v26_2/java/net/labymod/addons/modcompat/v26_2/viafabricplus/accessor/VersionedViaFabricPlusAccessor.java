package net.labymod.addons.modcompat.v26_2.viafabricplus.accessor;

import com.viaversion.viafabricplus.ViaFabricPlus;
import net.labymod.addons.modcompat.viafabricplus.ViaFabricPlusAccessor;
import net.labymod.api.service.annotation.AutoService;
import net.minecraft.client.Minecraft;

@AutoService(value = ViaFabricPlusAccessor.class, versionSpecific = true)
public class VersionedViaFabricPlusAccessor implements ViaFabricPlusAccessor {

  @Override
  public void openProtocolSelectionScreen() {
    ViaFabricPlus.api().screens().openViaFabricPlusScreen(Minecraft.getInstance().gui.screen());
  }
}
