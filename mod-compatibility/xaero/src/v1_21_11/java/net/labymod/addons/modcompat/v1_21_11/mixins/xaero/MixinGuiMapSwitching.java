package net.labymod.addons.modcompat.v1_21_11.mixins.xaero;

import net.labymod.addons.modcompat.xaero.XaeroRetainedGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The world map screen clears the main render target and draws the map outside the gui render
 * state. It calls {@code preMapRender} on every frame it draws.
 */
@Pseudo
@Mixin(targets = "xaero.map.gui.GuiMapSwitching", remap = false)
public class MixinGuiMapSwitching {

  @Inject(method = "preMapRender", at = @At("HEAD"), require = 0, remap = false)
  private void modcompat$buildNextGui(CallbackInfo ci) {
    XaeroRetainedGui.onDraw();
  }
}
