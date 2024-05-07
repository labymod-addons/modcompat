package net.labymod.addons.modcompat.v1_20_2.mixins.fabricapi;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

@Pseudo
@Mixin(targets = "net.fabricmc.fabric.impl.networking.payload.RetainedPayload")
public abstract class MixinRetainedPayload implements CustomPacketPayload {

  @Shadow
  public abstract FriendlyByteBuf buf();

  @Override
  public void write(FriendlyByteBuf friendlyByteBuf) {
    friendlyByteBuf.writeBytes(this.buf(), 0, this.buf().readableBytes());
  }
}
