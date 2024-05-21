package net.labymod.addons.modcompat.portinglib.transformer;

import java.util.List;
import net.labymod.api.mapping.MappingService;
import net.labymod.api.mapping.provider.child.ClassMapping;
import net.labymod.api.models.addon.annotation.EarlyAddonTransformer;
import net.labymod.api.volt.asm.util.ASMHelper;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

/**
 * Fixes that a shadow field used the hardcoded intermediary name by adding the actual shadow field
 * name to the list of aliases.
 */
@EarlyAddonTransformer
public class GrindStoneMenuOutputSlotMixinTransformer implements IClassTransformer {

  private static final String MIXIN_NAME = "io.github.fabricators_of_create.porting_lib.mixin.common.GrindstoneMenuMixin$GrindstoneMenuOutputSlotMixin";
  private static final String SHADOW_FIELD_NAME = "menu";

  private static final ClassMapping GRINDSTONE_MAPPING = MappingService.instance().currentMappings()
      .getClassMapping("net/minecraft/world/inventory/GrindstoneMenu$4");
  private static final String ACTUAL_SHADOW_FIELD_NAME = GRINDSTONE_MAPPING == null
      ? "this$0"
      : GRINDSTONE_MAPPING.mapField("this$0");

  private static final String SHADOW_DESC = "Lorg/spongepowered/asm/mixin/Shadow;";

  @Override
  public byte[] transform(String name, String transformedName, byte... classData) {
    if (!MIXIN_NAME.equals(name)) {
      return classData;
    }
    return ASMHelper.transformClassData(classData, this::patch);
  }

  private void patch(ClassNode classNode) {
    for (FieldNode field : classNode.fields) {
      if (SHADOW_FIELD_NAME.equals(field.name) && field.visibleAnnotations != null) {
        for (AnnotationNode visibleAnnotation : field.visibleAnnotations) {
          //noinspection rawtypes
          if (SHADOW_DESC.equals(visibleAnnotation.desc)
              && visibleAnnotation.values != null
              && visibleAnnotation.values.size() == 2
              && visibleAnnotation.values.get(1) instanceof List list) {
            //noinspection unchecked
            list.add(ACTUAL_SHADOW_FIELD_NAME);
          }
        }
        break;
      }
    }
  }
}
