package net.labymod.addons.modcompat.mixin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.labymod.addons.modcompat.event.mixin.MixinAdditionalMixinsEvent;
import net.labymod.addons.modcompat.event.mixin.MixinApplyEvent;
import net.labymod.addons.modcompat.event.mixin.MixinLoadEvent;
import net.labymod.addons.modcompat.event.mixin.MixinShouldApplyEvent;
import net.labymod.api.Laby;
import net.labymod.api.event.Phase;
import net.labymod.api.mixin.ConfigPlugin;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

@ConfigPlugin
public class ModCompatMixinConfigPlugin implements IMixinConfigPlugin {

  @Override
  public void onLoad(String mixinPackage) {
    Laby.fireEvent(new MixinLoadEvent(mixinPackage));
  }

  @Override
  public String getRefMapperConfig() {
    return null;
  }

  @Override
  public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
    return Laby.fireEvent(new MixinShouldApplyEvent(true)).shouldApply();
  }

  @Override
  public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

  }

  @Override
  public List<String> getMixins() {
    return Laby.fireEvent(new MixinAdditionalMixinsEvent(new ArrayList<>())).getAdditionalMixins();
  }

  @Override
  public void preApply(
      String targetClassName,
      ClassNode targetClass,
      String mixinClassName,
      IMixinInfo mixinInfo
  ) {
    Laby.fireEvent(new MixinApplyEvent(
        Phase.PRE,
        targetClassName,
        targetClass,
        mixinClassName,
        mixinInfo
    ));
  }

  @Override
  public void postApply(
      String targetClassName,
      ClassNode targetClass,
      String mixinClassName,
      IMixinInfo mixinInfo
  ) {
    Laby.fireEvent(new MixinApplyEvent(
        Phase.POST,
        targetClassName,
        targetClass,
        mixinClassName,
        mixinInfo
    ));
  }
}
