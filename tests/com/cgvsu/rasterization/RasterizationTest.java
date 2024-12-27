//package com.cgvsu.rasterization;
//
//import com.cgvsu.math.Point2f;
//import org.junit.jupiter.api.Test;
//
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class RasterizationTest {
//
//    @Test
//    void testSortVerticesByY() {
//        int[] arrX = {10, 20, 30};
//        int[] arrY = {15, 10, 25};
//        float[] arrZ = {0.5f, 0.2f, 0.8f};
//        float[] lightIntensities = {1.0f, 0.8f, 0.6f};
//        Point2f[] texCoords = {
//                new Point2f(0.0f, 0.0f),
//                new Point2f(1.0f, 1.0f),
//                new Point2f(0.5f, 0.5f)
//        };
//
//        Rasterization.sortVerticesByY(arrX, arrY, arrZ, lightIntensities, texCoords);
//
//        // Проверяем, что вершины отсортированы по Y
//        assertArrayEquals(new int[]{20, 10, 30}, arrX);
//        assertArrayEquals(new int[]{10, 15, 25}, arrY);
//        assertArrayEquals(new float[]{0.2f, 0.5f, 0.8f}, arrZ);
//        assertArrayEquals(new float[]{0.8f, 1.0f, 0.6f}, lightIntensities);
//        assertEquals(new Point2f(1.0f, 1.0f), texCoords[0]);
//        assertEquals(new Point2f(0.0f, 0.0f), texCoords[1]);
//        assertEquals(new Point2f(0.5f, 0.5f), texCoords[2]);
//    }
//
//    @Test
//    void testSortVerticesByYWithFloatArrays() {
//        int[] arrX = {50, 30, 10};
//        int[] arrY = {30, 10, 20};
//        float[] arrZ = {0.9f, 0.4f, 0.7f};
//        float[] lightIntensities = {0.3f, 0.9f, 0.5f};
//
//        Rasterization.sortVerticesByY(arrX, arrY, arrZ, lightIntensities);
//
//        // Проверяем, что вершины отсортированы по Y
//        assertArrayEquals(new int[]{30, 10, 50}, arrX);
//        assertArrayEquals(new int[]{10, 20, 30}, arrY);
//        assertArrayEquals(new float[]{0.4f, 0.7f, 0.9f}, arrZ);
//        assertArrayEquals(new float[]{0.9f, 0.5f, 0.3f}, lightIntensities);
//    }
//
//    @Test
//    void testInterpolateX() {
//        int result = Rasterization.interpolateX(15, 10, 20, 5, 25);
//        assertEquals(15, result);
//
//        result = Rasterization.interpolateX(10, 10, 10, 5, 25);
//        assertEquals(5, result); // Проверка случая деления на 0
//    }
//
//    @Test
//    void testInterpolate() {
//        float result = Rasterization.interpolate(7.5f, 5.0f, 10.0f, 2.0f, 8.0f);
//        assertEquals(5.0f, result, 0.001);
//
//        result = Rasterization.interpolate(5.0f, 5.0f, 5.0f, 2.0f, 8.0f);
//        assertEquals(2.0f, result); // Проверка случая деления на 0
//    }
//
//    @Test
//    void testSwapIntArray() {
//        int[] array = {1, 2, 3};
//        Rasterization.swap(array, 0, 2);
//        assertArrayEquals(new int[]{3, 2, 1}, array);
//    }
//
//    @Test
//    void testSwapFloatArray() {
//        float[] array = {0.1f, 0.2f, 0.3f};
//        Rasterization.swap(array, 0, 2);
//        assertArrayEquals(new float[]{0.3f, 0.2f, 0.1f}, array);
//    }
//
//    @Test
//    void testSwapPoint2fArray() {
//        Point2f[] array = {
//                new Point2f(1.0f, 1.0f),
//                new Point2f(2.0f, 2.0f),
//                new Point2f(3.0f, 3.0f)
//        };
//        Rasterization.swap(array, 0, 2);
//        assertEquals(new Point2f(3.0f, 3.0f), array[0]);
//        assertEquals(new Point2f(1.0f, 1.0f), array[2]);
//    }
//}