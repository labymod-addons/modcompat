package net.labymod.addons.modcompat.v1_8_9.mixins.skyblockaddons.gui;

import codes.biscuit.skyblockaddons.gui.SettingsGui;
import codes.biscuit.skyblockaddons.utils.EnumUtils;
import codes.biscuit.skyblockaddons.utils.EnumUtils.FeatureSetting;
import net.labymod.addons.modcompat.v1_8_9.SkyblockAddonsCompat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SettingsGui.class, remap = false)
public class MixinSettingsGui {

  @Inject(method = "addButton", at = @At("HEAD"), cancellable = true)
  private void modcompat$removeScaleSetting(EnumUtils.FeatureSetting setting, CallbackInfo ci) {
    if (SkyblockAddonsCompat.isFeatureIntegration() && setting == FeatureSetting.GUI_SCALE) {
      // Remove the scale setting, scaling should be done in the LabyMod widget editor
      ci.cancel();
    }
  }
}
