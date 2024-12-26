package com.cgvsu.rasterization;

import com.cgvsu.math.Point2f;
import com.cgvsu.math.Vector3f;

public class Rasterization {

    public static void sortVerticesByY(int[] arrX, int[] arrY, float[] arrZ, float[] lightIntensities, Point2f[] texCoords) {
        quickSort(arrX, arrY, arrZ, lightIntensities, texCoords, 0, arrY.length - 1);
    }

    private static void quickSort(int[] arrX, int[] arrY, float[] arrZ, float[] lightIntensities, Point2f[] texCoords, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(arrX, arrY, arrZ, lightIntensities, texCoords, low, high);

            quickSort(arrX, arrY, arrZ, lightIntensities, texCoords, low, pivotIndex - 1);
            quickSort(arrX, arrY, arrZ, lightIntensities, texCoords, pivotIndex + 1, high);
        }
    }

    private static int partition(int[] arrX, int[] arrY, float[] arrZ, float[] lightIntensities, Point2f[] texCoords, int low, int high) {
        int pivot = arrY[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arrY[j] <= pivot) {
                i++;
                swap(arrX, i, j);
                swap(arrY, i, j);
                swap(arrZ, i, j);
                swap(lightIntensities, i, j);
                swap(texCoords, i, j);
            }
        }

        swap(arrX, i + 1, high);
        swap(arrY, i + 1, high);
        swap(arrZ, i + 1, high);
        swap(lightIntensities, i + 1, high);
        swap(texCoords, i + 1, high);

        return i + 1;
    }

    static void swap(Point2f[] array, int i, int j) {
        Point2f temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    static void swap(float[] array, int i, int j) {
        float temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    static void sortVerticesByY(int[] arrX, int[] arrY, float[]... arrays) {
        quickSort(arrX, arrY, 0, arrY.length - 1, arrays);
    }

    private static void quickSort(int[] arrX, int[] arrY, int low, int high, float[]... arrays) {
        if (low < high) {
            int pivotIndex = partition(arrX, arrY, low, high, arrays);

            quickSort(arrX, arrY, low, pivotIndex - 1, arrays);
            quickSort(arrX, arrY, pivotIndex + 1, high, arrays);
        }
    }

    private static int partition(int[] arrX, int[] arrY, int low, int high, float[]... arrays) {
        int pivot = arrY[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arrY[j] <= pivot) {
                i++;
                swap(arrX, i, j);
                swap(arrY, i, j);

                for (float[] array : arrays) {
                    swap(array, i, j);
                }
            }
        }

        swap(arrX, i + 1, high);
        swap(arrY, i + 1, high);

        for (float[] array : arrays) {
            swap(array, i + 1, high);
        }

        return i + 1;
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