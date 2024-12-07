package com.cgvsu.rasterization;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.util.Arrays;

public class Rasterization {
    public static void fillTriangle(
            final GraphicsContext graphicsContext,
            final int[] arrX,
            final int[] arrY,
            final Color[] colors,
            final double[] arrZ) {

        final PixelWriter pixelWriter = graphicsContext.getPixelWriter();
        final int width = (int) graphicsContext.getCanvas().getWidth();
        final int height = (int) graphicsContext.getCanvas().getHeight();

        // Инициализация Z-буфера
        double[][] zBuffer = new double[width][height];
        for (int i = 0; i < width; i++) {
            Arrays.fill(zBuffer[i], Double.POSITIVE_INFINITY);
        }

        // Сортируем вершины по Y
        sort(arrX, arrY, colors, arrZ);

        // Растеризация двух частей треугольника
        rasterizeHalf(arrX, arrY, colors, arrZ, zBuffer, pixelWriter, width, height, true); // Нижняя часть
        rasterizeHalf(arrX, arrY, colors, arrZ, zBuffer, pixelWriter, width, height, false); // Верхняя часть
    }

    private static void rasterizeHalf(
            int[] arrX, int[] arrY, Color[] colors, double[] arrZ,
            double[][] zBuffer, PixelWriter pixelWriter,
            int width, int height, boolean isLower) {

        int yStart = isLower ? arrY[1] : arrY[0];
        int yEnd = isLower ? arrY[2] : arrY[1];
        if (!isLower) yStart--; // Верхняя часть рисуется "вниз"

        for (int y = yStart; y <= yEnd; y++) {
            if (y < 0 || y >= height) continue;

            int x1 = interpolateX(y, arrY[isLower ? 1 : 0], arrY[2], arrX[isLower ? 1 : 0], arrX[2]);
            int x2 = interpolateX(y, arrY[0], arrY[2], arrX[0], arrX[2]);

            if (x1 > x2) {
                int temp = x1;
                x1 = x2;
                x2 = temp;
            }

            for (int x = Math.max(0, x1); x <= Math.min(width - 1, x2); x++) {
                double[] baryCoords = calculateBarycentricCoords(x, y, arrX, arrY);
                double z = baryCoords[0] * arrZ[0] + baryCoords[1] * arrZ[1] + baryCoords[2] * arrZ[2];

                if (z < zBuffer[x][y]) {
                    zBuffer[x][y] = z;
                    pixelWriter.setColor(x, y, interpolateColor(baryCoords, colors));
                }
            }
        }
    }

    private static int interpolateX(int y, int y1, int y2, int x1, int x2) {
        return y2 == y1 ? x1 : (y - y1) * (x2 - x1) / (y2 - y1) + x1;
    }

    private static double[] calculateBarycentricCoords(int x, int y, int[] arrX, int[] arrY) {
        double denom = (arrY[1] - arrY[2]) * (arrX[0] - arrX[2]) + (arrX[2] - arrX[1]) * (arrY[0] - arrY[2]);
        double b0 = ((arrY[1] - arrY[2]) * (x - arrX[2]) + (arrX[2] - arrX[1]) * (y - arrY[2])) / denom;
        double b1 = ((arrY[2] - arrY[0]) * (x - arrX[2]) + (arrX[0] - arrX[2]) * (y - arrY[2])) / denom;
        double b2 = 1 - b0 - b1;
        return new double[]{b0, b1, b2};
    }

    private static Color interpolateColor(double[] baryCoords, Color[] colors) {
        double r = baryCoords[0] * colors[0].getRed() +
                   baryCoords[1] * colors[1].getRed() +
                   baryCoords[2] * colors[2].getRed();
        double g = baryCoords[0] * colors[0].getGreen() +
                   baryCoords[1] * colors[1].getGreen() +
                   baryCoords[2] * colors[2].getGreen();
        double b = baryCoords[0] * colors[0].getBlue() +
                   baryCoords[1] * colors[1].getBlue() +
                   baryCoords[2] * colors[2].getBlue();
        return new Color(
                Math.min(1.0, Math.max(0.0, r)),
                Math.min(1.0, Math.max(0.0, g)),
                Math.min(1.0, Math.max(0.0, b)),
                1.0);
    }

    private static void sort(int[] x, int[] y, Color[] c, double[] z) {
        if (y[0] > y[1]) swap(x, y, c, z, 0, 1);
        if (y[1] > y[2]) swap(x, y, c, z, 1, 2);
        if (y[0] > y[1]) swap(x, y, c, z, 0, 1);
    }

    private static void swap(int[] x, int[] y, Color[] c, double[] z, int i, int j) {
        int tempX = x[i], tempY = y[i];
        Color tempC = c[i];
        double tempZ = z[i];
        x[i] = x[j];
        y[i] = y[j];
        c[i] = c[j];
        z[i] = z[j];
        x[j] = tempX;
        y[j] = tempY;
        c[j] = tempC;
        z[j] = tempZ;
    }
}