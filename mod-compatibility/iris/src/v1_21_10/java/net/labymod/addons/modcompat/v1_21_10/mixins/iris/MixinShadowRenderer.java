package net.labymod.addons.modcompat.v1_21_10.mixins.iris;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.irisshaders.iris.mixin.LevelRendererAccessor;
import net.irisshaders.iris.shadows.ShadowRenderer;
import net.labymod.api.Laby;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.render.matrix.VanillaStackAccessor;
import net.labymod.core.event.client.render.world.RenderWorldEventCaller;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.CameraRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShadowRenderer.class)
public class MixinShadowRenderer {

  @Inject(method = "renderShadows", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/feature/FeatureRenderDispatcher;renderAllFeatures()V", shift = Shift.AFTER))
  private void labyMod$renderLabyModFeatures(
      LevelRendererAccessor levelRenderer,
      Camera playerCamera,
      CameraRenderState renderState,
      CallbackInfo ci,
      @Local PoseStack poseStack
  ) {
    Stack stack = ((VanillaStackAccessor) poseStack).stack();
    RenderWorldEventCaller.callPost(
        stack,
        Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false)
    );
  }

}
