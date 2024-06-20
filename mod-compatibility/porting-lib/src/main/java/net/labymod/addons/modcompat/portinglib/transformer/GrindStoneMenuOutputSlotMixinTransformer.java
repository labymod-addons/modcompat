package net.labymod.addons.modcompat.portinglib.transformer;

import java.util.List;
import net.labymod.addons.modcompat.transformer.MixinClassTransformer;
import net.labymod.api.mapping.provider.child.ClassMapping;
import net.labymod.api.models.addon.annotation.EarlyAddonTransformer;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

/**
 * Fixes that a shadow field used the hardcoded intermediary name by adding the actual shadow field
 * name to the list of aliases.
 */
@EarlyAddonTransformer
public class GrindStoneMenuOutputSlotMixinTransformer extends MixinClassTransformer {

  private static final String MIXIN_NAME = "io.github.fabricators_of_create.porting_lib.mixin.common.GrindstoneMenuMixin$GrindstoneMenuOutputSlotMixin";
  private static final String SHADOW_FIELD_NAME = "menu";

  private static final ClassMapping GRINDSTONE_MAPPING = MAPPINGS
      .getClassMapping("net/minecraft/world/inventory/GrindstoneMenu$4");
  private static final String ACTUAL_SHADOW_FIELD_NAME = GRINDSTONE_MAPPING == null
      ? "this$0"
      : GRINDSTONE_MAPPING.mapField("this$0");

  public GrindStoneMenuOutputSlotMixinTransformer() {
    super(MIXIN_NAME);
  }

  @Override
  protected void transform(ClassNode classNode) {
    for (FieldNode field : classNode.fields) {
      if (SHADOW_FIELD_NAME.equals(field.name) && field.visibleAnnotations != null) {
        for (AnnotationNode visibleAnnotation : field.visibleAnnotations) {
          //noinspection rawtypes
          if (SHADOW_DESC.equals(visibleAnnotation.desc)
              && visibleAnnotation.values != null
              && visibleAnnotation.values.size() >= 2
              && visibleAnnotation.values.get(1) instanceof List list) {
            //noinspection unchecked
            list.add(ACTUAL_SHADOW_FIELD_NAME);
            break;
          }
        }
        break;
      }
    }
  }
}
