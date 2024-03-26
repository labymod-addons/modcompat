package net.labymod.addons.modcompat.v1_19_3.mixins.modmenu.config;

import com.terraformersmc.modmenu.config.ModMenuConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(value = ModMenuConfig.class, remap = false)
public class MixinModMenuConfig {

  @Redirect(method = "asOptions", at = @At(value = "INVOKE", target = "Ljava/lang/String;equals(Ljava/lang/Object;)Z"))
  private static boolean modcompat$removeButtonStyleOption(String instance, Object object) {
    return instance.equals(object) || instance.equals("MODS_BUTTON_STYLE");
  }
}
