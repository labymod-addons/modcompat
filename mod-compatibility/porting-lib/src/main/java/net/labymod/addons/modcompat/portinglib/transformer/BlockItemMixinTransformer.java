package net.labymod.addons.modcompat.portinglib.transformer;

import java.util.List;
import net.labymod.api.models.addon.annotation.EarlyAddonTransformer;
import net.labymod.api.volt.asm.util.ASMHelper;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * Fixes a local variable injection by specifying the ordinal. The local variables contain two
 * block states and the second one is the one that needs to be used.
 */
@EarlyAddonTransformer
public class BlockItemMixinTransformer implements IClassTransformer {

  private static final String MIXIN_NAME = "io.github.fabricators_of_create.porting_lib.mixin.common.BlockItemMixin";
  private static final String MIXIN_METHOD_NAME = "port_lib$postProcessPlace";

  private static final String LOCAL_DESC = "Lcom/llamalad7/mixinextras/sugar/Local;";

  @Override
  public byte[] transform(String name, String transformedName, byte... classData) {
    if (!MIXIN_NAME.equals(name)) {
      return classData;
    }
    return ASMHelper.transformClassData(classData, this::patch);
  }

  private void patch(ClassNode classNode) {
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
