package net.labymod.addons.modcompat.iris.transformer;

import net.labymod.addons.modcompat.transformer.MixinClassTransformer;
import net.labymod.api.models.addon.annotation.EarlyAddonTransformer;
import org.objectweb.asm.tree.ClassNode;

/**
 * Removes the AMD crash fix from Iris 1.16.5 which is already present in LabyMod.
 */
@EarlyAddonTransformer
public class MixinAmdCrashFixTransformer extends MixinClassTransformer {

  public MixinAmdCrashFixTransformer() {
    super("net.coderbot.iris.mixin.MixinGlStateManager_AmdCrashFix");
  }

  @Override
  protected void transform(ClassNode classNode) {
    classNode.methods.clear();
  }
}
