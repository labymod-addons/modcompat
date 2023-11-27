package net.labymod.addons.modcompat.v1_20_1.replaymod;

import net.labymod.api.volt.asm.tree.InsnListBuilder;
import net.labymod.api.volt.asm.util.ASMHelper;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;

public class ReplayModPauseButtonsTransformer implements IClassTransformer {

  private static final String GUI_RECORDING_CONTROLS_NAME = "com.replaymod.recording.gui.GuiRecordingControls";
  private static final String INJECT_METHOD_NAME = "injectIntoIngameMenu";

  @Override
  public byte[] transform(String name, String transformedName, byte... classData) {
    if (!GUI_RECORDING_CONTROLS_NAME.equals(name)) {
      return classData;
    }
    return ASMHelper.transformClassData(classData, this::patch);
  }

  private void patch(ClassNode classNode) {
    for (MethodNode method : classNode.methods) {
      if (INJECT_METHOD_NAME.equals(method.name)) {
        this.patchInjectMethod(method);
      }
    }
  }

  private void patchInjectMethod(MethodNode methodNode) {
    LabelNode label = new LabelNode();
    InsnList deadInsnList = InsnListBuilder.create()
        .addInstruction(Opcodes.ICONST_1)
        .addJump(Opcodes.IFEQ, label)
        .addInstruction(Opcodes.RETURN)
        .add(label)
        .build();

    methodNode.instructions.insertBefore(methodNode.instructions.getFirst(), deadInsnList);
  }
}
