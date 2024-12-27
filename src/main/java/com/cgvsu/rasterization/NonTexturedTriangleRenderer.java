package com.cgvsu.rasterization;

import com.cgvsu.math.Point2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.render_engine.ColorLighting;
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
    public double[][] getZBuffer() {
        return zBuffer;
    }

    @Override
    public void render(
            final GraphicsContext graphicsContext,
            int[] arrX,
            int[] arrY,
            float[] arrZ,
            Point2f[] texCoords,
            List<ColorLighting> colorLightings,
            ArrayList<Vector3f> normals) {

        Color[] vertexColors = new Color[3];

        if (colorLightings.isEmpty()) {
            // Если список освещения пуст, все вершины получают базовый цвет
            for (int i = 0; i < 3; i++) {
                vertexColors[i] = null;
            }
        } else {
            // Расчёт освещения для каждой вершины
            for (int i = 0; i < 3; i++) {
                Vector3f normal = normals.get(i).normalizek();
                Color accumulatedColor = Color.BLACK;

                for (ColorLighting lighting : colorLightings) {
                    Vector3f lightDirection = lighting.light.normalizek();
                    float intensity = Math.max(0, Vector3f.dotProduct(normal, lightDirection)); // Ламбертово затенение
                    Color lightColor = lighting.color;
                    accumulatedColor = accumulatedColor.interpolate(lightColor, intensity); // Смешиваем цвета
                }

                vertexColors[i] = accumulatedColor;
            }
        }

        // Сортировка вершин по Y
        Rasterization.sortVerticesByY(arrX, arrY, arrZ, vertexColors);

        for (int y = arrY[0]; y <= arrY[2]; y++) {
            if (y < 0 || y >= zBuffer[0].length) continue;

            int x1, x2;
            float z1, z2;
            Color c1, c2;

            // Интерполяция данных для каждой строки
            if (y <= arrY[1]) {
                x1 = Rasterization.interpolateX(y, arrY[0], arrY[1], arrX[0], arrX[1]);
                x2 = Rasterization.interpolateX(y, arrY[0], arrY[2], arrX[0], arrX[2]);
                z1 = Rasterization.interpolate(y, arrY[0], arrY[1], arrZ[0], arrZ[1]);
                z2 = Rasterization.interpolate(y, arrY[0], arrY[2], arrZ[0], arrZ[2]);
                c1 = Rasterization.interpolateColor(y, arrY[0], arrY[1], vertexColors[0], vertexColors[1]);
                c2 = Rasterization.interpolateColor(y, arrY[0], arrY[2], vertexColors[0], vertexColors[2]);
            } else {
                x1 = Rasterization.interpolateX(y, arrY[1], arrY[2], arrX[1], arrX[2]);
                x2 = Rasterization.interpolateX(y, arrY[0], arrY[2], arrX[0], arrX[2]);
                z1 = Rasterization.interpolate(y, arrY[1], arrY[2], arrZ[1], arrZ[2]);
                z2 = Rasterization.interpolate(y, arrY[0], arrY[2], arrZ[0], arrZ[2]);
                c1 = Rasterization.interpolateColor(y, arrY[1], arrY[2], vertexColors[1], vertexColors[2]);
                c2 = Rasterization.interpolateColor(y, arrY[0], arrY[2], vertexColors[0], vertexColors[2]);
            }

            if (x1 > x2) {
                int tempX = x1;
                x1 = x2;
                x2 = tempX;
                float tempZ = z1;
                z1 = z2;
                z2 = tempZ;
                Color tempC = c1;
                c1 = c2;
                c2 = tempC;
            }

            for (int x = Math.max(0, x1); x < Math.min(zBuffer.length - 1, x2); x++) {
                float z = Rasterization.interpolate(x, x1, x2, z1, z2);
                Color color = Rasterization.interpolateColor(x, x1, x2, c1, c2);

                if (z < zBuffer[x][y]) {
                    zBuffer[x][y] = z;
                    // Если освещение пустое, используем только базовый цвет
                    Color finalColor = colorLightings.isEmpty() ? baseColor : baseColor.interpolate(color, 0.5);
                    graphicsContext.getPixelWriter().setColor(x, y, finalColor);
                }
            }
        }
    }
}
