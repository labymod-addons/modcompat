package net.labymod.addons.modcompat.v1_8_9.mixins.skyblockaddons;

import codes.biscuit.skyblockaddons.listeners.RenderListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(value = RenderListener.class, remap = false)
public class MixinSkyblockAddonsRenderListener {

  @Inject(method = "renderOverlays", at = @At("HEAD"), cancellable = true)
  private void modcompat$cancelRenderOverlays(CallbackInfo ci) {
    ci.cancel();
  }

  @Inject(method = "renderTimersOnly", at = @At("HEAD"), cancellable = true)
  private void modcompat$cancelRenderTimersOnly(CallbackInfo ci) {
    ci.cancel();
  }

  @Inject(method = "transformXY", at = @At("HEAD"), cancellable = true)
  private void modcompat$cancelTransformXY(
      float xy,
      int widthHeight,
      float scale,
      CallbackInfoReturnable<Float> cir
  ) {
    cir.setReturnValue(xy);
  }
}
