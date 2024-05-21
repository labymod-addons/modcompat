package net.labymod.addons.modcompat.portinglib.transformer;

import net.labymod.api.models.addon.annotation.EarlyAddonTransformer;
import net.labymod.api.volt.asm.util.ASMHelper;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * Fixes a local variable modification by specifying the ordinal. The first float should be used.
 */
@EarlyAddonTransformer
public class LivingEntityMixinTransformer implements IClassTransformer {

  private static final String MIXIN_NAME = "io.github.fabricators_of_create.porting_lib.mixin.common.LivingEntityMixin";
  private static final String MIXIN_METHOD_NAME = "port_lib$setSlipperiness";

  private static final String MODIFY_VARIABLE_DESC = "Lorg/spongepowered/asm/mixin/injection/ModifyVariable;";

  @Override
  public byte[] transform(String name, String transformedName, byte... classData) {
    if (!MIXIN_NAME.equals(name)) {
      return classData;
    }
    return ASMHelper.transformClassData(classData, this::patch);
  }

  private void patch(ClassNode classNode) {
    for (MethodNode method : classNode.methods) {
      if (MIXIN_METHOD_NAME.equals(method.name) && method.visibleAnnotations != null) {
        for (AnnotationNode visibleAnnotation : method.visibleAnnotations) {
          if (MODIFY_VARIABLE_DESC.equals(visibleAnnotation.desc)) {
            visibleAnnotation.values.add("ordinal");
            visibleAnnotation.values.add(0);
            break;
          }
        }
        break;
      }
    }
  }
}
