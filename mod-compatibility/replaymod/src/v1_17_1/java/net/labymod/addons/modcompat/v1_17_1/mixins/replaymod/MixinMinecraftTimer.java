package net.labymod.addons.modcompat.v1_17_1.mixins.replaymod;

import net.labymod.addons.modcompat.replaymod.accessor.MinecraftTimerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Timer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Minecraft.class)
public class MixinMinecraftTimer implements MinecraftTimerAccessor {

  @Shadow
  @Final
  private Timer timer;

  @Override
  public void setTickDelta(float tickDelta) {
    this.timer.tickDelta = tickDelta;
  }
}