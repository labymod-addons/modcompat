package net.labymod.addons.modcompat.iris.transformer.modelview;

import net.labymod.addons.modcompat.transformer.MixinClassTransformer;
import net.labymod.api.loader.MinecraftVersions;
import net.labymod.api.models.addon.annotation.EarlyAddonTransformer;
import org.objectweb.asm.tree.ClassNode;

/**
 * Removes the redirects that conflict with Iris. They are merged in a new Mixin.
 */
@EarlyAddonTransformer
public class LabyModMixinModelViewTransformer extends MixinClassTransformer {

  private static final String[] LABYMOD_MIXIN_NAMES = new String[]{
      "net.labymod.v1_20_5.mixins.client.renderer.MixinGameRenderer",
      "net.labymod.v1_20_6.mixins.client.renderer.MixinGameRenderer"
  };

  private static final String NO_BOBBING_NAME = "labyMod$noViewBobbing";

  public LabyModMixinModelViewTransformer() {
    super(LABYMOD_MIXIN_NAMES);
  }

  @Override
  protected boolean shouldTransform(String name, String transformedName, byte... bytes) {
    return MinecraftVersions.V1_20_5.orNewer();
  }

  @Override
  protected void transform(ClassNode classNode) {
    classNode.methods.removeIf(methodNode -> NO_BOBBING_NAME.equals(methodNode.name));
  }
}
