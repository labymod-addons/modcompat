package net.labymod.addons.modcompat.v1_20_2.mixins.modmenu.config;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.terraformersmc.modmenu.config.ModMenuConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(value = ModMenuConfig.class, remap = false)
public class MixinModMenuConfig {

  @WrapOperation(method = "asOptions", at = @At(value = "INVOKE", target = "Ljava/lang/String;equals(Ljava/lang/Object;)Z"))
  private static boolean modcompat$removeButtonStyleOption(
      String instance,
      Object object,
      Operation<Boolean> original
  ) {
    return original.call(instance, object) || instance.equals("GAME_MENU_BUTTON_STYLE");
  }
}
