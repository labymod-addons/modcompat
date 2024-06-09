package net.labymod.addons.modcompat.iris.transformer;

import net.labymod.addons.modcompat.transformer.MixinClassTransformer;
import net.labymod.api.loader.MinecraftVersions;
import net.labymod.api.models.addon.annotation.EarlyAddonTransformer;
import org.objectweb.asm.tree.ClassNode;

/**
 * Removes the redirects that conflict with LabyMod. They are merged in a new Mixin.
 */
@EarlyAddonTransformer
public class MixinModelViewBobbingTransformer extends MixinClassTransformer {

  private static final String IRIS_MIXIN_NAME = "net.irisshaders.iris.mixin.MixinModelViewBobbing";

  private static final String STOP_BOBBING_NAME = "iris$stopBobbing";
  private static final String APPLY_BOBBING_NAME = "iris$applyBobbingToModelView";

  public MixinModelViewBobbingTransformer() {
    super(IRIS_MIXIN_NAME);
  }

  @Override
  protected boolean shouldTransform(String name, String transformedName, byte... bytes) {
    return MinecraftVersions.V1_20_5.orNewer();
  }

  @Override
  protected void transform(ClassNode classNode) {
    classNode.methods.removeIf(
        methodNode ->
            STOP_BOBBING_NAME.equals(methodNode.name)
                || APPLY_BOBBING_NAME.equals(methodNode.name)
    );
  }
}
