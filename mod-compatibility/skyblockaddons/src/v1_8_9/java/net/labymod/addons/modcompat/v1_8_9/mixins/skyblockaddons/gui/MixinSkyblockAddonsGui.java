package net.labymod.addons.modcompat.v1_8_9.mixins.skyblockaddons.gui;

import codes.biscuit.skyblockaddons.core.Feature;
import codes.biscuit.skyblockaddons.gui.SkyblockAddonsGui;
import codes.biscuit.skyblockaddons.gui.buttons.ButtonFeature;
import net.labymod.addons.modcompat.v1_8_9.skyblockaddons.SkyblockAddonsCompat;
import net.labymod.api.Laby;
import net.labymod.core.client.gui.screen.activity.activities.labymod.LabyModActivity;
import net.labymod.core.client.gui.screen.activity.activities.labymod.child.WidgetsEditorActivity;
import net.minecraft.client.gui.GuiButton;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(value = SkyblockAddonsGui.class)
public class MixinSkyblockAddonsGui {

  @Dynamic
  @Inject(method = "actionPerformed(Lnet/minecraft/client/gui/GuiButton;)V", at = @At("HEAD"), cancellable = true)
  private void modcompat$adjustOpenEditor(GuiButton abstractButton, CallbackInfo ci) {
    if (SkyblockAddonsCompat.isFeatureIntegration()
        && abstractButton instanceof ButtonFeature buttonFeature) {
      if (buttonFeature.getFeature() == Feature.EDIT_LOCATIONS) {
        // Open LabyMod widget editor
        LabyModActivity labyModActivity = LabyModActivity.getFromNavigationRegistry();
        if (labyModActivity != null) {
          labyModActivity.switchTab(WidgetsEditorActivity.class);
          Laby.labyAPI().minecraft().minecraftWindow().displayScreen(labyModActivity);
        }

        ci.cancel();
      }
    }
  }
}
