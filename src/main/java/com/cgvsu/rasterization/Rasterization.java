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

    public static double[][] zBuffer;

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
            final float[] lightIntensities,
            final Color baseColor,
            final boolean useLighting) {

        TriangleRenderer triangleRenderer = new NonTexturedTriangleRenderer(baseColor, zBuffer);
        triangleRenderer.render(graphicsContext, arrX, arrY, arrZ, new Point2f[3], lightIntensities, useLighting);
    }

    public static void fillTriangleWithTexture(
            final GraphicsContext graphicsContext,
            final int[] arrX,
            final int[] arrY,
            final float[] arrZ,
            final Point2f[] texCoords,
            final Image texture,
            final float[] lightIntensities,
            final boolean useLighting) {

        TriangleRenderer triangleRenderer = new TexturedTriangleRenderer(texture, zBuffer);
        triangleRenderer.render(graphicsContext, arrX, arrY, arrZ, texCoords, lightIntensities, useLighting);
    }

    static void sortVerticesByY(int[] arrX, int[] arrY, float[]... arrays) {
        for (int i = 0; i < arrY.length - 1; i++) {
            for (int j = 0; j < arrY.length - i - 1; j++) {
                if (arrY[j] > arrY[j + 1]) {
                    swap(arrX, j, j + 1);
                    swap(arrY, j, j + 1);
                    for (float[] array : arrays) {
                        swap(array, j, j + 1);
                    }
                }
            }
        }
    }

    static void sortVerticesByY(int[] arrX, int[] arrY, float[] arrZ, Point2f[] texCoords, float[] lightIntensities) {
        for (int i = 0; i < 2; i++) {
            for (int j = i + 1; j < 3; j++) {
                if (arrY[i] > arrY[j]) {
                    swap(arrX, arrY, arrZ, texCoords, lightIntensities, i, j);
                }
            }
        }
    }

    private static void swap(int[] arrX, int[] arrY, float[] arrZ, Point2f[] texCoords, float[] lightIntensities, int i, int j) {
        int tempX = arrX[i];
        arrX[i] = arrX[j];
        arrX[j] = tempX;

        int tempY = arrY[i];
        arrY[i] = arrY[j];
        arrY[j] = tempY;

        float tempZ = arrZ[i];
        arrZ[i] = arrZ[j];
        arrZ[j] = tempZ;

        Point2f tempT = texCoords[i];
        texCoords[i] = texCoords[j];
        texCoords[j] = tempT;

        float tempI = lightIntensities[i];
        lightIntensities[i] = lightIntensities[j];
        lightIntensities[j] = tempI;
    }


    static int interpolateX(int y, int y1, int y2, int x1, int x2) {
        if (y1 == y2) return x1;
        return x1 + (x2 - x1) * (y - y1) / (y2 - y1);
    }

    static float interpolate(float value, float start, float end, float valStart, float valEnd) {
        if (start == end) return valStart;
        return valStart + (valEnd - valStart) * (value - start) / (end - start);
    }

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    private static void swap(float[] array, int i, int j) {
        float temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    // Интерполяция текстурных координат для каждого пикселя
    static Point2f interpolateTexCoord(float x, float x1, float x2, Point2f coord1, Point2f coord2) {
        if (x1 == x2) return new Point2f(coord1.x, coord1.y);
        float t = (x - x1) / (x2 - x1);
        float u = coord1.x + t * (coord2.x - coord1.x);
        float v = coord1.y + t * (coord2.y - coord1.y);

        // Дополнительная проверка, чтобы избежать значений вне диапазона [0, 1]
        u = Math.max(0, Math.min(u, 1));
        v = Math.max(0, Math.min(v, 1));

        return new Point2f(u, v);
    }

    static Color sampleTexture(Image texture, float u, float v) {
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