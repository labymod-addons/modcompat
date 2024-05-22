package net.labymod.addons.modcompat.puzzleslib.transformer;

import net.labymod.addons.modcompat.transformer.MixinClassTransformer;
import net.labymod.api.models.addon.annotation.EarlyAddonTransformer;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

@EarlyAddonTransformer
public class LivingEntityFabricMixinTransformer extends MixinClassTransformer {

  private static final String MIXIN_NAME = "fuzs.puzzleslib.fabric.mixin.LivingEntityFabricMixin";
  private static final String MIXIN_METHOD_NAME = "baseTick$2";

  public LivingEntityFabricMixinTransformer() {
    super(MIXIN_NAME);
  }

  @Override
  protected void transform(ClassNode classNode) {
    for (MethodNode method : classNode.methods) {
      if (MIXIN_METHOD_NAME.equals(method.name) && method.visibleAnnotations != null) {
        for (AnnotationNode visibleAnnotation : method.visibleAnnotations) {
          if (MODIFY_VARIABLE_DESC.equals(visibleAnnotation.desc)) {
            visibleAnnotation.values.add("ordinal");
            visibleAnnotation.values.add(1);
            break;
          }
        }
        break;
      }
    }
  }
}
