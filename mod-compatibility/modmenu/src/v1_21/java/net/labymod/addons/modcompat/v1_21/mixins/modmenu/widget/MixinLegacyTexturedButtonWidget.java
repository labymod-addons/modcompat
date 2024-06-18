package net.labymod.addons.modcompat.v1_21.mixins.modmenu.widget;

import com.terraformersmc.modmenu.gui.widget.LegacyTexturedButtonWidget;
import net.labymod.api.Laby;
import net.labymod.api.client.render.matrix.VanillaStackAccessor;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.volt.annotation.Insert;
import net.labymod.api.volt.callback.InsertInfo;
import net.labymod.v1_21.mixins.client.gui.components.MixinImageButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(LegacyTexturedButtonWidget.class)
public abstract class MixinLegacyTexturedButtonWidget extends MixinImageButton {

  @Shadow
  @Final
  private net.minecraft.resources.ResourceLocation texture;

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

  @Insert(
      method = "renderWidget",
      at = @At(
          value = "HEAD"
      ),
      cancellable = true
  )
  public void render(
      GuiGraphics graphics,
      int mouseX,
      int mouseY,
      float partialTicks,
      InsertInfo ci
  ) {
    this.getWatcher().update(this, ((AbstractWidget) (Object) this).getMessage());

    Laby.labyAPI().minecraft().updateMouse(mouseX, mouseY, mouse -> {
      boolean rendered = this.getWatcher().render(
          ((VanillaStackAccessor) graphics.pose()).stack(),
          mouse,
          partialTicks
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
