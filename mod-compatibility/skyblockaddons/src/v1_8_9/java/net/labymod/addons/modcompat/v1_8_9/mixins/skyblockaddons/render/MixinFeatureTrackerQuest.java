package net.labymod.addons.modcompat.v1_8_9.mixins.skyblockaddons.render;

import codes.biscuit.skyblockaddons.features.EntityOutlines.FeatureTrackerQuest;
import net.labymod.addons.modcompat.v1_8_9.skyblockaddons.FeatureDrawContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(value = FeatureTrackerQuest.class, remap = false)
public class MixinFeatureTrackerQuest {

  @Inject(method = "drawTrackerLocationIndicator", at = @At(value = "INVOKE", target = "Lcodes/biscuit/skyblockaddons/listeners/RenderListener;transformXY(FIF)F", shift = Shift.AFTER, ordinal = 1), cancellable = true)
  private static void modcompat$noFeatureRender(CallbackInfo ci) {
    if (FeatureDrawContext.get().isActive() && FeatureDrawContext.get().isNoRender()) {
      // Disable rendering, call was only to obtain size information
      ci.cancel();
    }
  }
}
