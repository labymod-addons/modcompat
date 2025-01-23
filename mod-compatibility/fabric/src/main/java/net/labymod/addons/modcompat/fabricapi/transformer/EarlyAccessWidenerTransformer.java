package net.labymod.addons.modcompat.fabricapi.transformer;

import net.labymod.addons.modcompat.fabricapi.accesswidener.AccessWatcherService;
import net.labymod.api.models.addon.annotation.EarlyAddonTransformer;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import java.util.Collection;

@EarlyAddonTransformer
public class EarlyAccessWidenerTransformer implements IClassTransformer {

  static {
    Launch.classLoader.addTransformerExclusion("net.labymod.addons.modcompat.fabricapi.accesswidener.");
    Launch.classLoader.addTransformerExclusion("net.labymod.addons.modcompat.util.");
  }

  @Override
  public byte[] transform(String name, String transformedName, byte... classData) {
    if (classData == null) {
      return null;
    }

    Collection<String> values = AccessWatcherService.getValues(name);
    if (values != null && !values.isEmpty()) {
      ClassNode node = new ClassNode();
      ClassReader reader = new ClassReader(classData);
      reader.accept(node, 0);

      for (MethodNode method : node.methods) {
        String key = method.name + method.desc;
        if (values.contains(key)) {
          AccessWatcherService.setAccess(key, method.access);
        }
      }
    }

    return classData;
  }

  @Override
  public int getPriority() {
    return 10;
  }
}
