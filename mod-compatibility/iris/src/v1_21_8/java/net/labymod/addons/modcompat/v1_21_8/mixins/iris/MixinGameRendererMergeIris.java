package net.labymod.addons.modcompat.v1_21_8.mixins.iris;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.blaze3d.vertex.PoseStack;
import net.labymod.addons.modcompat.iris.IrisCompat;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public abstract class MixinGameRendererMergeIris {

  @WrapWithCondition(
      method = "renderLevel",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;bobView(Lcom/mojang/blaze3d/vertex/PoseStack;F)V")
  )
  private boolean modcompat$stopBobbing(
      GameRenderer instance, PoseStack poseStack, float partialTicks
  ) {
    return !IrisCompat.api().isShaderActive();
  }

}
