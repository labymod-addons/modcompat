package net.labymod.addons.modcompat.v1_21_11.mixins.modmenu.widget;

import com.terraformersmc.modmenu.gui.widget.LegacyTexturedButtonWidget;
import net.labymod.api.Laby;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.v1_21_11.client.util.MinecraftUtil;
import net.labymod.v1_21_11.mixins.client.gui.components.MixinImageButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(LegacyTexturedButtonWidget.class)
public abstract class MixinLegacyTexturedButtonWidget extends MixinImageButton {

  @Shadow
  @Final
  private Identifier texture;

  @Shadow
  @Final
  private int u;

  @Shadow
  @Final
  private int v;

  @Shadow
  @Final
  private int hoveredVOffset;

  @Shadow
  @Final
  private int textureWidth;

  @Shadow
  @Final
  private int textureHeight;

  @Inject(
      method = "renderContents",
      at = @At(
          value = "HEAD"
      ),
      cancellable = true
  )
  public void render(
      GuiGraphics context, int mouseX, int mouseY, float deltaTicks, CallbackInfo ci
  ) {
    this.getWatcher().update(this, ((AbstractWidget) (Object) this).getMessage());

    Laby.labyAPI().minecraft().updateMouse(mouseX, mouseY, mouse -> {
      boolean rendered = this.getWatcher().render(
          MinecraftUtil.obtainStackFromGraphics(context),
          mouse,
          deltaTicks
      );

      // Disable image button rendering
      if (rendered) {
        ci.cancel();
      }
    });
  }

  @Override
  public ResourceLocation getResourceLocation() {
    return (ResourceLocation) (Object) this.texture;
  }

  @Override
  public int getXTexStart() {
    return this.u;
  }

  @Override
  public int getYTexStart() {
    return this.v;
  }

  @Override
  public int getYDiffTex() {
    return this.hoveredVOffset;
  }

  @Override
  public int getTextureWidth() {
    return this.textureWidth;
  }

  @Override
  public int getTextureHeight() {
    return this.textureHeight;
  }
}
