package net.labymod.addons.modcompat.fabricapi.transformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import net.labymod.api.loader.MinecraftVersions;
import net.labymod.api.mapping.MappingService;
import net.labymod.api.mapping.provider.MappingProvider;
import net.labymod.api.models.addon.annotation.EarlyAddonTransformer;
import net.labymod.api.volt.asm.tree.InsnListBuilder;
import net.labymod.api.volt.asm.util.ASMContext;
import net.labymod.api.volt.asm.util.ASMHelper;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

@EarlyAddonTransformer
public class FabricApiGameRendererMixinTransformer implements IClassTransformer {

  private static final String GAME_RENDERER_MXIN_NAME = "net.fabricmc.fabric.mixin.screen.GameRendererMixin";

  private static final boolean IS_NO_JOML = MinecraftVersions.V1_19_2.orOlder();
  private static final boolean IS_NO_MATRIX = MinecraftVersions.V1_16_5.orOlder();

  private static final MappingProvider MAPPINGS = MappingService.instance().currentMappings();

  private static final Type CALLBACK_INFO_TYPE = Type.getType(
      "Lorg/spongepowered/asm/mixin/injection/callback/CallbackInfo;"
  );

  private static final Type WINDOW_TYPE = getType("com/mojang/blaze3d/platform/Window");
  private static final Type MATRIX_TYPE = getType(
      IS_NO_JOML ? "com/mojang/math/Matrix4f" : "org/joml/Matrix4f"
  );
  private static final List<Type> INSERT_LOCAL_VARIABLE_TYPES = IS_NO_MATRIX
      ? Collections.singletonList(WINDOW_TYPE)
      : Arrays.asList(WINDOW_TYPE, MATRIX_TYPE);

  static {
    ASMContext.setPlatformClassLoader(Launch.classLoader);
    ASMContext.setResourceFinder(Launch.classLoader::loadResource);
  }

  private static Type getType(String className) {
    return Type.getType("L" + MAPPINGS.mapClass(className) + ";");
  }

  @Override
  public int getPriority() {
    return 999;
  }

  @Override
  public byte[] transform(String name, String transformedName, byte... classData) {
    if (!GAME_RENDERER_MXIN_NAME.equals(name)) {
      return classData;
    }
    return ASMHelper.transformClassData(classData, this::patch);
  }

  private void patch(ClassNode classNode) {
    for (MethodNode methodNode : classNode.methods) {
      if (
          methodNode.name.equals("onBeforeRenderScreen")
              || methodNode.name.equals("onAfterRenderScreen")
      ) {
        this.patchLocalVariableArguments(methodNode);

        if (methodNode.name.equals("onAfterRenderScreen")) {
          this.patchAfterRender(methodNode);
        }
      }
    }
  }

  private void patchLocalVariableArguments(MethodNode methodNode) {
    // TODO: 26.05.2023 Implement a fixer that solves this problem in general, not just for this particular case
    String descriptor = methodNode.desc;

    Type returnType = Type.getReturnType(descriptor);
    List<Type> argumentTypes = Arrays.asList(Type.getArgumentTypes(descriptor));

    // Search for the start index of the local variables
    int localVariableStartIndex = argumentTypes.indexOf(CALLBACK_INFO_TYPE) + 1;
    if (localVariableStartIndex >= argumentTypes.size()) {
      // No local variables used
      return;
    }

    // Search for the index where the local variables are inserted (after the last int)
    int insertIndex = 0;
    for (int i = localVariableStartIndex; i < argumentTypes.size(); i++) {
      if (argumentTypes.get(i).equals(Type.INT_TYPE)) {
        insertIndex = i + 1;
      }
    }

    if (new HashSet<>(argumentTypes).containsAll(INSERT_LOCAL_VARIABLE_TYPES)) {
      // Already inserted, nothing to fix
      return;
    }

    List<Type> newArgumentTypes = new ArrayList<>(argumentTypes);

    // For 1.20.3+, the local variables start with a float which needs to be added
    if (MinecraftVersions.V1_20_3.orNewer()) {
      newArgumentTypes.add(localVariableStartIndex, Type.FLOAT_TYPE);
      insertIndex++;
    }

    // Insert missing local variables
    newArgumentTypes.addAll(
        insertIndex,
        INSERT_LOCAL_VARIABLE_TYPES
    );

    // Set new parameters to descriptor
    methodNode.desc = Type.getMethodDescriptor(returnType, newArgumentTypes.toArray(new Type[0]));

    // Get the index of the local variable where the arguments were inserted
    int localVariableInsertIndex = 0;
    for (int i = 0; i < insertIndex; i++) {
      localVariableInsertIndex += newArgumentTypes.get(i).getSize();
    }

    // Get the size of the inserted local variables
    int insertedLocalVariableSizes = 0;
    for (Type insertLocalVariableType : INSERT_LOCAL_VARIABLE_TYPES) {
      insertedLocalVariableSizes += insertLocalVariableType.getSize();
    }

    // Shift indices of var instructions to be after the inserted arguments
    for (AbstractInsnNode instruction : methodNode.instructions) {
      if (instruction instanceof VarInsnNode varInsnNode) {
        if (varInsnNode.var > localVariableInsertIndex) {
          varInsnNode.var += insertedLocalVariableSizes;
        }

        // Shift all instructions for 1.20.3+ to skip the float at the start
        if (MinecraftVersions.V1_20_3.orNewer()) {
          if (varInsnNode.var > localVariableStartIndex) {
            varInsnNode.var += Type.FLOAT_TYPE.getSize();
          }
        }
      }
    }
  }

  private void patchAfterRender(MethodNode methodNode) {
    // TODO: this might be a weird fix as renderingScreen should never be null here
    if (methodNode.name.equals("onAfterRenderScreen")) {
      // Find a get field instruction for the renderingScreen local variable
      FieldInsnNode getRenderingScreenInsn = null;
      for (AbstractInsnNode instruction : methodNode.instructions) {
        if (instruction instanceof FieldInsnNode fieldInsnNode) {
          getRenderingScreenInsn = fieldInsnNode;
          break;
        }
      }

      if (getRenderingScreenInsn != null) {
        // Return early if renderingScreen is null
        LabelNode labelNode = new LabelNode();
        InsnList returnList = InsnListBuilder.create()
            // load this
            .addVar(Opcodes.ALOAD, 0)
            // load renderingScreen local variable
            .addField(
                Opcodes.GETFIELD,
                getRenderingScreenInsn.owner,
                getRenderingScreenInsn.name,
                getRenderingScreenInsn.desc
            )
            // jump if renderingScreen is not null
            .addJump(Opcodes.IFNONNULL, labelNode)
            // if the jump is not executed, return
            .addInstruction(Opcodes.RETURN)
            // jump destination is skipping the return
            .add(labelNode)
            .build();

        // Insert the return list at the start of the method
        methodNode.instructions.insertBefore(methodNode.instructions.getFirst(), returnList);
      }
    }
  }
}
