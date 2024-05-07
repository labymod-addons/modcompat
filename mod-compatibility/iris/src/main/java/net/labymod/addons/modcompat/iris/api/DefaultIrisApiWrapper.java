package net.labymod.addons.modcompat.iris.api;

import net.irisshaders.iris.api.v0.IrisApi;

public class DefaultIrisApiWrapper implements IrisApiWrapper {

  @Override
  public boolean isShadowPass() {
    return IrisApi.getInstance().isRenderingShadowPass();
  }

  @Override
  public boolean isShaderActive() {
    return IrisApi.getInstance().isShaderPackInUse();
  }
}
