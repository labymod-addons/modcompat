package net.labymod.addons.modcompat.v1_8_9.mixins.skyblockaddons;

import codes.biscuit.skyblockaddons.gui.LocationEditGui;
import codes.biscuit.skyblockaddons.gui.buttons.ButtonLocation;
import codes.biscuit.skyblockaddons.listeners.RenderListener;
import net.labymod.addons.modcompat.v1_8_9.skyblockaddons.FeatureDrawContext;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(value = RenderListener.class, remap = false)
public class MixinSkyblockAddonsRenderListener {

  @ModifyConstant(method = "drawSkeletonBar", constant = @Constant(classValue = LocationEditGui.class))
  public Class<?> modcompat$adjustEditorCheck(
      Object instance, Class<?> constant, Minecraft mc, float scale, ButtonLocation buttonLocation
  ) {
    // Check button location instead of the current screen
    return buttonLocation == null ? Void.class : Object.class;
  }

  @Inject(method = "renderOverlays", at = @At("HEAD"), cancellable = true)
  private void modcompat$cancelRenderOverlays(CallbackInfo ci) {
    // Disable rendering, already rendered via the hud widgets
    ci.cancel();
  }

  @Inject(method = "renderTimersOnly", at = @At("HEAD"), cancellable = true)
  private void modcompat$cancelRenderTimersOnly(CallbackInfo ci) {
    // Disable rendering, already rendered via the hud widgets
    ci.cancel();
  }

  @Inject(method = "transformXY", at = @At("HEAD"), cancellable = true)
  private void modcompat$cancelTransformXY(
      float xy,
      int widthHeight,
      float scale,
      CallbackInfoReturnable<Float> cir
  ) {
    // This method is used by every feature to determine the x and y coordinates, so we can use it
    // to obtain the width and height of the feature
    FeatureDrawContext featureDrawContext = FeatureDrawContext.get();
    // Check if a draw context is active
    if (featureDrawContext.isActive()) {
      if (featureDrawContext.getWidth() == 0) {
        // Assuming that this is the first call to transformXY of the draw context, because the width
        // is still zero and the first call is always for the x coordinate
        featureDrawContext.setWidth(widthHeight);
      } else {
        // Otherwise, assuming that this is the second call to transformXY, which is always for the y coordinate
        featureDrawContext.setHeight(widthHeight);
      }

      // Disable transformation as handled by the widget editor
      cir.setReturnValue(xy);
    }
  }
}
