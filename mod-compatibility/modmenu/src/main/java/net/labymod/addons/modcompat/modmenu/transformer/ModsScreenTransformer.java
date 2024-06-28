package net.labymod.addons.modcompat.modmenu.transformer;

import java.util.Collection;
import java.util.List;
import net.labymod.api.models.addon.annotation.EarlyAddonTransformer;
import net.labymod.api.volt.asm.util.ASMHelper;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

@EarlyAddonTransformer
public class ModsScreenTransformer implements IClassTransformer {

  private static final Collection<String> CLASS_NAMES = List.of(
      "com.terraformersmc.modmenu.gui.ModsScreen",
      "io.github.prospector.modmenu.gui.ModsScreen"
  );

  private static final String FABRIC_LOADER_NAME = "net/fabricmc/loader/api/FabricLoader";
  private static final String GET_GAME_DIR_NAME = "getGameDir";
  private static final String GET_GAME_DIR_DESC = "()Ljava/nio/file/Path;";

  private static final String HOOKS_NAME = ModMenuHooks.class.getName().replace('.', '/');

  @Override
  public int getPriority() {
    return 999;
  }

  @Override
  public byte[] transform(String name, String transformedName, byte... clasData) {
    if (name != null && CLASS_NAMES.contains(name)) {
      return ASMHelper.transformClassData(clasData, this::transformGameDirCalls);
    }
    return clasData;
  }

  private void transformGameDirCalls(ClassNode classNode) {
    for (MethodNode method : classNode.methods) {
      MethodInsnNode target = null;

      for (AbstractInsnNode instruction : method.instructions) {
        if (instruction instanceof MethodInsnNode methodInsnNode
            && methodInsnNode.getOpcode() == Opcodes.INVOKEINTERFACE
            && methodInsnNode.owner.equals(FABRIC_LOADER_NAME)
            && methodInsnNode.name.equals(GET_GAME_DIR_NAME)
            && methodInsnNode.desc.equals(GET_GAME_DIR_DESC)
        ) {
          target = methodInsnNode;
          break;
        }
      }

      if (target != null) {
        method.instructions.insert(target, new MethodInsnNode(
            Opcodes.INVOKESTATIC,
            HOOKS_NAME,
            GET_GAME_DIR_NAME,
            GET_GAME_DIR_DESC,
            false
        ));

        // Remove the original call to FabricLoader.getInstance()
        method.instructions.remove(target.getPrevious());
        // Remove the original call to FabricLoader#getGameDir()
        method.instructions.remove(target);
      }
    }
  }
}
