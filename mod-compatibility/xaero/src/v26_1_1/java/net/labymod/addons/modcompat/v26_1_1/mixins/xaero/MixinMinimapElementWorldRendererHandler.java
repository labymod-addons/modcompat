package net.labymod.addons.modcompat.v26_1_1.mixins.xaero;

import net.labymod.addons.modcompat.xaero.XaeroRetainedGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * In-world waypoints draw straight to the screen with their own render pass. Older minimap versions
 * do that while the hud is built.
 */
@Pseudo
@Mixin(targets = "xaero.hud.minimap.element.render.world.MinimapElementWorldRendererHandler", remap = false)
public class MixinMinimapElementWorldRendererHandler {

  @Inject(method = "render", at = @At("HEAD"), require = 0, remap = false)
  private void modcompat$buildNextGui(CallbackInfo ci) {
    XaeroRetainedGui.onDraw();
  }
}
