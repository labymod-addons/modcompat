package net.labymod.addons.modcompat.v1_20_6.mixins.modmenu.widget;

import com.mojang.blaze3d.vertex.Tesselator;
import com.terraformersmc.modmenu.gui.widget.DescriptionListWidget;
import net.labymod.api.Laby;
import net.labymod.core.client.gui.screen.theme.fancy.FancyTheme;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(DescriptionListWidget.class)
public class MixinDescriptionListWidget {

  @Redirect(method = "renderListItems", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/Tesselator;end()V"))
  private void modcompat$removeDirtBackground(Tesselator instance) {
    if (Laby.labyAPI().themeService().currentTheme() instanceof FancyTheme) {
      instance.getBuilder().end().release();
    } else {
      instance.end();
    }
  }
}
