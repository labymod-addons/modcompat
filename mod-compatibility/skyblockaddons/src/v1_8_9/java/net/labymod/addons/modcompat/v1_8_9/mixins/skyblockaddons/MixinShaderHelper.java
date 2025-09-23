package net.labymod.addons.modcompat.v1_8_9.mixins.skyblockaddons;

import codes.biscuit.skyblockaddons.shader.ShaderHelper;
import java.nio.ByteBuffer;
import net.labymod.api.Laby;
import net.labymod.laby3d.api.RenderDevice;
import net.labymod.laby3d.api.opengl.GlRenderDevice;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;

@Pseudo
@Mixin(value = ShaderHelper.class, remap = false)
public class MixinShaderHelper {

  /**
   * @author LabyMedia GmbH
   * @reason LabyMod updates legacy Minecraft to LWJGL 3
   */
  @Overwrite
  public static void glShaderSource(int shaderIn, ByteBuffer string) {
    byte[] bytes = new byte[string.remaining()];
    string.get(bytes);
    String source = new String(bytes);

    RenderDevice renderDevice = Laby.references().laby3D().renderDevice();
    if (renderDevice instanceof GlRenderDevice glRenderDevice) {
      glRenderDevice.functions().shaderSource(shaderIn, source);
    }
  }
}
