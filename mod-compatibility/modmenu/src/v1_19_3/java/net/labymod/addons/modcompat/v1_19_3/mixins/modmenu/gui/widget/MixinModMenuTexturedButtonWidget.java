package net.labymod.addons.modcompat.v1_19_3.mixins.modmenu.gui.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import com.terraformersmc.modmenu.gui.widget.ModMenuTexturedButtonWidget;
import net.labymod.api.Laby;
import net.labymod.api.client.render.matrix.VanillaStackAccessor;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.volt.annotation.Insert;
import net.labymod.api.volt.callback.InsertInfo;
import net.labymod.core.client.accessor.gui.ImageButtonAccessor;
import net.labymod.v1_19_3.mixins.client.gui.components.MixinAbstractWidget;
import net.minecraft.client.gui.components.AbstractWidget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(ModMenuTexturedButtonWidget.class)
public abstract class MixinModMenuTexturedButtonWidget extends MixinAbstractWidget implements
    ImageButtonAccessor {

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
  private int uWidth;

  @Shadow
  @Final
  private int vHeight;

  @Insert(
      method = "renderButton",
      at = @At(
          value = "HEAD"
      ),
      cancellable = true
  )
  public void render(
      PoseStack stack,
      int mouseX,
      int mouseY,
      float partialTicks,
      InsertInfo ci
  ) {
    this.getWatcher().update(this, ((AbstractWidget) (Object) this).getMessage());

    Laby.labyAPI().minecraft().updateMouse(mouseX, mouseY, mouse -> {
      boolean rendered = this.getWatcher().render(
          ((VanillaStackAccessor) stack).stack(),
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
    return (ResourceLocation) this.texture;
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
    return 0;
  }

  @Override
  public int getTextureWidth() {
    return this.uWidth;
  }

  @Override
  public int getTextureHeight() {
    return this.vHeight;
  }
}
