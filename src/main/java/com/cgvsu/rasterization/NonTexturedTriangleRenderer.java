package com.cgvsu.rasterization;

import com.cgvsu.math.Point2f;
import com.cgvsu.triangulation.DrawWireframe;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class NonTexturedTriangleRenderer implements TriangleRenderer {
    private static Color baseColor;
    private final double[][] zBuffer;

    public NonTexturedTriangleRenderer(Color baseColor, double[][] zBuffer) {
        this.baseColor = baseColor;
        this.zBuffer = zBuffer;
    }

    @Override
    public void render(
            final GraphicsContext graphicsContext,
            int[] arrX,
            int[] arrY,
            float[] arrZ,
            Point2f[] texCoords,
            float[] lightIntensities,
            final boolean useLighting) {

        // Сортировка вершин по Y
        Rasterization.sortVerticesByY(arrX, arrY, arrZ, lightIntensities);

        for (int y = arrY[0]; y <= arrY[2]; y++) {
            if (y < 0 || y >= zBuffer[0].length) continue;

            int x1, x2;
            float z1, z2, i1, i2;

            // Интерполяция данных для каждой строки
            if (y <= arrY[1]) {
                x1 = Rasterization.interpolateX(y, arrY[0], arrY[1], arrX[0], arrX[1]);
                x2 = Rasterization.interpolateX(y, arrY[0], arrY[2], arrX[0], arrX[2]);
                z1 = Rasterization.interpolate(y, arrY[0], arrY[1], arrZ[0], arrZ[1]);
                z2 = Rasterization.interpolate(y, arrY[0], arrY[2], arrZ[0], arrZ[2]);
                i1 = Rasterization.interpolate(y, arrY[0], arrY[1], lightIntensities[0], lightIntensities[1]);
                i2 = Rasterization.interpolate(y, arrY[0], arrY[2], lightIntensities[0], lightIntensities[2]);
            } else {
                x1 = Rasterization.interpolateX(y, arrY[1], arrY[2], arrX[1], arrX[2]);
                x2 = Rasterization.interpolateX(y, arrY[0], arrY[2], arrX[0], arrX[2]);
                z1 = Rasterization.interpolate(y, arrY[1], arrY[2], arrZ[1], arrZ[2]);
                z2 = Rasterization.interpolate(y, arrY[0], arrY[2], arrZ[0], arrZ[2]);
                i1 = Rasterization.interpolate(y, arrY[1], arrY[2], lightIntensities[1], lightIntensities[2]);
                i2 = Rasterization.interpolate(y, arrY[0], arrY[2], lightIntensities[0], lightIntensities[2]);
            }

            if (x1 > x2) {
                int tempX = x1;
                x1 = x2;
                x2 = tempX;
                float tempZ = z1;
                z1 = z2;
                z2 = tempZ;
                float tempI = i1;
                i1 = i2;
                i2 = tempI;
            }
            drawLineWithZBuffer(graphicsContext, x1, y, z1, i1, x2, y, z2, i2, zBuffer, useLighting);
        }
    }

    public static void drawLineWithZBuffer(GraphicsContext gc, int x0, int y0, float z0, float i0, int x1, int y1, float z1, float i1, double[][] zBuffer, boolean useLighting) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;

        while (true) {
            if (x0 >= 0 && x0 < zBuffer.length && y0 >= 0 && y0 < zBuffer[0].length) {
                // Интерполяция между вершинами для Z и освещения
                float t = (float) (Math.sqrt((x1 - x0) * (x1 - x0) + (y1 - y0) * (y1 - y0)) / Math.sqrt(dx * dx + dy * dy));
                float z = z0 * (1 - t) + z1 * t; // Интерполяция Z
                float intensity = i0 * (1 - t) + i1 * t; // Интерполяция освещения

                if (z < zBuffer[x0][y0]) {
                    zBuffer[x0][y0] = z;

                    // Учитываем освещение, если оно включено
                    Color colorToDraw = baseColor;
                    if (useLighting) {
                        intensity = Math.max(0, Math.min(1, intensity)); // Ограничиваем интенсивность в пределах [0, 1]
                        colorToDraw = baseColor.deriveColor(0, 1, intensity, 1);
                    }

                    gc.getPixelWriter().setColor(x0, y0, colorToDraw);
                }
            }

            if (x0 == x1 && y0 == y1) break;
            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x0 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y0 += sy;
            }
        }
    }
}