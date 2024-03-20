package net.labymod.addons.modcompat.v1_8_9.skyblockaddons;

import codes.biscuit.skyblockaddons.core.Feature;
import net.labymod.api.client.render.matrix.Stack;

public class FeatureDrawContext {

  private static final FeatureDrawContext INSTANCE = new FeatureDrawContext();

  private Feature drawnFeature;
  private Stack stack;
  private boolean noRender;
  private int width;
  private int height;

  public static FeatureDrawContext get() {
    return INSTANCE;
  }

  public void reset() {
    this.drawnFeature = null;
    this.stack = null;
    this.noRender = false;
    this.width = 0;
    this.height = 0;
  }

  public boolean isActive() {
    return this.drawnFeature != null;
  }

  public Feature getDrawnFeature() {
    return this.drawnFeature;
  }

  public void setDrawnFeature(Feature drawnFeature) {
    this.drawnFeature = drawnFeature;
  }

  public Stack getStack() {
    return this.stack;
  }

  public void setStack(Stack stack) {
    this.stack = stack;
  }

  public boolean isNoRender() {
    return this.noRender;
  }

  public void setNoRender(boolean noRender) {
    this.noRender = noRender;
  }

  public int getWidth() {
    return this.width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return this.height;
  }

  public void setHeight(int height) {
    this.height = height;
  }
}
