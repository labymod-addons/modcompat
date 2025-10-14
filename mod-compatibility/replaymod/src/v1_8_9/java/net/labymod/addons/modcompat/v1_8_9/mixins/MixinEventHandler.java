package net.labymod.addons.modcompat.v1_8_9.mixins;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com/replaymod/lib/de/johni0702/minecraft/gui/container/VanillaGuiScreen$EventHandler")
public class MixinEventHandler {

  @Dynamic
  @Inject(method = "onMouseInput", at = @At("HEAD"), cancellable = true, remap = false)
  private void modcompat$disableMouseInput(CallbackInfo ci) {
    ci.cancel();
  }

  @Dynamic
  @Inject(method = "onKeyboardInput", at = @At("HEAD"), cancellable = true, remap = false)
  private void modcompat$disableKeyboardInput(CallbackInfo ci) {
    ci.cancel();
  }

}
