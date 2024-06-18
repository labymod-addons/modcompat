package net.labymod.addons.modcompat.v1_21.mixins.modmenu.config;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.terraformersmc.modmenu.config.ModMenuConfig;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import java.lang.reflect.Field;

@Pseudo
@Mixin(value = ModMenuConfig.class, remap = false)
public class MixinModMenuConfig {

  @Dynamic("Support old version of Mod Menu")
  @WrapOperation(method = "asOptions", at = @At(value = "INVOKE", target = "Ljava/lang/String;equals(Ljava/lang/Object;)Z"), require = 0, expect = 0)
  private static boolean modcompat$removeButtonStyleOptionOld(
      String instance,
      Object object,
      Operation<Boolean> original
  ) {
    return original.call(instance, object) || instance.equals("GAME_MENU_BUTTON_STYLE");
  }

  @WrapOperation(method = "asOptions", at = @At(value = "INVOKE", target = "Ljava/lang/reflect/Field;isAnnotationPresent(Ljava/lang/Class;)Z"), require = 0, expect = 0)
  private static boolean modcompat$removeButtonStyleOption(
      Field instance, Class<?> clazz, Operation<Boolean> original
  ) {
    return original.call(instance, clazz) || instance.getName().equals("GAME_MENU_BUTTON_STYLE");
  }
}
