package com.cgvsu.rasterization;

import com.cgvsu.math.Point2f;
import com.cgvsu.math.Vector3f;
import javafx.scene.paint.Color;

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

    public static void sortVerticesByY(int[] arrX, int[] arrY, float[] arrZ, Color[] vertexColors) {
        quickSort(arrX, arrY, arrZ, vertexColors, 0, arrY.length - 1);
    }

    private static void quickSort(int[] arrX, int[] arrY, float[] arrZ, Color[] vertexColors, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(arrX, arrY, arrZ, vertexColors, low, high);

            quickSort(arrX, arrY, arrZ, vertexColors, low, pivotIndex - 1);
            quickSort(arrX, arrY, arrZ, vertexColors, pivotIndex + 1, high);
        }
    }

    private static int partition(int[] arrX, int[] arrY, float[] arrZ, Color[] vertexColors, int low, int high) {
        int pivot = arrY[high]; // Выбираем опорный элемент
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arrY[j] <= pivot) {
                i++;
                swap(arrX, arrY, arrZ, vertexColors, i, j);
            }
        }

        // Перемещаем опорный элемент на его правильное место
        swap(arrX, arrY, arrZ, vertexColors, i + 1, high);
        return i + 1;
    }

    private static void swap(int[] arrX, int[] arrY, float[] arrZ, Color[] vertexColors, int i, int j) {
        // Меняем местами координаты X
        int tempX = arrX[i];
        arrX[i] = arrX[j];
        arrX[j] = tempX;

        // Меняем местами координаты Y
        int tempY = arrY[i];
        arrY[i] = arrY[j];
        arrY[j] = tempY;

        // Меняем местами координаты Z
        float tempZ = arrZ[i];
        arrZ[i] = arrZ[j];
        arrZ[j] = tempZ;

        // Меняем местами цвета
        Color tempColor = vertexColors[i];
        vertexColors[i] = vertexColors[j];
        vertexColors[j] = tempColor;
    }

    public static void sortVerticesByY(
            int[] arrX,
            int[] arrY,
            float[] arrZ,
            Color[] vertexColors,
            Point2f[] texCoords) {

        // Вызов рекурсивной функции быстрой сортировки
        quickSort(arrX, arrY, arrZ, vertexColors, texCoords, 0, arrY.length - 1);
    }

    private static void quickSort(
            int[] arrX,
            int[] arrY,
            float[] arrZ,
            Color[] vertexColors,
            Point2f[] texCoords,
            int low,
            int high) {

        if (low < high) {
            int pivotIndex = partition(arrX, arrY, arrZ, vertexColors, texCoords, low, high);

            // Рекурсивно сортируем левую и правую части
            quickSort(arrX, arrY, arrZ, vertexColors, texCoords, low, pivotIndex - 1);
            quickSort(arrX, arrY, arrZ, vertexColors, texCoords, pivotIndex + 1, high);
        }
    }

    private static int partition(
            int[] arrX,
            int[] arrY,
            float[] arrZ,
            Color[] vertexColors,
            Point2f[] texCoords,
            int low,
            int high) {

        int pivotY = arrY[high]; // Берем последний элемент в качестве опорного
        int i = low - 1; // Индекс меньшего элемента

        for (int j = low; j < high; j++) {
            if (arrY[j] <= pivotY) {
                i++;
                // Меняем местами элементы
                swap(arrX, arrY, arrZ, vertexColors, texCoords, i, j);
            }
        }

        // Помещаем опорный элемент в правильное место
        swap(arrX, arrY, arrZ, vertexColors, texCoords, i + 1, high);
        return i + 1;
    }

    private static void swap(
            int[] arrX,
            int[] arrY,
            float[] arrZ,
            Color[] vertexColors,
            Point2f[] texCoords,
            int index1,
            int index2) {

        // Меняем местами координаты X
        int tempX = arrX[index1];
        arrX[index1] = arrX[index2];
        arrX[index2] = tempX;

        // Меняем местами координаты Y
        int tempY = arrY[index1];
        arrY[index1] = arrY[index2];
        arrY[index2] = tempY;

        // Меняем местами координаты Z
        float tempZ = arrZ[index1];
        arrZ[index1] = arrZ[index2];
        arrZ[index2] = tempZ;

        // Меняем местами цвета
        Color tempColor = vertexColors[index1];
        vertexColors[index1] = vertexColors[index2];
        vertexColors[index2] = tempColor;

        // Меняем местами текстурные координаты
        Point2f tempTexCoord = texCoords[index1];
        texCoords[index1] = texCoords[index2];
        texCoords[index2] = tempTexCoord;
    }

    static int interpolateX(int y, int y1, int y2, int x1, int x2) {
        if (y1 == y2) return x1;
        return x1 + (x2 - x1) * (y - y1) / (y2 - y1);
    }

    static float interpolate(float value, float start, float end, float valStart, float valEnd) {
        if (start == end) return valStart;
        return valStart + (valEnd - valStart) * (value - start) / (end - start);
    }

    public static Color interpolateColor(float value, float start, float end, Color color1, Color color2) {
        if (color1 == null && color2 == null) {
            return null; // Если оба цвета null, возвращаем null
        }
        if (color1 == null) {
            return color2; // Если первый цвет null, используем второй
        }
        if (color2 == null) {
            return color1; // Если второй цвет null, используем первый
        }

        if (start == end) {
            return color1; // Если начальная и конечная точки совпадают, возвращаем первый цвет
        }

        double t = (value - start) / (end - start);
        t = Math.min(1.0, Math.max(0.0, t)); // Ограничиваем t в диапазоне [0, 1]

        double r = color1.getRed() + t * (color2.getRed() - color1.getRed());
        double g = color1.getGreen() + t * (color2.getGreen() - color1.getGreen());
        double b = color1.getBlue() + t * (color2.getBlue() - color1.getBlue());
        double opacity = color1.getOpacity() + t * (color2.getOpacity() - color1.getOpacity());

        return new Color(r, g, b, opacity);
    }
}