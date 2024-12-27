package com.cgvsu.rasterization;

import com.cgvsu.math.Point2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.render_engine.ColorLighting;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public interface TriangleRenderer {
    double[][] getZBuffer();

    void render(
            final GraphicsContext graphicsContext,
            int[] arrX,
            int[] arrY,
            float[] arrZ,
            Point2f[] texCoords,
            List<ColorLighting> lightSources,
            ArrayList<Vector3f> normals);
}
