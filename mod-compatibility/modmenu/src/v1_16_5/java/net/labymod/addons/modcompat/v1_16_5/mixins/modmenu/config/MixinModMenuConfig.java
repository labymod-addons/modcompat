package net.labymod.addons.modcompat.v1_16_5.mixins.modmenu.config;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.terraformersmc.modmenu.config.ModMenuConfig;
import java.util.Set;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(value = ModMenuConfig.class, remap = false)
public class MixinModMenuConfig {

  @WrapOperation(method = "asOptions", at = @At(value = "INVOKE", target = "Ljava/util/Set;contains(Ljava/lang/Object;)Z"))
  private static boolean modcompat$removeButtonStyleOption(
      Set instance,
      Object object,
      Operation<Boolean> original
  ) {
    return original.call(instance, object) || object.equals("MODS_BUTTON_STYLE");
  }
}
