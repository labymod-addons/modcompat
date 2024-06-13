package net.labymod.addons.modcompat.v1_21.mixins.modmenu.widget;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.blaze3d.vertex.MeshData;
import com.terraformersmc.modmenu.gui.widget.DescriptionListWidget;
import net.labymod.api.Laby;
import net.labymod.core.client.gui.screen.theme.fancy.FancyTheme;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(DescriptionListWidget.class)
public class MixinDescriptionListWidget {

  @WrapWithCondition(method = "renderListItems", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/BufferUploader;drawWithShader(Lcom/mojang/blaze3d/vertex/MeshData;)V"))
  private boolean modcompat$removeDirtBackground(MeshData meshData) {
    return !(Laby.labyAPI().themeService().currentTheme() instanceof FancyTheme);
  }
}
