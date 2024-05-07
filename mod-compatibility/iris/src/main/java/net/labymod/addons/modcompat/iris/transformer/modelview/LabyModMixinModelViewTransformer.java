package net.labymod.addons.modcompat.iris.transformer.modelview;

import net.labymod.api.loader.MinecraftVersions;
import net.labymod.api.util.CollectionHelper;
import net.labymod.api.volt.asm.util.ASMHelper;
import net.minecraft.launchwrapper.IClassTransformer;

/**
 * Removes the redirects that conflict with Iris. They are merged in a new Mixin.
 */
public class LabyModMixinModelViewTransformer implements IClassTransformer {

  private static final String[] LABYMOD_MIXIN_NAMES = new String[]{
      "net.labymod.v1_20_5.mixins.client.renderer.MixinGameRenderer",
      "net.labymod.v1_20_6.mixins.client.renderer.MixinGameRenderer"
  };

  private static final String NO_BOBBING_NAME = "labyMod$noViewBobbing";
  private static final String SET_VIEW_MATRIX_NAME = "labyMod$setViewMatrix";

  @Override
  public byte[] transform(String name, String transformedName, byte... classData) {
    if (MinecraftVersions.V1_20_4.orOlder()
        || !CollectionHelper.contains(LABYMOD_MIXIN_NAMES, name)) {
      return classData;
    }

    return ASMHelper.transformClassData(
        classData,
        classNode -> classNode.methods.removeIf(
            methodNode ->
                NO_BOBBING_NAME.equals(methodNode.name)
                    || SET_VIEW_MATRIX_NAME.equals(methodNode.name)
        ));
  }
}
