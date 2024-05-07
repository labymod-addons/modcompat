package net.labymod.addons.modcompat.fabricapi.transformer;

import net.labymod.api.loader.MinecraftVersions;
import net.labymod.api.volt.asm.util.ASMContext;
import net.labymod.api.volt.asm.util.ASMHelper;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class FabricApiConnectionMixinTransformer implements IClassTransformer {

  private static final String CONNECTION_MIXIN_NAME = "net.fabricmc.fabric.mixin.networking.ClientConnectionMixin";

  private static final String MODIFY_VARIABLE_DESC = "Lorg/spongepowered/asm/mixin/injection/ModifyVariable;";

  static {
    ASMContext.setPlatformClassLoader(Launch.classLoader);
    ASMContext.setResourceFinder(Launch.classLoader::loadResource);
  }

  @Override
  public byte[] transform(String name, String transformedName, byte... classData) {
    if (!CONNECTION_MIXIN_NAME.equals(name) || MinecraftVersions.V1_20_1.orOlder()) {
      return classData;
    }
    return ASMHelper.transformClassData(classData, this::patch);
  }

  private void patch(ClassNode classNode) {
    for (MethodNode methodNode : classNode.methods) {
      if (methodNode.name.equals("disconnectAddon") && methodNode.visibleAnnotations != null) {
        for (AnnotationNode visibleAnnotation : methodNode.visibleAnnotations) {
          if (visibleAnnotation.desc.equals(MODIFY_VARIABLE_DESC)) {
            this.patchModifyVariableOrdinal(visibleAnnotation);
            break;
          }
        }
      }
    }
  }

  private void patchModifyVariableOrdinal(AnnotationNode annotationNode) {
    // There are two PacketListener local variables in the original method, but only one in fabrics.
    // Setting the ordinal to specify which instance is targeted.
    annotationNode.values.add("ordinal");
    annotationNode.values.add(1);
  }
}
