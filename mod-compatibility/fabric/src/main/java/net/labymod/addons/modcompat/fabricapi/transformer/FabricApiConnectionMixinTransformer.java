package net.labymod.addons.modcompat.fabricapi.transformer;

import net.labymod.addons.modcompat.transformer.MixinClassTransformer;
import net.labymod.api.loader.MinecraftVersions;
import net.labymod.api.models.addon.annotation.EarlyAddonTransformer;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

@EarlyAddonTransformer
public class FabricApiConnectionMixinTransformer extends MixinClassTransformer {

  private static final String CONNECTION_MIXIN_NAME = "net.fabricmc.fabric.mixin.networking.ClientConnectionMixin";

  public FabricApiConnectionMixinTransformer() {
    super(CONNECTION_MIXIN_NAME);
  }

  @Override
  protected boolean shouldTransform(String name, String transformedName, byte... bytes) {
    return MinecraftVersions.V1_20_2.orNewer();
  }

  @Override
  protected void transform(ClassNode classNode) {
    for (MethodNode methodNode : classNode.methods) {
      if (methodNode.name.equals("disconnectAddon") && methodNode.visibleAnnotations != null) {
        for (AnnotationNode visibleAnnotation : methodNode.visibleAnnotations) {
          if (visibleAnnotation.desc.equals(MODIFY_VARIABLE_DESC)) {
            this.patchModifyVariableOrdinal(visibleAnnotation);
            break;
          }
        }
      }
    }
  }

  private void patchModifyVariableOrdinal(AnnotationNode annotationNode) {
    // There are two PacketListener local variables in the original method, but only one in fabrics.
    // Setting the ordinal to specify which instance is targeted.
    annotationNode.values.add("ordinal");
    annotationNode.values.add(1);
  }
}
