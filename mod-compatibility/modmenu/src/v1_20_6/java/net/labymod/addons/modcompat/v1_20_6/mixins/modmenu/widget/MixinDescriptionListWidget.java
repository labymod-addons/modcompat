package net.labymod.addons.modcompat.v1_20_6.mixins.modmenu.widget;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.Tesselator;
import com.terraformersmc.modmenu.gui.widget.DescriptionListWidget;
import net.labymod.api.Laby;
import net.labymod.core.client.gui.screen.theme.fancy.FancyTheme;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(DescriptionListWidget.class)
public class MixinDescriptionListWidget {

  @WrapOperation(method = "renderListItems", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/Tesselator;end()V"))
  private void modcompat$removeDirtBackground(Tesselator instance, Operation<Void> original) {
    if (Laby.labyAPI().themeService().currentTheme() instanceof FancyTheme) {
      instance.getBuilder().end().release();
    } else {
      original.call(instance);
    }
  }
}
