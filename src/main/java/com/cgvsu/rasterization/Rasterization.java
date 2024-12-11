package com.cgvsu.rasterization;

import com.cgvsu.math.Point2f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import javafx.scene.image.PixelWriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Rasterization {

    private static double[][] zBuffer;

    public static void initializeZBuffer(int width, int height) {
        zBuffer = new double[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                zBuffer[i][j] = Double.POSITIVE_INFINITY; // Инициализируем максимальной глубиной
            }
        }
    }

    public static void fillTriangle(
            final GraphicsContext graphicsContext,
            final int[] arrX,
            final int[] arrY,
            final float[] arrZ,
            final Color color) {

        // Упорядочиваем вершины по Y
        sortVerticesByY(arrX, arrY, arrZ);

        for (int y = arrY[0]; y <= arrY[2]; y++) {
            if (y < 0 || y >= zBuffer[0].length) continue; // Проверяем границы Y

            int x1, x2;
            float z1, z2;

            if (y <= arrY[1]) {
                // Нижняя часть треугольника
                x1 = interpolateX(y, arrY[0], arrY[1], arrX[0], arrX[1]);
                x2 = interpolateX(y, arrY[0], arrY[2], arrX[0], arrX[2]);
                z1 = interpolateZ(y, arrY[0], arrY[1], arrZ[0], arrZ[1]);
                z2 = interpolateZ(y, arrY[0], arrY[2], arrZ[0], arrZ[2]);
            } else {
                // Верхняя часть треугольника
                x1 = interpolateX(y, arrY[1], arrY[2], arrX[1], arrX[2]);
                x2 = interpolateX(y, arrY[0], arrY[2], arrX[0], arrX[2]);
                z1 = interpolateZ(y, arrY[1], arrY[2], arrZ[1], arrZ[2]);
                z2 = interpolateZ(y, arrY[0], arrY[2], arrZ[0], arrZ[2]);
            }

            if (x1 > x2) {
                int tempX = x1;
                x1 = x2;
                x2 = tempX;
                float tempZ = z1;
                z1 = z2;
                z2 = tempZ;
            }

            for (int x = Math.max(0, x1); x <= Math.min(zBuffer.length - 1, x2); x++) {
                float z = interpolateZ(x, x1, x2, z1, z2);
                if (z < zBuffer[x][y]) {
                    zBuffer[x][y] = z;
                    graphicsContext.getPixelWriter().setColor(x, y, color);
                }
            }
        }
    }

    private static float interpolateZ(int value, int start, int end, float zStart, float zEnd) {
        return end == start ? zStart : (value - start) * (zEnd - zStart) / (end - start) + zStart;
    }

    private static int interpolateX(int y, int y1, int y2, int x1, int x2) {
        return y2 == y1 ? x1 : (y - y1) * (x2 - x1) / (y2 - y1) + x1;
    }

    private static void sortVerticesByY(int[] x, int[] y, float[] z) {
        if (y[0] > y[1]) swap(x, y, z, 0, 1);
        if (y[1] > y[2]) swap(x, y, z, 1, 2);
        if (y[0] > y[1]) swap(x, y, z, 0, 1);
    }

    private static void swap(int[] x, int[] y, float[] z, int i, int j) {
        int tempX = x[i];
        int tempY = y[i];
        float tempZ = z[i];
        x[i] = x[j];
        y[i] = y[j];
        z[i] = tempZ;
        x[j] = tempX;
        y[j] = tempY;
        z[j] = tempZ;
    }

    public static void fillTriangleWithTexture(
            final GraphicsContext graphicsContext,
            final int[] arrX,
            final int[] arrY,
            final float[] arrZ,
            final Point2f[] texCoords,
            final Image texture) {

        // Упорядочиваем вершины по Y
        sortVerticesByY(arrX, arrY, arrZ, texCoords);

        for (int y = arrY[0]; y <= arrY[2]; y++) {
            if (y < 0 || y >= graphicsContext.getCanvas().getHeight()) continue;

            int x1, x2;
            float z1, z2;
            Point2f t1, t2;

            if (y <= arrY[1]) {
                x1 = interpolateX(y, arrY[0], arrY[1], arrX[0], arrX[1]);
                x2 = interpolateX(y, arrY[0], arrY[2], arrX[0], arrX[2]);
                z1 = interpolate(y, arrY[0], arrY[1], arrZ[0], arrZ[1]);
                z2 = interpolate(y, arrY[0], arrY[2], arrZ[0], arrZ[2]);
                t1 = interpolateTexCoord(y, arrY[0], arrY[1], texCoords[0], texCoords[1]);
                t2 = interpolateTexCoord(y, arrY[0], arrY[2], texCoords[0], texCoords[2]);
            } else {
                x1 = interpolateX(y, arrY[1], arrY[2], arrX[1], arrX[2]);
                x2 = interpolateX(y, arrY[0], arrY[2], arrX[0], arrX[2]);
                z1 = interpolate(y, arrY[1], arrY[2], arrZ[1], arrZ[2]);
                z2 = interpolate(y, arrY[0], arrY[2], arrZ[0], arrZ[2]);
                t1 = interpolateTexCoord(y, arrY[1], arrY[2], texCoords[1], texCoords[2]);
                t2 = interpolateTexCoord(y, arrY[0], arrY[2], texCoords[0], texCoords[2]);
            }

            if (x1 > x2) {
                int tempX = x1;
                x1 = x2;
                x2 = tempX;

                float tempZ = z1;
                z1 = z2;
                z2 = tempZ;

                Point2f tempT = t1;
                t1 = t2;
                t2 = tempT;
            }

            for (int x = x1; x <= x2; x++) {
                float z = interpolate(x, x1, x2, z1, z2);
                if (z < zBuffer[x][y]) {
                    Point2f texCoord = interpolateTexCoord(x, x1, x2, t1, t2);
                    Color color = sampleTexture(texture, texCoord.x, texCoord.y);
                    graphicsContext.getPixelWriter().setColor(x, y, color);
                    zBuffer[x][y] = z;
                }
            }
        }
    }

    private static void sortVerticesByY(int[] arrX, int[] arrY, float[] arrZ, Point2f[] texCoords) {
        for (int i = 0; i < arrY.length - 1; i++) {
            for (int j = 0; j < arrY.length - 1 - i; j++) {
                if (arrY[j] > arrY[j + 1]) {
                    // Меняем местами Y
                    int tempY = arrY[j];
                    arrY[j] = arrY[j + 1];
                    arrY[j + 1] = tempY;

                    // Меняем местами X
                    int tempX = arrX[j];
                    arrX[j] = arrX[j + 1];
                    arrX[j + 1] = tempX;

                    // Меняем местами Z
                    float tempZ = arrZ[j];
                    arrZ[j] = arrZ[j + 1];
                    arrZ[j + 1] = tempZ;

                    // Меняем местами текстурные координаты
                    Point2f tempTex = texCoords[j];
                    texCoords[j] = texCoords[j + 1];
                    texCoords[j + 1] = tempTex;
                }
            }
        }
    }

    private static float interpolate(int y, int y1, int y2, float value1, float value2) {
        if (y1 == y2) return value1; // Защита от деления на 0
        return value1 + (value2 - value1) * (y - y1) / (float) (y2 - y1);
    }

    private static Point2f interpolateTexCoord(int y, int y1, int y2, Point2f coord1, Point2f coord2) {
        if (y1 == y2) return new Point2f(coord1.x, coord1.y); // Защита от деления на 0
        float t = (y - y1) / (float) (y2 - y1);
        float x = coord1.x + t * (coord2.x - coord1.x);
        float yCoord = coord1.y + t * (coord2.y - coord1.y);
        return new Point2f(x, yCoord);
    }

    private static Color sampleTexture(Image texture, float u, float v) {
        int texWidth = (int) texture.getWidth();
        int texHeight = (int) texture.getHeight();

        // Преобразуем текстурные координаты [0, 1] в пиксельные координаты текстуры
        int texX = Math.min((int) (u * texWidth), texWidth - 1);
        int texY = Math.min((int) ((1 - v) * texHeight), texHeight - 1); // Инверсия Y для соответствия системе координат

        PixelReader pixelReader = texture.getPixelReader();
        if (pixelReader != null) {
            return pixelReader.getColor(texX, texY);
        }

        return Color.BLACK; // Возвращаем черный цвет, если текстура недоступна
    }
}