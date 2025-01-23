package net.labymod.addons.modcompat.fabricapi.transformer;

import net.labymod.addons.modcompat.fabricapi.accesswidener.AccessWatcherService;
import net.labymod.api.models.addon.annotation.EarlyAddonTransformer;
import net.labymod.api.volt.asm.util.ASMHelper;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import java.util.Collection;

@EarlyAddonTransformer
public class LateAccessWidenerTransformer implements IClassTransformer {

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
        int access = AccessWatcherService.getAccess(method.name + method.desc);
        if (access != Integer.MAX_VALUE && access != method.access) {
          method.access = access;
        }
      }

      ClassWriter writer = ASMHelper.newClassWriter(reader, false);
      node.accept(writer);
      return writer.toByteArray();
    }

    return classData;
  }

  @Override
  public int getPriority() {
    return IClassTransformer.super.getPriority() * 10;
  }
}
