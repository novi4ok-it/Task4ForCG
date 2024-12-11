package com.cgvsu.rasterization;

import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.PixelWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Rasterization {

    public static void fillTriangle(
            final GraphicsContext graphicsContext,
            final int[] arrX,
            final int[] arrY,
            final Color color) {

        // Sort vertices by Y
        sortVerticesByY(arrX, arrY);

        // Rasterize the two halves of the triangle
        rasterizeHalf(graphicsContext, arrX, arrY, color, true);  // Bottom half
        rasterizeHalf(graphicsContext, arrX, arrY, color, false); // Top half
    }

    private static void rasterizeHalf(
            GraphicsContext graphicsContext,
            int[] arrX, int[] arrY,
            Color color,
            boolean isLower) {

        int yStart = isLower ? arrY[1] : arrY[0];
        int yEnd = isLower ? arrY[2] : arrY[1];
        if (!isLower) yStart++; // Top half is drawn downwards

        for (int y = yStart; y <= yEnd; y++) {
            if (y < 0 || y >= graphicsContext.getCanvas().getHeight()) continue;

            int x1 = interpolateX(y, arrY[isLower ? 1 : 0], arrY[2], arrX[isLower ? 1 : 0], arrX[2]);
            int x2 = interpolateX(y, arrY[0], arrY[2], arrX[0], arrX[2]);

            if (x1 > x2) {
                int temp = x1;
                x1 = x2;
                x2 = temp;
            }

            for (int x = Math.max(0, x1); x <= Math.min((int) graphicsContext.getCanvas().getWidth() - 1, x2); x++) {
                graphicsContext.getPixelWriter().setColor(x, y, color);
            }
        }
    }

    private static int interpolateX(int y, int y1, int y2, int x1, int x2) {
        return y2 == y1 ? x1 : (y - y1) * (x2 - x1) / (y2 - y1) + x1;
    }

    private static void sortVerticesByY(int[] x, int[] y) {
        if (y[0] > y[1]) swap(x, y, 0, 1);
        if (y[1] > y[2]) swap(x, y, 1, 2);
        if (y[0] > y[1]) swap(x, y, 0, 1);
    }

    private static void swap(int[] x, int[] y, int i, int j) {
        int tempX = x[i];
        int tempY = y[i];
        x[i] = x[j];
        y[i] = y[j];
        x[j] = tempX;
        y[j] = tempY;
    }
}