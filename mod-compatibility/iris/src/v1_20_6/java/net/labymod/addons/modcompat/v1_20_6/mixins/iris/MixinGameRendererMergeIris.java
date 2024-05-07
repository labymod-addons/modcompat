package net.labymod.addons.modcompat.v1_20_6.mixins.iris;

import com.mojang.blaze3d.vertex.PoseStack;
import net.labymod.addons.modcompat.iris.IrisCompat;
import net.labymod.core.main.LabyMod;
import net.labymod.v1_20_6.client.util.MinecraftUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Mixin that merges the redirects from Iris and LabyMod to the same methods.
 */
@Mixin(GameRenderer.class)
public abstract class MixinGameRendererMergeIris {

  @Shadow
  @Final
  Minecraft minecraft;

  @Shadow
  protected abstract void bobView(PoseStack poseStack, float tickDelta);

  @Shadow
  protected abstract void bobHurt(PoseStack poseStack, float tickDelta);

  @Redirect(
      method = "renderLevel",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;bobView(Lcom/mojang/blaze3d/vertex/PoseStack;F)V")
  )
  private void modcompat$stopBobbing(
      GameRenderer instance, PoseStack poseStack, float tickDelta
  ) {
    if (!IrisCompat.api().isShaderActive()
        && !LabyMod.getInstance().config().ingame().noViewBobbing().get()) {
      // Merge: Stop bobbing if either shaders are on or if LabyMods view bobbing is disabled
      this.bobView(poseStack, tickDelta);
    }
  }

  @Redirect(
      method = "renderLevel",
      at = @At(value = "INVOKE", target = "Lorg/joml/Matrix4f;rotationXYZ(FFF)Lorg/joml/Matrix4f;")
  )
  private Matrix4f modcompat$applyBobbingSetViewMatrix(
      Matrix4f instance, float angleX, float angleY, float angleZ, float tickDelta
  ) {
    if (!IrisCompat.api().isShaderActive()) {
      // Merge: Set the LabyMod view matrix
      return MinecraftUtil.setViewMatrix(instance.rotationXYZ(angleX, angleY, angleZ));
    }

    PoseStack stack = new PoseStack();
    stack.last().pose().set(instance);

    this.bobHurt(stack, tickDelta);
    if (this.minecraft.options.bobView().get()
        && !LabyMod.getInstance().config().ingame().noViewBobbing().get()) {
      // Merge: Do not apply view bobbing to model view matrix if LabyMods view bobbing is disabled
      this.bobView(stack, tickDelta);
    }

    instance.set(stack.last().pose());
    // Merge: Set the LabyMod view matrix
    return MinecraftUtil.setViewMatrix(instance.rotateXYZ(angleX, angleY, angleZ));
  }
}
