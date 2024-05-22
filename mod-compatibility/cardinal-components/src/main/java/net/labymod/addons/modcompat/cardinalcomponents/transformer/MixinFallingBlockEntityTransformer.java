package net.labymod.addons.modcompat.cardinalcomponents.transformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.labymod.addons.modcompat.transformer.MixinClassTransformer;
import net.labymod.api.models.addon.annotation.EarlyAddonTransformer;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

@EarlyAddonTransformer
public class MixinFallingBlockEntityTransformer extends MixinClassTransformer {

  private static final String MIXIN_NAME = "dev.onyxstudios.cca.mixin.block.common.MixinFallingBlockEntity";
  private static final String MIXIN_METHOD_NAME = "readComponentData";

  // Expected: [Lnet/minecraft/world/level/block/Block;, Lnet/minecraft/core/BlockPos;, Lnet/minecraft/world/level/block/entity/BlockEntity;, Lnet/minecraft/nbt/CompoundTag;]
  // Available: [Lnet/minecraft/world/level/block/Block;, Lnet/minecraft/core/BlockPos;, Z, Z, D, Lnet/minecraft/world/level/block/state/BlockState;, Z, Z, Z, Lnet/minecraft/world/level/block/entity/BlockEntity;, Lnet/minecraft/nbt/CompoundTag;]
  // Diff: Z, Z, D, Lnet/minecraft/world/level/block/state/BlockState;, Z, Z, Z
  private static final Type BLOCK_POS_TYPE = getType(
      "net/minecraft/core/BlockPos"
  );
  private static final Type BLOCK_STATE_TYPE = getType(
      "net/minecraft/world/level/block/state/BlockState"
  );
  private static final List<Type> TO_INSERT = List.of(
      Type.BOOLEAN_TYPE,
      Type.BOOLEAN_TYPE,
      Type.DOUBLE_TYPE,
      BLOCK_STATE_TYPE,
      Type.BOOLEAN_TYPE,
      Type.BOOLEAN_TYPE,
      Type.BOOLEAN_TYPE
  );
  private static final int INSERT_SIZE = TO_INSERT.stream().mapToInt(Type::getSize).sum();

  public MixinFallingBlockEntityTransformer() {
    super(MIXIN_NAME);
  }

  @Override
  protected void transform(ClassNode classNode) {
    for (MethodNode method : classNode.methods) {
      if (method.name.equals(MIXIN_METHOD_NAME)) {
        this.patchLocalVariableArguments(method);
        break;
      }
    }
  }

  private void patchLocalVariableArguments(MethodNode method) {
    List<Type> argumentTypes = new ArrayList<>(Arrays.asList(Type.getArgumentTypes(method.desc)));

    // Insert missing local variables
    int insertIndex = argumentTypes.indexOf(BLOCK_POS_TYPE) + 1;
    argumentTypes.addAll(insertIndex, TO_INSERT);

    // Adjust method descriptor
    method.desc = Type.getMethodDescriptor(
        Type.getReturnType(method.desc),
        argumentTypes.toArray(new Type[0])
    );

    // Calculate insert index with respect to type sizes
    int localVariableInsertIndex = 0;
    for (int i = 0; i < insertIndex; i++) {
      localVariableInsertIndex += argumentTypes.get(i).getSize();
    }

    // Shift var instructions which are affected by the insertion
    for (AbstractInsnNode instruction : method.instructions) {
      if (instruction instanceof VarInsnNode varInsnNode) {
        if (varInsnNode.var > localVariableInsertIndex) {
          varInsnNode.var += INSERT_SIZE;
        }
      }
    }
  }
}
