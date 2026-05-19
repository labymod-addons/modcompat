package net.labymod.addons.modcompat.fabricapi.transformer.screen;

import net.labymod.addons.modcompat.transformer.MixinClassTransformer;
import net.labymod.api.models.addon.annotation.EarlyAddonTransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

/**
 * Refreshes the captured screen at the start of {@code onAfterRenderScreen} so the
 * {@code AFTER_RENDER} event fires for the screen that is currently active, not the one captured
 * before the render call. Without this, screens swapped during rendering miss the event.
 */
@EarlyAddonTransformer
public class GameRendererMixinTransformer extends MixinClassTransformer {

  private static final String MIXIN_NAME = "net.fabricmc.fabric.mixin.screen.GameRendererMixin";
  private static final String AFTER_RENDER_METHOD_NAME = "onAfterRenderScreen";
  private static final String RENDERING_SCREEN_FIELD_NAME = "renderingScreen";

  private static final String MINECRAFT_NAME = "net/minecraft/client/Minecraft";
  private static final String SCREEN_DESC = "Lnet/minecraft/client/gui/screens/Screen;";
  private static final String GET_INSTANCE_NAME = "getInstance";
  private static final String GET_INSTANCE_DESC = "()L" + MINECRAFT_NAME + ";";
  private static final String SCREEN_FIELD_NAME = "screen";

  public GameRendererMixinTransformer() {
    super(MIXIN_NAME);
  }

  @Override
  protected void transform(ClassNode classNode) {
    for (MethodNode method : classNode.methods) {
      if (AFTER_RENDER_METHOD_NAME.equals(method.name)) {
        this.prependScreenRefresh(classNode, method);
      }
    }
  }

  private void prependScreenRefresh(ClassNode classNode, MethodNode method) {
    InsnList insertion = new InsnList();
    insertion.add(new VarInsnNode(Opcodes.ALOAD, 0));
    insertion.add(new MethodInsnNode(
        Opcodes.INVOKESTATIC,
        MINECRAFT_NAME,
        GET_INSTANCE_NAME,
        GET_INSTANCE_DESC,
        false
    ));
    insertion.add(new FieldInsnNode(
        Opcodes.GETFIELD,
        MINECRAFT_NAME,
        SCREEN_FIELD_NAME,
        SCREEN_DESC
    ));
    insertion.add(new FieldInsnNode(
        Opcodes.PUTFIELD,
        classNode.name,
        RENDERING_SCREEN_FIELD_NAME,
        SCREEN_DESC
    ));
    method.instructions.insertBefore(method.instructions.getFirst(), insertion);
  }
}
