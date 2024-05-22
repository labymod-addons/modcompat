package net.labymod.addons.modcompat.tweakeroo.transformer;

import net.labymod.addons.modcompat.transformer.MixinClassTransformer;
import net.labymod.api.models.addon.annotation.EarlyAddonTransformer;
import org.objectweb.asm.tree.ClassNode;

/**
 * Removes the view bobbing feature from Tweakeroo, as the feature is already implemented in
 * LabyMod, which causes Mixin conflicts with the redirects.
 */
@EarlyAddonTransformer
public class TweakerooViewBobTransformer extends MixinClassTransformer {

  private static final String MIXIN_GAME_RENDERER_NAME = "fi.dy.masa.tweakeroo.mixin.MixinGameRenderer";
  private static final String DISABLE_VIEW_BOB_METHOD_NAME = "disableWorldViewBob";

  public TweakerooViewBobTransformer() {
    super(MIXIN_GAME_RENDERER_NAME);
  }

  @Override
  protected void transform(ClassNode classNode) {
    classNode.methods.removeIf(
        methodNode -> DISABLE_VIEW_BOB_METHOD_NAME.equals(methodNode.name)
    );
  }
}
