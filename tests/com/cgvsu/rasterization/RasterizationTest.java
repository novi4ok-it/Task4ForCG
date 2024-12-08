package com.cgvsu.rasterization;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;


public class RasterizationTest {

    @BeforeAll
    static void setup() {
        TestApp.initToolkit(); // Инициализация JavaFX
    }

    @Test
    void testBarycentricCoordinates() {
        Vector3f a = new Vector3f(0, 0, 0);
        Vector3f b = new Vector3f(1, 0, 0);
        Vector3f c = new Vector3f(0, 1, 0);

        Vector3f pointInside = new Vector3f(0.25f, 0.25f, 0);
        Vector3f pointOutside = new Vector3f(1.5f, 1.5f, 0);

        double[] baryCoordsInside = Rasterization.calculateBarycentricCoords(pointInside, new Vector3f[]{a, b, c});
        double[] baryCoordsOutside = Rasterization.calculateBarycentricCoords(pointOutside, new Vector3f[]{a, b, c});

        // Проверяем сумму координат для точки внутри
        assertEquals(1.0, baryCoordsInside[0] + baryCoordsInside[1] + baryCoordsInside[2], 1e-6);

        // Проверяем, что координаты для точки вне треугольника некорректны
        assertTrue(baryCoordsOutside[0] < 0 || baryCoordsOutside[1] < 0 || baryCoordsOutside[2] < 0);
    }

    @Test
    void testInterpolation() {
        Vector3f start = new Vector3f(0, 0, 0);
        Vector3f end = new Vector3f(10, 10, 10);

        Vector3f midPoint = Rasterization.interpolate(start, end, 0.5f);

        assertEquals(5, midPoint.x, 1e-6);
        assertEquals(5, midPoint.y, 1e-6);
        assertEquals(5, midPoint.z, 1e-6);
    }

    @Test
    void testFillTriangle() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                Canvas canvas = new Canvas(100, 100);
                GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

                Vector3f[] vertices = {
                    new Vector3f(10, 10, 0),
                    new Vector3f(50, 10, 0),
                    new Vector3f(30, 50, 0)
                };

                Rasterization.fillTriangle(graphicsContext, vertices, Color.RED);

                // Проверка, что пиксели внутри треугольника окрашены
                PixelReader pixelReader = canvas.snapshot(null, null).getPixelReader();
                assertEquals(Color.RED, pixelReader.getColor(30, 30));
                assertEquals(Color.TRANSPARENT, pixelReader.getColor(5, 5));
            } finally {
                latch.countDown();
            }
        });

        latch.await();
    }

    @Test
    void testFillTriangleWithTexture() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Color> colorInside = new AtomicReference<>();
        AtomicReference<Color> colorOutside = new AtomicReference<>();

        Platform.runLater(() -> {
            try {
                Canvas canvas = new Canvas(100, 100);
                WritableImage texture = new WritableImage(100, 100);

                // Задаем текстуру
                texture.getPixelWriter().setColor(50, 50, Color.BLUE);

                GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

                Vector3f[] vertices = {
                    new Vector3f(10, 10, 0),
                    new Vector3f(50, 10, 0),
                    new Vector3f(30, 50, 0)
                };

                Vector2f[] texCoords = {
                    new Vector2f(0, 0),
                    new Vector2f(1, 0),
                    new Vector2f(0.5f, 1)
                };

                Rasterization.fillTriangleWithTexture(graphicsContext, vertices, texCoords, texture);

                // Проверяем пиксель внутри треугольника
                PixelReader pixelReader = canvas.snapshot(null, null).getPixelReader();
                colorInside.set(pixelReader.getColor(30, 30));
                colorOutside.set(pixelReader.getColor(5, 5));
            } finally {
                latch.countDown();
            }
        });

        latch.await();

        assertNotNull(colorInside.get());
        assertEquals(Color.BLUE, colorInside.get()); // Проверяем, что цвет соответствует текстуре

        assertNotNull(colorOutside.get());
        assertEquals(Color.TRANSPARENT, colorOutside.get()); // Проверяем, что точка вне треугольника прозрачна
    }
}