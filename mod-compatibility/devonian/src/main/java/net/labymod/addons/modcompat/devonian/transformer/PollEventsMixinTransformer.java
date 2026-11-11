package net.labymod.addons.modcompat.devonian.transformer;

import java.util.ArrayList;
import java.util.List;
import net.labymod.addons.modcompat.transformer.MixinClassTransformer;
import net.labymod.api.mapping.provider.child.ClassMapping;
import net.labymod.api.models.addon.annotation.EarlyAddonTransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

/**
 * Moves Devonian's {@code GLFWMixin} off {@code GLFW#glfwPollEvents}, or
 * {@code SDLEvents#SDL_PollEvent} on 26.3+, onto a {@code @WrapOperation} around that call in
 * Minecraft's {@code pollEvents}. LabyMod loads GLFW and SDL before Mixin prepares its configs, so
 * the original targets crash with {@code MixinTargetAlreadyLoadedException}.
 */
@EarlyAddonTransformer
public class PollEventsMixinTransformer extends MixinClassTransformer {

  private static final String MIXIN_NAME = "com.github.synnerz.devonian.mixin.GLFWMixin";
  private static final String HANDLER_NAME = "devonian$noCursorReset";

  private static final Type GLFW_TYPE = Type.getType("Lorg/lwjgl/glfw/GLFW;");
  private static final Type SDL_EVENTS_TYPE = Type.getType("Lorg/lwjgl/sdl/SDLEvents;");

  private static final String RENDER_SYSTEM_NAME = "com/mojang/blaze3d/systems/RenderSystem";
  private static final String SDL_EVENT_HANDLER_NAME = "com/mojang/blaze3d/platform/SDLEventHandler";
  private static final String POLL_EVENTS_NAME = "pollEvents";
  private static final String POLL_EVENTS_DESC = "()V";

  private static final String GLFW_POLL_EVENTS_TARGET = "Lorg/lwjgl/glfw/GLFW;glfwPollEvents()V";
  private static final String SDL_POLL_EVENT_TARGET = "Lorg/lwjgl/sdl/SDLEvents;SDL_PollEvent(Lorg/lwjgl/sdl/SDL_Event;)Z";

  private static final String WRAP_OPERATION_DESC = "Lcom/llamalad7/mixinextras/injector/wrapoperation/WrapOperation;";
  private static final String AT_DESC = "Lorg/spongepowered/asm/mixin/injection/At;";
  private static final String OPERATION_NAME = "com/llamalad7/mixinextras/injector/wrapoperation/Operation";
  private static final String OPERATION_CALL_DESC = "([Ljava/lang/Object;)Ljava/lang/Object;";

  private static final String GLFW_HANDLER_DESC = "(L" + OPERATION_NAME + ";)V";
  private static final String SDL_HANDLER_DESC = "(Lorg/lwjgl/sdl/SDL_Event;L" + OPERATION_NAME + ";)Z";

  private static final String NO_CURSOR_RESET_NAME = "com/github/synnerz/devonian/features/misc/inventory/NoCursorReset";
  private static final String IGNORE_FIRST_BATCH_NAME = "ignoreFirstBatch";

  public PollEventsMixinTransformer() {
    super(MIXIN_NAME);
  }

  @Override
  protected void transform(ClassNode classNode) {
    AnnotationNode mixin = findMixinAnnotation(classNode);
    if (mixin == null) {
      return;
    }

    int targetsIndex = mixin.values.indexOf("value") + 1;
    List<?> targets = (List<?>) mixin.values.get(targetsIndex);
    String pollerName;
    String callTarget;
    MethodNode handler;
    if (targets.contains(GLFW_TYPE)) {
      pollerName = RENDER_SYSTEM_NAME;
      callTarget = GLFW_POLL_EVENTS_TARGET;
      handler = createGlfwHandler();
    } else if (targets.contains(SDL_EVENTS_TYPE)) {
      pollerName = SDL_EVENT_HANDLER_NAME;
      callTarget = SDL_POLL_EVENT_TARGET;
      handler = createSdlHandler();
    } else {
      return;
    }

    List<Type> newTargets = new ArrayList<>();
    newTargets.add(getType(pollerName));
    mixin.values.set(targetsIndex, newTargets);

    handler.visibleAnnotations = new ArrayList<>();
    handler.visibleAnnotations.add(createWrapOperation(pollerName, callTarget));

    classNode.methods.removeIf(method -> HANDLER_NAME.equals(method.name));
    classNode.methods.add(handler);
  }

