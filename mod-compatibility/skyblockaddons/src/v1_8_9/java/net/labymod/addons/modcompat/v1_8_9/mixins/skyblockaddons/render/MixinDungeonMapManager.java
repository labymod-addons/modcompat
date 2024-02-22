package net.labymod.addons.modcompat.v1_8_9.mixins.skyblockaddons.render;

import codes.biscuit.skyblockaddons.features.dungeonmap.DungeonMapManager;
import net.labymod.addons.modcompat.v1_8_9.skyblockaddons.FeatureDrawContext;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(value = DungeonMapManager.class, remap = false)
public class MixinDungeonMapManager {

  @Inject(method = "drawDungeonsMap", at = @At(value = "INVOKE", target = "Lcodes/biscuit/skyblockaddons/listeners/RenderListener;transformXY(FIF)F", shift = Shift.AFTER, opcode = 1), cancellable = true)
  private static void modcompat$noFeatureRender(CallbackInfo ci) {
    if (FeatureDrawContext.get().isActive() && FeatureDrawContext.get().isNoRender()) {
      GlStateManager.popMatrix();
      GL11.glDisable(GL11.GL_SCISSOR_TEST);
      // Disable rendering, call was only to obtain size information
      ci.cancel();
    }
  }
}
