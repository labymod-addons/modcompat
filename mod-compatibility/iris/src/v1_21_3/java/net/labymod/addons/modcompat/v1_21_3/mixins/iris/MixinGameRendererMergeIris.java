package net.labymod.addons.modcompat.v1_21_3.mixins.iris;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.labymod.addons.modcompat.iris.IrisCompat;
import net.labymod.core.main.LabyMod;
import net.labymod.v1_21_3.client.util.MinecraftUtil;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
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
  private int confusionAnimationTick;

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
      Matrix4f instance, Quaternionfc quat, Operation<Matrix4f> original, DeltaTracker deltaTracker
  ) {
    if (!IrisCompat.api().isShaderActive()) {
      return original.call(instance, quat);
    }

    PoseStack stack = new PoseStack();
    stack.last().pose().set(instance);

    float partialTicks = this.mainCamera.getPartialTickTime();

    this.bobHurt(stack, partialTicks);
    if (this.minecraft.options.bobView().get()
        && !LabyMod.getInstance().config().ingame().noViewBobbing().get()) {
      // Merge: Do not apply view bobbing to model view matrix if LabyMods view bobbing is disabled
      this.bobView(stack, partialTicks);
    }

    instance.set(stack.last().pose());

    // Minecraft confusion logic, replaced by Iris as well
    float f = deltaTracker.getGameTimeDeltaPartialTick(false);
    float h = this.minecraft.options.screenEffectScale().get().floatValue();
    float i = Mth.lerp(f, this.minecraft.player.oSpinningEffectIntensity,
        this.minecraft.player.spinningEffectIntensity) * h * h;
    if (i > 0.0F) {
      int j = this.minecraft.player.hasEffect(MobEffects.CONFUSION) ? 7 : 20;
      float k = 5.0F / (i * i + 5.0F) - i * 0.04F;
      k *= k;
      Vector3f vector3f = new Vector3f(0.0F, Mth.SQRT_OF_TWO / 2.0F, Mth.SQRT_OF_TWO / 2.0F);
      float l = ((float) this.confusionAnimationTick + f) * (float) j * (float) (Math.PI / 180.0);
      instance.rotate(l, vector3f);
      instance.scale(1.0F / k, 1.0F, 1.0F);
      instance.rotate(-l, vector3f);
    }

    // Store rotation from the modifications
    AxisAngle4f rotation = new AxisAngle4f();
    instance.getRotation(rotation);

    // Apply LabyMod's eye height transform, which overrides the rotation from the modifications
    original.call(instance, quat);

    // Restore rotation from the modifications and add actual rotation
    instance.rotation(rotation);
    instance.rotate(quat);

    // Update LabyMod's view matrix
    return MinecraftUtil.setViewMatrix(instance);
  }
}
