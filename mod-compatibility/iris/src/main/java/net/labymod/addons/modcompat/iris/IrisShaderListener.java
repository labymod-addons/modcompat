package net.labymod.addons.modcompat.iris;

import net.irisshaders.iris.api.v0.IrisApi;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.render.shader.ShaderPipelineContextEvent;

public class IrisShaderListener {

  @Subscribe
  public void onShaderPipelineContext(ShaderPipelineContextEvent event) {
    event.setShadowRenderPassContext(new IrisShadowRenderPassContext());
    event.setActiveShaderPackSupplier(() -> IrisApi.getInstance().isShaderPackInUse());
  }
}
