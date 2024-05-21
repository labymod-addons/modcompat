package net.labymod.addons.modcompat.iris.transformer;

import net.labymod.api.models.addon.annotation.EarlyAddonTransformer;
import net.labymod.api.volt.asm.util.ASMHelper;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

@EarlyAddonTransformer
public class IrisRenderSystemTransformer implements IClassTransformer {

  private static final String IRIS_RENDER_SYSTEM_NAME = "net.coderbot.iris.gl.IrisRenderSystem";
  private static final String DSA_UNSUPPORTED_NAME = "net/coderbot/iris/gl/IrisRenderSystem$DSAUnsupported";

  private static final String NEW_IRIS_RENDER_SYSTEM_NAME = "net.irisshaders.iris.gl.IrisRenderSystem";
  private static final String NEW_DSA_UNSUPPORTED_NAME = "net/irisshaders/iris/gl/IrisRenderSystem$DSAUnsupported";

  private static final String DSACORE_SUFFIX = "DSACore";
  private static final String DSAARB = "DSAARB";

  @Override
  public byte[] transform(String name, String transformedName, byte... classData) {
    boolean newIrisRenderSystem = NEW_IRIS_RENDER_SYSTEM_NAME.equals(name);

    if (!IRIS_RENDER_SYSTEM_NAME.equals(name) && !newIrisRenderSystem) {
      return classData;
    }

    return ASMHelper.transformClassData(
        classData,
        classNode -> this.patch(classNode, newIrisRenderSystem)
    );
  }

  private void patch(ClassNode classNode, boolean newNaming) {
    for (MethodNode methodNode : classNode.methods) {
      if (methodNode.name.equals("initRenderer") && methodNode.desc.equals("()V")) {
        this.patchInitRenderer(methodNode, newNaming);
      }
    }
  }

  private void patchInitRenderer(MethodNode methodNode, boolean newNaming) {
    for (AbstractInsnNode instruction : methodNode.instructions) {
      if (instruction instanceof TypeInsnNode typeInsnNode) {
        String desc = typeInsnNode.desc;
        if (this.isDSA(desc)) {
          typeInsnNode.desc = newNaming ? NEW_DSA_UNSUPPORTED_NAME : DSA_UNSUPPORTED_NAME;
        }
      }

      if (instruction instanceof MethodInsnNode methodInsnNode) {
        String owner = methodInsnNode.owner;
        if (this.isDSA(owner)) {
          methodInsnNode.owner = newNaming ? NEW_DSA_UNSUPPORTED_NAME : DSA_UNSUPPORTED_NAME;
        }
      }
    }
  }

  private boolean isDSA(String value) {
    return value.endsWith(DSACORE_SUFFIX) || value.endsWith(DSAARB);
  }
}
