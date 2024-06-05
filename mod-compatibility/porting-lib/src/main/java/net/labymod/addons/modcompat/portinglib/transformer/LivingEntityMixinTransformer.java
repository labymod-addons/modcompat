package net.labymod.addons.modcompat.portinglib.transformer;

import net.labymod.addons.modcompat.transformer.MixinClassTransformer;
import net.labymod.api.models.addon.annotation.EarlyAddonTransformer;
import net.labymod.core.main.BuildData;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * Fixes a local variable modification by specifying the ordinal. The first float should be used.
 */
@EarlyAddonTransformer
public class LivingEntityMixinTransformer extends MixinClassTransformer {

  private static final String MIXIN_NAME = "io.github.fabricators_of_create.porting_lib.mixin.common.LivingEntityMixin";
  private static final String MIXIN_METHOD_NAME = "port_lib$setSlipperiness";

  public LivingEntityMixinTransformer() {
    super(MIXIN_NAME);
  }

  @Override
  protected boolean shouldTransform(String name, String transformedName, byte... bytes) {
    return !BuildData.version().isGreaterThan(BROKEN_FRAMES_VERSION);
  }

  @Override
  protected void transform(ClassNode classNode) {
    for (MethodNode method : classNode.methods) {
      if (MIXIN_METHOD_NAME.equals(method.name) && method.visibleAnnotations != null) {
        for (AnnotationNode visibleAnnotation : method.visibleAnnotations) {
          if (MODIFY_VARIABLE_DESC.equals(visibleAnnotation.desc)) {
            visibleAnnotation.values.add("ordinal");
            visibleAnnotation.values.add(0);
            break;
          }
        }
        break;
      }
    }
  }
}
