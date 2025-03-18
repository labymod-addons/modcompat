package net.labymod.addons.modcompat.sodium.transformer;

import net.labymod.api.loader.MinecraftVersions;
import net.labymod.api.models.addon.annotation.EarlyAddonTransformer;
import net.labymod.api.volt.asm.util.ASMHelper;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * Sodium 1.20.1 expects LWJGL 3.3.1, but LabyMod always uses LWJGL 3.3.3. In between these
 * versions, LWJGL broke their api, causing Sodium to crash with LWJGL 3.3.3. This transformer adds
 * the required override for the new create method.
 */
@EarlyAddonTransformer
public class WindowsApiStructTransformer implements IClassTransformer {

  private static final String OLD_PACKAGE_NAME = "me.jellysquid.mods.sodium.client.platform.windows.api.";
  private static final String NEW_PACKAGE_NAME = "me.caffinemc.mods.sodium.client.platform.windows.api.";
  private static final String LWJGL_STRUCT = "org/lwjgl/system/Struct";

  private static final String CONSTRUCTOR_NAME = "<init>";
  private static final String CONSTRUCTOR_DESC = "(JLjava/nio/ByteBuffer;)V";

  private static final String CREATE_METHOD_NAME = "create";
  private static final String CREATE_METHOD_DESC = "(JLjava/nio/ByteBuffer;)Lorg/lwjgl/system/Struct;";
  private static final String CREATE_METHOD_DESC_GENERIC = "(JLjava/nio/ByteBuffer;)L%s;";

  @Override
  public byte[] transform(String name, String transformedName, byte... classBytes) {
    if (name != null &&
        (name.startsWith(OLD_PACKAGE_NAME) || name.startsWith(NEW_PACKAGE_NAME)) &&
        MinecraftVersions.V1_20_1.isCurrent()) {
      return ASMHelper.transformClassData(classBytes, this::transform);
    }
    return classBytes;
  }

  private void transform(ClassNode classNode) {
    if (!LWJGL_STRUCT.equals(classNode.superName)) {
      return;
    }

    String className = classNode.name;

    MethodNode genericMethod = ASMHelper.createMethod(
        Opcodes.ACC_PROTECTED,
        CREATE_METHOD_NAME,
        String.format(CREATE_METHOD_DESC_GENERIC, className),
        null,
        null,
        methodNode -> {
          // Create new instance of the class
          methodNode.visitTypeInsn(Opcodes.NEW, className);
          methodNode.visitInsn(Opcodes.DUP);

          // Call constructor with long (address) and ByteBuffer (container)
          methodNode.visitVarInsn(Opcodes.LLOAD, 1);
          methodNode.visitVarInsn(Opcodes.ALOAD, 3);
          methodNode.visitMethodInsn(
              Opcodes.INVOKESPECIAL,
              className,
              CONSTRUCTOR_NAME,
              CONSTRUCTOR_DESC,
              false
          );

          // Return the new instance
          methodNode.visitInsn(Opcodes.ARETURN);
        }
    );
    classNode.methods.add(genericMethod);

    // Bridge method with supertype of generic as return type
    classNode.methods.add(ASMHelper.createMethod(
        Opcodes.ACC_PROTECTED | Opcodes.ACC_BRIDGE | Opcodes.ACC_SYNTHETIC,
        CREATE_METHOD_NAME,
        CREATE_METHOD_DESC,
        null,
        null,
        methodNode -> {
          // Call generic method
          methodNode.visitVarInsn(Opcodes.ALOAD, 0);
          methodNode.visitVarInsn(Opcodes.LLOAD, 1);
          methodNode.visitVarInsn(Opcodes.ALOAD, 3);
          methodNode.visitMethodInsn(
              Opcodes.INVOKEVIRTUAL,
              className,
              genericMethod.name,
              genericMethod.desc,
              false
          );

          // Return the result of the generic method
          methodNode.visitInsn(Opcodes.ARETURN);
        }
    ));
  }
}