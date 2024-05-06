package net.labymod.addons.modcompat.iris.transformer;

import net.labymod.api.volt.asm.util.ASMHelper;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.tree.*;

public class IrisRenderSystemTransformer implements IClassTransformer {

  private static final String IRIS_RENDER_SYSTEM_NAME = "net.coderbot.iris.gl.IrisRenderSystem";
  private static final String DSA_UNSUPPORTED_NAME = "net.coderbot.iris.gl.IrisRenderSystem$DSAUnsupported";
  private static final String DSA_UNSUPPORTED_INTERNAL_NAME = DSA_UNSUPPORTED_NAME.replace('.', '/');

  private static final String DSACORE_SUFFIX = "DSACore";
  private static final String DSAARB = "DSAARB";

  @Override
  public byte[] transform(String name, String transformedName, byte... classData) {
    if (!IRIS_RENDER_SYSTEM_NAME.equals(name)) {
      return classData;
    }

    return ASMHelper.transformClassData(classData, this::patch);
  }

  private void patch(ClassNode classNode) {
    for (MethodNode methodNode : classNode.methods) {
      if (methodNode.name.equals("initRenderer") && methodNode.desc.equals("()V")) {
        this.patchInitRenderer(methodNode);
      }
    }
  }

  private void patchInitRenderer(MethodNode methodNode) {
    for (AbstractInsnNode instruction : methodNode.instructions) {
      if (instruction instanceof TypeInsnNode typeInsnNode) {
        String desc = typeInsnNode.desc;
        if (this.isDSA(desc)) {
          typeInsnNode.desc = DSA_UNSUPPORTED_INTERNAL_NAME;
        }
      }

      if (instruction instanceof MethodInsnNode methodInsnNode) {
        String owner = methodInsnNode.owner;
        if (this.isDSA(owner)) {
          methodInsnNode.owner = DSA_UNSUPPORTED_INTERNAL_NAME;
        }
      }
    }
  }

  private boolean isDSA(String value) {
    return value.endsWith(DSACORE_SUFFIX) || value.endsWith(DSAARB);
  }
}
