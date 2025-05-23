package net.labymod.addons.modcompat.sodium.transformer;

import net.labymod.api.models.addon.annotation.EarlyAddonTransformer;
import net.labymod.api.volt.asm.util.ASMHelper;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * Disables the Sodium bug check for issue 2561, which checks whether the LWJGL version matches.
 */
@EarlyAddonTransformer
public class BugChecksTransformer implements IClassTransformer {

  private static final String OLD_BUG_CHECKS_CLASS_NAME = "me.jellysquid.mods.sodium.client.compatibility.checks.BugChecks";
  private static final String NEW_BUG_CHECKS_CLASS_NAME = "net.caffeinemc.mods.sodium.client.compatibility.checks.BugChecks";

  private static final String STATIC_INITIALIZER_NAME = "<clinit>";
  private static final String STATIC_INITIALIZER_DESC = "()V";

  private static final String CHECK_NAME = "issue2561";

  @Override
  public byte[] transform(String name, String transformedName, byte... classBytes) {
    if (OLD_BUG_CHECKS_CLASS_NAME.equals(name) || NEW_BUG_CHECKS_CLASS_NAME.equals(name)) {
      return ASMHelper.transformClassData(classBytes, this::transform);
    }
    return classBytes;
  }

  private void transform(ClassNode classNode) {
    for (MethodNode method : classNode.methods) {
      // Bug checks are created in the static initializer
      if (
          STATIC_INITIALIZER_NAME.equals(method.name)
              && STATIC_INITIALIZER_DESC.equals(method.desc)
      ) {
        // Search for the ldc instruction that loads the LWJGL check name
        AbstractInsnNode lwjglCheckLdcInsn = ASMHelper.findInstruction(
            method.instructions.getFirst(),
            true,
            insn -> insn instanceof LdcInsnNode ldcInsn && CHECK_NAME.equals(ldcInsn.cst)
        );

        // If the check is found, replace the following true boolean with false to disable the check
        if (
            lwjglCheckLdcInsn != null
                && lwjglCheckLdcInsn.getNext() instanceof InsnNode insn
                && insn.getOpcode() == Opcodes.ICONST_1
        ) {
          // Remove true
          method.instructions.remove(insn);
          // Insert false after the ldc instruction
          method.instructions.insert(lwjglCheckLdcInsn, new InsnNode(Opcodes.ICONST_0));
        }
        break;
      }
    }
  }
}
