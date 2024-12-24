package com.cgvsu.rasterization;

import com.cgvsu.math.Point2f;
import javafx.scene.canvas.GraphicsContext;

public interface TriangleRenderer {
    void render(
            final GraphicsContext graphicsContext,
            int[] arrX,
            int[] arrY,
            float[] arrZ,
            Point2f[] texCoords,
            float[] lightIntensities,
            final boolean useLighting);
}
