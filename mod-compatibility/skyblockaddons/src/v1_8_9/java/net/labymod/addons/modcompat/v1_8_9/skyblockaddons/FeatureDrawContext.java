package net.labymod.addons.modcompat.v1_8_9.skyblockaddons;

import codes.biscuit.skyblockaddons.core.Feature;

public class FeatureDrawContext {

  private static final FeatureDrawContext INSTANCE = new FeatureDrawContext();

  private Feature drawnFeature;
  private boolean isEditor;
  private int width;
  private int height;

  public static FeatureDrawContext get() {
    return INSTANCE;
  }

  public void reset() {
    this.drawnFeature = null;
    this.isEditor = false;
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

  public boolean isEditor() {
    return this.isEditor;
  }

  public void setEditor(boolean editor) {
    isEditor = editor;
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
