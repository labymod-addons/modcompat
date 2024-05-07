package net.labymod.addons.modcompat.iris.transformer;

import net.labymod.api.loader.MinecraftVersions;
import net.labymod.api.volt.asm.util.ASMHelper;
import net.minecraft.launchwrapper.IClassTransformer;

/**
 * Removes the redirects that conflict with LabyMod. They are merged in a new Mixin.
 */
public class IrisMixinModelViewBobbingTransformer implements IClassTransformer {

  private static final String IRIS_MIXIN_NAME = "net.irisshaders.iris.mixin.MixinModelViewBobbing";

  private static final String STOP_BOBBING_NAME = "iris$stopBobbing";
  private static final String APPLY_BOBBING_NAME = "iris$applyBobbingToModelView";

  @Override
  public byte[] transform(String name, String transformedName, byte... classData) {
    if (MinecraftVersions.V1_20_4.orOlder() || !IRIS_MIXIN_NAME.equals(name)) {
      return classData;
    }

    return ASMHelper.transformClassData(
        classData,
        classNode -> classNode.methods.removeIf(
            methodNode ->
                STOP_BOBBING_NAME.equals(methodNode.name)
                    || APPLY_BOBBING_NAME.equals(methodNode.name)
        ));
  }
}
