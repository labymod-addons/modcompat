package net.labymod.addons.modcompat.iris;

import net.irisshaders.iris.api.v0.IrisApi;
import net.labymod.api.client.gfx.pipeline.renderer.shadow.ShadowRenderPassContext;
import net.labymod.api.util.math.vector.FloatMatrix4;
import org.jetbrains.annotations.Nullable;

public class IrisShadowRenderPassContext implements ShadowRenderPassContext {

  @Override
  public boolean isShadowRenderPass() {
    return IrisApi.getInstance().isRenderingShadowPass();
  }

  @Override
  public @Nullable FloatMatrix4 getShadowModelViewMatrix() {
    return null;
  }

  @Override
  public @Nullable FloatMatrix4 getShadowModelViewInverseMatrix() {
    return null;
  }

  @Override
  public @Nullable FloatMatrix4 getShadowProjectionMatrix() {
    return null;
  }

  @Override
  public @Nullable FloatMatrix4 getShadowProjectionInverseMatrix() {
    return null;
  }
}
