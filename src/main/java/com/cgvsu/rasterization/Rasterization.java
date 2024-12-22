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

//    public static void fillTriangle(
//            final GraphicsContext graphicsContext,
//            final int[] arrX,
//            final int[] arrY,
//            final float[] arrZ,
//            final float[] lightIntensities,
//            final Color baseColor,
//            final boolean useLighting) {
//
//        TriangleRenderer triangleRenderer = new NonTexturedTriangleRenderer(baseColor, zBuffer);
//        triangleRenderer.render(graphicsContext, arrX, arrY, arrZ, new Point2f[3], lightIntensities, useLighting);
//    }
//
//    public static void fillTriangleWithTexture(
//            final GraphicsContext graphicsContext,
//            final int[] arrX,
//            final int[] arrY,
//            final float[] arrZ,
//            final Point2f[] texCoords,
//            final Image texture,
//            final float[] lightIntensities,
//            final boolean useLighting) {
//
//        TriangleRenderer triangleRenderer = new TexturedTriangleRenderer(texture, zBuffer);
//        triangleRenderer.render(graphicsContext, arrX, arrY, arrZ, texCoords, lightIntensities, useLighting);
//    }

    public static void sortVerticesByY(int[] arrX, int[] arrY, float[] arrZ, float[] lightIntensities, Point2f[] texCoords) {
        for (int i = 0; i < arrY.length - 1; i++) {
            for (int j = 0; j < arrY.length - i - 1; j++) {
                if (arrY[j] > arrY[j + 1]) {
                    swap(arrX, j, j + 1);
                    swap(arrY, j, j + 1);
                    swap(arrZ, j, j + 1);
                    swap(lightIntensities, j, j + 1);
                    swap(texCoords, j, j + 1);
                }
            }
        }
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

 private static <T> void swap(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
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

    static int interpolateX(int y, int y1, int y2, int x1, int x2) {
        if (y1 == y2) return x1;
        return x1 + (x2 - x1) * (y - y1) / (y2 - y1);
    }

    static float interpolate(float value, float start, float end, float valStart, float valEnd) {
        if (start == end) return valStart;
        return valStart + (valEnd - valStart) * (value - start) / (end - start);
    }
}