package com.cgvsu.rasterization;

import com.cgvsu.math.Point2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.triangulation.DrawWireframe;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

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
            List<Vector3f> lightSources,
            ArrayList<Vector3f> normals) {

        // Расчет интенсивности освещения для каждой вершины
        float[] lightIntensities = new float[3];
        for (int i = 0; i < 3; i++) {
            Vector3f normal = normals.get(i).normalizek();
            float totalIntensity = 0;

            for (Vector3f lightSource : lightSources) {
                Vector3f lightDirection = lightSource.normalizek();
                float intensity = Math.max(0, Vector3f.dotProduct(normal, lightDirection)); // Ламбертово затенение
                totalIntensity += intensity;
            }

            lightIntensities[i] = Math.min(1, totalIntensity); // Ограничение интенсивности
        }

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
            for (int x = Math.max(0, x1); x < Math.min(zBuffer.length - 1, x2); x++) {
                float z = Rasterization.interpolate(x, x1, x2, z1, z2);
                float intensity = Rasterization.interpolate(x, x1, x2, i1, i2);

                if (z < zBuffer[x][y]) {
                    zBuffer[x][y] = z;

                    Color shadedColor = baseColor.deriveColor(0, 1, intensity, 1);
                    graphicsContext.getPixelWriter().setColor(x, y, shadedColor);
                }
            }
        }
    }
}
