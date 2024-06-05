package net.labymod.addons.modcompat.portinglib.transformer;

import java.util.List;
import net.labymod.addons.modcompat.transformer.MixinClassTransformer;
import net.labymod.api.models.addon.annotation.EarlyAddonTransformer;
import net.labymod.core.main.BuildData;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * Fixes a local variable injection by specifying the ordinal. The local variables contain two block
 * states and the second one is the one that needs to be used.
 */
@EarlyAddonTransformer
public class BlockItemMixinTransformer extends MixinClassTransformer {

  private static final String MIXIN_NAME = "io.github.fabricators_of_create.porting_lib.mixin.common.BlockItemMixin";
  private static final String MIXIN_METHOD_NAME = "port_lib$postProcessPlace";

  public BlockItemMixinTransformer() {
    super(MIXIN_NAME);
  }

  @Override
  protected boolean shouldTransform(String name, String transformedName, byte... bytes) {
    return !BuildData.version().isGreaterThan(BROKEN_FRAMES_VERSION);
  }

  @Override
  protected void transform(ClassNode classNode) {
    for (MethodNode method : classNode.methods) {
      if (MIXIN_METHOD_NAME.equals(method.name)
          && method.invisibleParameterAnnotations != null
          && method.invisibleParameterAnnotations.length == 4) {
        List<AnnotationNode> parameterAnnotations = method.invisibleParameterAnnotations[3];
        for (AnnotationNode parameterAnnotation : parameterAnnotations) {
          if (LOCAL_DESC.equals(parameterAnnotation.desc)) {
            parameterAnnotation.values = List.of("ordinal", 1);
            break;
          }
        }
        break;
      }
    }
  }
}
