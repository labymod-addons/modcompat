package net.labymod.addons.modcompat.v1_8_9.mixins.skyblockaddons.render;

import codes.biscuit.skyblockaddons.features.dungeonmap.DungeonMapManager;
import net.labymod.addons.modcompat.v1_8_9.skyblockaddons.FeatureDrawContext;
import net.labymod.api.Laby;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(value = DungeonMapManager.class, remap = false)
public class MixinDungeonMapManager {

  @Unique
  private static final int DUNGEON_MAP_SIZE = 64;

  @Inject(method = "drawDungeonsMap", at = @At(value = "INVOKE", target = "Lcodes/biscuit/skyblockaddons/listeners/RenderListener;transformXY(FIF)F", shift = Shift.AFTER, ordinal = 1), cancellable = true)
  private static void modcompat$noFeatureRender(CallbackInfo ci) {
    if (FeatureDrawContext.get().isActive() && FeatureDrawContext.get().isNoRender()) {
      GlStateManager.popMatrix();
      GL11.glDisable(GL11.GL_SCISSOR_TEST);
      // Disable rendering, call was only to obtain size information
      ci.cancel();
    }
  }

  @Redirect(method = "drawDungeonsMap", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glEnable(I)V"))
  private static void modcompat$enableLabyModScissor(int target) {
    FeatureDrawContext drawContext = FeatureDrawContext.get();
    if (drawContext.isActive() && !drawContext.isNoRender()) {
      // Use GL scissor from LabyMod, the original scissor causes problems in the widget editor
      Laby.labyAPI().gfxRenderPipeline().scissor().push(
          drawContext.getStack(),
          0.0F,
          0.0F,
          DUNGEON_MAP_SIZE,
          DUNGEON_MAP_SIZE,
          false
      );
    } else {
      GL11.glEnable(target);
    }
  }

  @Redirect(method = "drawDungeonsMap", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glDisable(I)V"))
  private static void modcompat$disableLabyModScissor(int target) {
    FeatureDrawContext drawContext = FeatureDrawContext.get();
    if (drawContext.isActive() && !drawContext.isNoRender()) {
      // Pop GL scissor from LabyMod
      Laby.labyAPI().gfxRenderPipeline().scissor().pop();
    } else {
      GL11.glDisable(target);
    }
  }

  @Redirect(method = "drawDungeonsMap", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glScissor(IIII)V"))
  private static void modcompat$removeOriginalScissor(int x, int y, int width, int height) {
    FeatureDrawContext drawContext = FeatureDrawContext.get();
    if (drawContext.isActive() && !drawContext.isNoRender()) {
      // Do not apply original scissor
    } else {
      GL11.glScissor(x, y, width, height);
    }
  }
}
