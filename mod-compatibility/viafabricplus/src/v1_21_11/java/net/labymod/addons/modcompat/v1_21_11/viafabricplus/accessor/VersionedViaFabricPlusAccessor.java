package net.labymod.addons.modcompat.v1_21_11.viafabricplus.accessor;

import com.viaversion.viafabricplus.ViaFabricPlus;
import net.labymod.addons.modcompat.viafabricplus.ViaFabricPlusAccessor;
import net.labymod.api.service.annotation.AutoService;
import net.minecraft.client.Minecraft;

@AutoService(value = ViaFabricPlusAccessor.class, versionSpecific = true)
public class VersionedViaFabricPlusAccessor implements ViaFabricPlusAccessor {

  @Override
  public void openProtocolSelectionScreen() {
    ViaFabricPlus.getImpl().openProtocolSelectionScreen(Minecraft.getInstance().screen);
  }
}
