package net.labymod.addons.modcompat.v1_8_9.mixins.skyblockaddons;

import codes.biscuit.skyblockaddons.core.Feature;
import net.labymod.addons.modcompat.v1_8_9.skyblockaddons.event.SkyblockAddonsFeatureToggleEvent;
import net.labymod.api.Laby;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(value = Feature.class, remap = false)
public class MixinFeature {

  @Inject(method = "onToggle", at = @At("HEAD"))
  private void modcompat$callFeatureToggleEvent(CallbackInfo ci) {
    Laby.fireEvent(new SkyblockAddonsFeatureToggleEvent((Feature) (Object) this));
  }
}
