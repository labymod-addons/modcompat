package net.labymod.addons.modcompat.v1_8_9.mixins.skyblockaddons;

import codes.biscuit.skyblockaddons.shader.ShaderHelper;
import java.nio.ByteBuffer;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL20;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

@Pseudo
@Mixin(value = ShaderHelper.class, remap = false)
public class MixinShaderHelper {


  @Shadow
  @Final
  private static boolean USING_ARB_SHADERS;

  /**
   * @author LabyMedia GmbH
   * @reason LabyMod updates legacy Minecraft to LWJGL 3
   */
  @Overwrite
  public static void glShaderSource(int shaderIn, ByteBuffer string) {
    byte[] bytes = new byte[string.remaining()];
    string.get(bytes);
    String source = new String(bytes);

    if (USING_ARB_SHADERS) {
      ARBShaderObjects.glShaderSourceARB(shaderIn, source);
    } else {
      GL20.glShaderSource(shaderIn, source);
    }
  }
}
