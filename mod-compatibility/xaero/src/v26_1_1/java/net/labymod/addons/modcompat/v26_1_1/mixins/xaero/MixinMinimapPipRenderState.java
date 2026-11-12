package net.labymod.addons.modcompat.v26_1_1.mixins.xaero;

import net.labymod.addons.modcompat.xaero.XaeroRetainedGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * The minimap draws into a picture in picture state, which a frame that draws the kept gui again
 * doesn't prepare.
 */
@Pseudo
@Mixin(targets = "xaero.hud.minimap.render.MinimapPipRenderState", remap = false)
public class MixinMinimapPipRenderState {

  @Inject(method = "update", at = @At("HEAD"), require = 0, remap = false)
  private void modcompat$buildNextGui(CallbackInfoReturnable<Object> cir) {
    XaeroRetainedGui.onDraw();
  }
}
