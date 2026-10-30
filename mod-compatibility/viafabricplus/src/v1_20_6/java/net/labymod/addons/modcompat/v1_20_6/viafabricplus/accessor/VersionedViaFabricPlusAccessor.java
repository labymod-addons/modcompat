package net.labymod.addons.modcompat.v1_20_6.viafabricplus.accessor;

import de.florianmichael.viafabricplus.screen.base.ProtocolSelectionScreen;
import net.labymod.addons.modcompat.viafabricplus.ViaFabricPlusAccessor;
import net.labymod.api.service.annotation.AutoService;
import net.minecraft.client.Minecraft;

@AutoService(value = ViaFabricPlusAccessor.class, versionSpecific = true)
public class VersionedViaFabricPlusAccessor implements ViaFabricPlusAccessor {

  @Override
  public void openProtocolSelectionScreen() {
    ProtocolSelectionScreen.INSTANCE.open(Minecraft.getInstance().screen);
  }
}
