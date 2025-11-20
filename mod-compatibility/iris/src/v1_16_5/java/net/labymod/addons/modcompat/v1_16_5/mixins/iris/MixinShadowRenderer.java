package net.labymod.addons.modcompat.v1_16_5.mixins.iris;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.coderbot.iris.mixin.LevelRendererAccessor;
import net.coderbot.iris.pipeline.ShadowRenderer;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.render.matrix.VanillaStackAccessor;
import net.labymod.core.event.client.render.world.RenderWorldEventCaller;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShadowRenderer.class)
public class MixinShadowRenderer {

  @Inject(method = "renderShadows", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;endBatch()V", shift = Shift.AFTER))
  private void labyMod$renderLabyModFeatures(
      LevelRendererAccessor par1,
      Camera par2,
      CallbackInfo ci,
      @Local PoseStack poseStack
  ) {
    Stack stack = ((VanillaStackAccessor) poseStack).stack();
    RenderWorldEventCaller.callPost(
        stack,
        Minecraft.getInstance().getFrameTime()
    );
  }

}
