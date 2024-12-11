package com.cgvsu.rasterization;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

//public class RasterizationTest {
//
//    private GraphicsContext graphicsContext;
//    private PixelWriter pixelWriter;
//
//    @BeforeEach
//    public void setup() {
//        // Создаем mock-объекты для GraphicsContext и PixelWriter
//        graphicsContext = mock(GraphicsContext.class);
//        pixelWriter = mock(PixelWriter.class);
//
//        Canvas canvas = mock(Canvas.class);
//        when(canvas.getWidth()).thenReturn(100.0); // Устанавливаем размер холста
//        when(canvas.getHeight()).thenReturn(100.0);
//
//        when(graphicsContext.getCanvas()).thenReturn(canvas);
//        when(graphicsContext.getPixelWriter()).thenReturn(pixelWriter);
//    }
//
//    @Test
//    public void testFillTriangleSimpleCase() {
//        // Входные данные
//        int[] x = {10, 50, 30};
//        int[] y = {10, 10, 40};
//        Color[] colors = {Color.RED, Color.GREEN, Color.BLUE};
//        double[] z = {0.5, 0.2, 0.8};
//
//        // Вызов тестируемого метода
//        Rasterization.fillTriangle(graphicsContext, x, y, colors, z);
//
//        // Проверяем, что пиксели устанавливаются в пределах холста
//        verify(pixelWriter, atLeastOnce()).setColor(anyInt(), anyInt(), any(Color.class));
//
//        // Пример проверки конкретного пикселя
//        verify(pixelWriter, atLeastOnce()).setColor(eq(30), eq(25), any(Color.class));
//    }
//
//    @Test
//    public void testFillTriangleOutOfBounds() {
//        // Входные данные (треугольник вне области рисования)
//        int[] x = {-10, -50, -30};
//        int[] y = {-10, -10, -40};
//        Color[] colors = {Color.RED, Color.GREEN, Color.BLUE};
//        double[] z = {0.5, 0.2, 0.8};
//
//        // Вызов тестируемого метода
//        Rasterization.fillTriangle(graphicsContext, x, y, colors, z);
//
//        // Проверяем, что PixelWriter не вызывается (пиксели вне границ)
//        verify(pixelWriter, never()).setColor(anyInt(), anyInt(), any(Color.class));
//    }
//
//    @Test
//    public void testFillTriangleColorInterpolation() {
//        // Входные данные для проверки интерполяции цвета
//        int[] x = {10, 50, 30};
//        int[] y = {10, 10, 40};
//        Color[] colors = {Color.RED, Color.GREEN, Color.BLUE};
//        double[] z = {0.5, 0.2, 0.8};
//
//        // Вызов тестируемого метода
//        Rasterization.fillTriangle(graphicsContext, x, y, colors, z);
//
//        // Пример проверки интерполяции цвета (проверяем вызов setColor с интерполяцией)
//        verify(pixelWriter, atLeastOnce()).setColor(
//                anyInt(),
//                anyInt(),
//                argThat(color -> color.getRed() >= 0 && color.getRed() <= 1 &&
//                        color.getGreen() >= 0 && color.getGreen() <= 1 &&
//                        color.getBlue() >= 0 && color.getBlue() <= 1)
//        );
//    }
//
//    @Test
//    public void testFillTriangleZBuffer() {
//        // Входные данные
//        int[] x = {10, 50, 30};
//        int[] y = {10, 10, 40};
//        Color[] colors = {Color.RED, Color.GREEN, Color.BLUE};
//        double[] z1 = {0.5, 0.2, 0.8};
//        double[] z2 = {0.1, 0.1, 0.1}; // Второй треугольник ближе
//
//        // Рисуем первый треугольник
//        Rasterization.fillTriangle(graphicsContext, x, y, colors, z1);
//
//        // Проверяем вызов для первого треугольника
//        verify(pixelWriter, atLeastOnce()).setColor(anyInt(), anyInt(), any(Color.class));
//
//        // Сбрасываем mock для PixelWriter
//        Mockito.reset(pixelWriter);
//
//        // Рисуем второй треугольник
//        Rasterization.fillTriangle(graphicsContext, x, y, colors, z2);
//
//        // Проверяем, что второй треугольник "перекрыл" первый
//        verify(pixelWriter, atLeastOnce()).setColor(anyInt(), anyInt(), any(Color.class));
//    }
//}
