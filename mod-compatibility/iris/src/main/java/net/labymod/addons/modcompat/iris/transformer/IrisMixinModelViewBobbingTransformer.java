package net.labymod.addons.modcompat.iris.transformer;

import net.labymod.api.volt.asm.util.ASMHelper;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.tree.ClassNode;

public class IrisMixinModelViewBobbingTransformer implements IClassTransformer {

  private static final String IRIS_MIXIN_NAME = "net.irisshaders.iris.mixin.MixinModelViewBobbing";
  private static final String MIXIN_DESC = "Lorg/spongepowered/asm/mixin/Mixin;";

  @Override
  public byte[] transform(String name, String transformedName, byte... classData) {
    if (!IRIS_MIXIN_NAME.equals(name)) {
      return classData;
    }

    return ASMHelper.transformClassData(classData, this::patch);
  }

  private void patch(ClassNode classNode) {
    classNode.methods.clear();
    /*if (classNode.invisibleAnnotations != null) {
      for (AnnotationNode annotation : classNode.invisibleAnnotations) {
        if (MIXIN_DESC.equals(annotation.desc)) {
          annotation.values.add("priority");
          annotation.values.add(999);
          break;
        }
      }
    }*/
  }
}
