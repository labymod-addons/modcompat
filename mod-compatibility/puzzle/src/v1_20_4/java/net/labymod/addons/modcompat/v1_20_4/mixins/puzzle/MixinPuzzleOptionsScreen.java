package net.labymod.addons.modcompat.v1_20_4.mixins.puzzle;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.puzzlemc.gui.screen.PuzzleOptionsScreen;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PuzzleOptionsScreen.class)
public abstract class MixinPuzzleOptionsScreen extends Screen {

  protected MixinPuzzleOptionsScreen(Component title) {
    super(title);
  }

  @Redirect(method = "init()V", at = @At(value = "INVOKE", target = "Lnet/puzzlemc/gui/screen/PuzzleOptionsScreen;addWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;"))
  private <T extends GuiEventListener & Renderable & NarratableEntry> T modcompat$makeListRenderable(
      PuzzleOptionsScreen instance,
      T widget
  ) {
    return this.addRenderableWidget(widget);
  }

  /**
   * @author LabyMedia GmbH
   * @reason Fix fancy theme issues
   */
  @Overwrite
  public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
    super.render(guiGraphics, mouseX, mouseY, delta);
  }

  /**
   * @author LabyMedia GmbH
   * @reason Fix fancy theme issues
   */
  @Overwrite
  public void renderBackground(
      @NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta
  ) {
    super.renderBackground(guiGraphics, mouseX, mouseY, delta);
  }
}