  private static AnnotationNode findMixinAnnotation(ClassNode classNode) {
    if (classNode.invisibleAnnotations == null) {
      return null;
    }

    for (AnnotationNode annotation : classNode.invisibleAnnotations) {
      if (MIXIN_DESC.equals(annotation.desc)) {
        return annotation;
      }
    }
    return null;
  }

  private static AnnotationNode createWrapOperation(String pollerName, String callTarget) {
    ClassMapping pollerMapping = MAPPINGS.getClassMapping(pollerName);
    String pollEventsName = pollerMapping == null
        ? POLL_EVENTS_NAME
        : pollerMapping.mapMethod(POLL_EVENTS_NAME, POLL_EVENTS_DESC);

    AnnotationNode at = new AnnotationNode(AT_DESC);
    at.values = new ArrayList<>(List.of(
        "value", "INVOKE",
        "target", callTarget,
        "remap", false
    ));

    AnnotationNode wrapOperation = new AnnotationNode(WRAP_OPERATION_DESC);
    wrapOperation.values = new ArrayList<>(List.of(
        "method", new ArrayList<>(List.of(pollEventsName + POLL_EVENTS_DESC)),
        "at", new ArrayList<>(List.of(at)),
        "remap", false
    ));
    return wrapOperation;
  }

  /**
   * Builds {@code static void handler(Operation<Void> original)}, which calls
   * {@code glfwPollEvents} and then decrements {@code ignoreFirstBatch}.
   */
  private static MethodNode createGlfwHandler() {
    MethodNode handler = new MethodNode(
        Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC,
        HANDLER_NAME,
        GLFW_HANDLER_DESC,
        null,
        null
    );

    InsnList insns = handler.instructions;
    insns.add(new VarInsnNode(Opcodes.ALOAD, 0));
    insns.add(new InsnNode(Opcodes.ICONST_0));
    insns.add(new TypeInsnNode(Opcodes.ANEWARRAY, "java/lang/Object"));
    insns.add(callOperation());
    insns.add(new InsnNode(Opcodes.POP));
    insns.add(decrementIgnoreFirstBatch());
    insns.add(new InsnNode(Opcodes.RETURN));

    handler.maxStack = 2;
    handler.maxLocals = 1;
    return handler;
  }

  /**
   * Builds {@code boolean handler(SDL_Event event, Operation<Boolean> original)}, which calls
   * {@code SDL_PollEvent}, decrements {@code ignoreFirstBatch} and returns the poll result.
   */
  private static MethodNode createSdlHandler() {
    MethodNode handler = new MethodNode(
        Opcodes.ACC_PRIVATE,
        HANDLER_NAME,
        SDL_HANDLER_DESC,
        null,
        null
    );

    InsnList insns = handler.instructions;
    insns.add(new VarInsnNode(Opcodes.ALOAD, 2));
    insns.add(new InsnNode(Opcodes.ICONST_1));
    insns.add(new TypeInsnNode(Opcodes.ANEWARRAY, "java/lang/Object"));
    insns.add(new InsnNode(Opcodes.DUP));
    insns.add(new InsnNode(Opcodes.ICONST_0));
    insns.add(new VarInsnNode(Opcodes.ALOAD, 1));
    insns.add(new InsnNode(Opcodes.AASTORE));
    insns.add(callOperation());
    insns.add(new TypeInsnNode(Opcodes.CHECKCAST, "java/lang/Boolean"));
    insns.add(new MethodInsnNode(
        Opcodes.INVOKEVIRTUAL,
        "java/lang/Boolean",
        "booleanValue",
        "()Z",
        false
    ));
    insns.add(new VarInsnNode(Opcodes.ISTORE, 3));
    insns.add(decrementIgnoreFirstBatch());
    insns.add(new VarInsnNode(Opcodes.ILOAD, 3));
    insns.add(new InsnNode(Opcodes.IRETURN));

    handler.maxStack = 5;
    handler.maxLocals = 4;
    return handler;
  }

  private static MethodInsnNode callOperation() {
    return new MethodInsnNode(
        Opcodes.INVOKEINTERFACE,
        OPERATION_NAME,
        "call",
        OPERATION_CALL_DESC,
        true
    );
  }

  private static InsnList decrementIgnoreFirstBatch() {
    InsnList insns = new InsnList();
    insns.add(new FieldInsnNode(Opcodes.GETSTATIC, NO_CURSOR_RESET_NAME, IGNORE_FIRST_BATCH_NAME, "I"));
    insns.add(new InsnNode(Opcodes.ICONST_1));
    insns.add(new InsnNode(Opcodes.ISUB));
    insns.add(new FieldInsnNode(Opcodes.PUTSTATIC, NO_CURSOR_RESET_NAME, IGNORE_FIRST_BATCH_NAME, "I"));
    return insns;
  }
}
