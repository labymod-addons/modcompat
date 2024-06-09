package net.labymod.addons.modcompat.transformer;

import net.labymod.api.mapping.MappingService;
import net.labymod.api.mapping.provider.MappingProvider;
import net.labymod.api.util.CollectionHelper;
import net.labymod.api.volt.asm.util.ASMContext;
import net.labymod.api.volt.asm.util.ASMHelper;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

public abstract class MixinClassTransformer implements IClassTransformer {

  // Mixin
  protected static final String MIXIN_DESC = "Lorg/spongepowered/asm/mixin/Mixin;";
  protected static final String INJECT_DESC = "Lorg/spongepowered/asm/mixin/injection/Inject;";
  protected static final String MODIFY_VARIABLE_DESC = "Lorg/spongepowered/asm/mixin/injection/ModifyVariable;";
  protected static final String SHADOW_DESC = "Lorg/spongepowered/asm/mixin/Shadow;";
  // Mixin Extras
  protected static final String LOCAL_DESC = "Lcom/llamalad7/mixinextras/sugar/Local;";

  protected static final MappingProvider MAPPINGS = MappingService.instance().currentMappings();

  static {
    ASMContext.setPlatformClassLoader(Launch.classLoader);
    ASMContext.setResourceFinder(Launch.classLoader::loadResource);
  }

  private final String[] mixinNames;

  public MixinClassTransformer(String... mixinNames) {
    this.mixinNames = mixinNames;
  }

  protected static Type getType(String className) {
    return Type.getType("L" + MAPPINGS.mapClass(className) + ";");
  }

  @Override
  public int getPriority() {
    return 999;
  }

  @Override
  public byte[] transform(String name, String transformedName, byte... bytes) {
    if (CollectionHelper.contains(this.mixinNames, name)
        && this.shouldTransform(name, transformedName, bytes)) {
      return ASMHelper.transformClassData(bytes, this::transform);
    }
    return bytes;
  }

  protected boolean shouldTransform(String name, String transformedName, byte... bytes) {
    return true;
  }

  protected abstract void transform(ClassNode classNode);
}
