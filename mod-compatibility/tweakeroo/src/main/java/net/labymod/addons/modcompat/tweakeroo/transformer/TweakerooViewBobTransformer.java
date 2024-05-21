package net.labymod.addons.modcompat.tweakeroo.transformer;

import net.labymod.api.models.addon.annotation.EarlyAddonTransformer;
import net.labymod.api.volt.asm.util.ASMHelper;
import net.minecraft.launchwrapper.IClassTransformer;

/**
 * Removes the view bobbing feature from Tweakeroo, as the feature is already implemented in LabyMod,
 * which causes Mixin conflicts with the redirects.
 */
@EarlyAddonTransformer
public class TweakerooViewBobTransformer implements IClassTransformer {

  private static final String MIXIN_GAME_RENDERER_NAME = "fi.dy.masa.tweakeroo.mixin.MixinGameRenderer";
  private static final String DISABLE_VIEW_BOB_METHOD_NAME = "disableWorldViewBob";

  @Override
  public byte[] transform(String name, String transformedName, byte... classData) {
    if (!MIXIN_GAME_RENDERER_NAME.equals(name)) {
      return classData;
    }

    return ASMHelper.transformClassData(classData, classNode ->
        classNode.methods.removeIf(
            methodNode -> DISABLE_VIEW_BOB_METHOD_NAME.equals(methodNode.name)
        )
    );
  }
}
