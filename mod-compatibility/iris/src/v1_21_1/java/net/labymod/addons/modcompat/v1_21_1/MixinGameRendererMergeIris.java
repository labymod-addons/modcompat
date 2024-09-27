package net.labymod.addons.modcompat.v1_21_1;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.labymod.addons.modcompat.iris.IrisCompat;
import net.labymod.core.main.LabyMod;
import net.labymod.v1_21_1.client.util.MinecraftUtil;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Quaternionfc;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Mixin that merges the redirects from Iris and LabyMod to the same methods.
 */
@Mixin(GameRenderer.class)
public abstract class MixinGameRendererMergeIris {

  @Shadow
  @Final
  Minecraft minecraft;
  @Shadow
  @Final
  private Camera mainCamera;

  @Shadow
  protected abstract void bobView(PoseStack poseStack, float tickDelta);

  @Shadow
  protected abstract void bobHurt(PoseStack poseStack, float tickDelta);

  @WrapWithCondition(
      method = "renderLevel",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;bobView(Lcom/mojang/blaze3d/vertex/PoseStack;F)V")
  )
  private boolean modcompat$stopBobbing(
      GameRenderer instance, PoseStack poseStack, float partialTicks
  ) {
    return !IrisCompat.api().isShaderActive();
  }

  @WrapOperation(
      method = "renderLevel",
      at = @At(value = "INVOKE", target = "Lorg/joml/Matrix4f;rotation(Lorg/joml/Quaternionfc;)Lorg/joml/Matrix4f;")
  )
  private Matrix4f modcompat$applyBobbingSetViewMatrix(
      Matrix4f self, Quaternionfc quat, Operation<Matrix4f> original
  ) {
    if (!IrisCompat.api().isShaderActive()) {
      return original.call(self, quat);
    }

    PoseStack stack = new PoseStack();
    stack.last().pose().set(self);

    float partialTicks = this.mainCamera.getPartialTickTime();

    this.bobHurt(stack, partialTicks);
    if (this.minecraft.options.bobView().get()
        && !LabyMod.getInstance().config().ingame().noViewBobbing().get()) {
      // Merge: Do not apply view bobbing to model view matrix if LabyMods view bobbing is disabled
      this.bobView(stack, partialTicks);
    }

    self.set(stack.last().pose());

    // Store rotation from bobbing
    AxisAngle4f rotation = new AxisAngle4f();
    self.getRotation(rotation);

    // Apply LabyMod's eye height transform, which overrides the rotation from bobbing
    original.call(self, quat);

    // Restore rotation from bobbing and add actual rotation
    self.rotation(rotation);
    self.rotate(quat);

    // Update LabyMod's view matrix
    return MinecraftUtil.setViewMatrix(self);
  }
}
