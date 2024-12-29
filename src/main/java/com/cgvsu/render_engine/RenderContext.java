package com.cgvsu.render_engine;

import com.cgvsu.model.Model;
import javafx.scene.canvas.GraphicsContext;

import java.util.List;

public class RenderContext {
    private final GraphicsContext graphicsContext;
    private final Camera camera;
    private final Model mesh;
    private final int width;
    private final int height;
    private final List<ColorLighting> lightSources;
    private final boolean isTextureEnabled;
    private final boolean isPolygonalGridEnabled;
    private final double[][] zBuffer;

    public RenderContext(
            GraphicsContext graphicsContext,
            Camera camera,
            Model mesh,
            int width,
            int height,
            List<ColorLighting> lightSources,
            boolean isTextureEnabled,
            boolean isPolygonalGridEnabled,
            double[][] zBuffer) {
        this.graphicsContext = graphicsContext;
        this.camera = camera;
        this.mesh = mesh;
        this.width = width;
        this.height = height;
        this.lightSources = lightSources;
        this.isTextureEnabled = isTextureEnabled;
        this.isPolygonalGridEnabled = isPolygonalGridEnabled;
        this.zBuffer = zBuffer;
    }

    public GraphicsContext getGraphicsContext() {
        return graphicsContext;
    }

    public Camera getCamera() {
        return camera;
    }

    public Model getMesh() {
        return mesh;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<ColorLighting> getLightSources() {
        return lightSources;
    }

    public boolean isTextureEnabled() {
        return isTextureEnabled;
    }

    public boolean isPolygonalGridEnabled() {
        return isPolygonalGridEnabled;
    }

    public double[][] getZBuffer() {
        return zBuffer;
    }
}
