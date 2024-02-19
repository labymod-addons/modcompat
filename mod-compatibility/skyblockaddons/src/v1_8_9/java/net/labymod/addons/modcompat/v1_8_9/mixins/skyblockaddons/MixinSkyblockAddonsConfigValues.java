package net.labymod.addons.modcompat.v1_8_9.mixins.skyblockaddons;

import codes.biscuit.skyblockaddons.config.ConfigValues;
import codes.biscuit.skyblockaddons.core.Feature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(value = ConfigValues.class, remap = false)
public class MixinSkyblockAddonsConfigValues {

  @Inject(method = "getActualX", at = @At("HEAD"), cancellable = true)
  private void modcompat$removeActualX(Feature feature, CallbackInfoReturnable<Float> cir) {
    cir.setReturnValue(0.0F);
  }

  @Inject(method = "getActualY", at = @At("HEAD"), cancellable = true)
  private void modcompat$removeActualY(Feature feature, CallbackInfoReturnable<Float> cir) {
    cir.setReturnValue(0.0F);
  }

  // TODO: The sizes are used for resizable features (bars)

  @Inject(method = "getSizesX", at = @At("HEAD"), cancellable = true)
  private void modcompat$removeSizesX(Feature feature, CallbackInfoReturnable<Float> cir) {
    cir.setReturnValue(1.0F);
  }

  @Inject(method = "getSizesY", at = @At("HEAD"), cancellable = true)
  private void modcompat$removeSizesY(Feature feature, CallbackInfoReturnable<Float> cir) {
    cir.setReturnValue(1.0F);
  }
}
