package com.cgvsu.rasterization;

import com.cgvsu.math.Point2f;

public class Rasterization {

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