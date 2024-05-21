package net.labymod.addons.modcompat.puzzleslib.transformer;

import net.labymod.api.models.addon.annotation.EarlyAddonTransformer;
import net.labymod.api.volt.asm.util.ASMHelper;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

@EarlyAddonTransformer
public class LivingEntityFabricMixinTransformer implements IClassTransformer {

  private static final String MIXIN_NAME = "fuzs.puzzleslib.fabric.mixin.LivingEntityFabricMixin";
  private static final String MIXIN_METHOD_NAME = "baseTick$2";

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
            visibleAnnotation.values.add(1);
            break;
          }
        }
        break;
      }
    }
  }
}
