package net.labymod.addons.modcompat.v1_21_8.mixins.modmenu.config;

import com.terraformersmc.modmenu.config.ModMenuConfig.GameMenuButtonStyle;
import com.terraformersmc.modmenu.config.option.EnumConfigOption;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(value = EnumConfigOption.class, remap = false)
public class MixinEnumConfigOption {

  @Shadow
  @Final
  private Enum<?> defaultValue;

  @Inject(method = "getValue", at = @At("HEAD"), cancellable = true)
  private void modcompat$forceIconButtonStyle(CallbackInfoReturnable<Enum<?>> cir) {
    if (this.defaultValue instanceof GameMenuButtonStyle) {
      cir.setReturnValue(GameMenuButtonStyle.ICON);
    }
  }
}