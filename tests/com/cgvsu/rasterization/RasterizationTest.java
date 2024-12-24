package com.cgvsu.rasterization;

import com.cgvsu.HelloApplication;
import com.cgvsu.Main;
import com.cgvsu.math.Point2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.render_engine.Camera;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static org.junit.jupiter.api.Assertions.*;

//@ExtendWith(ApplicationExtension.class)
//public class RasterizationTest {
//
//    private GraphicsContext graphicsContext;
//    private Camera camera;
//    private Model mesh;
//    private int width;
//    private int height;
//
//    @BeforeEach
//    public void setUp(FxRobot robot) {
//        // Инициализация графического контекста
//        Stage stage = (Stage) robot.lookup("#stage");
//        Scene scene = stage.getScene();
//        Pane pane = (Pane) scene.getRoot();
//        Canvas canvas = new Canvas(800, 600);
//        pane.getChildren().add(canvas);
//        graphicsContext = canvas.getGraphicsContext2D();
//
//        // Инициализация камеры и модели
//        camera = new Camera(new Vector3f(0, 0, 100), new Vector3f(0, 0, 0), 1.0f, 1, 0.01f, 100);
//        mesh = new Model(); // Загрузите вашу модель здесь
//
//        width = 800;
//        height = 600;
//    }
//
//    @Test
//    public void testFillTriangle() {
//        int[] arrX = {100, 200, 150};
//        int[] arrY = {100, 100, 200};
//        float[] arrZ = {0.1f, 0.2f, 0.3f};
//        float[] lightIntensities = {1.0f, 1.0f, 1.0f};
//
//        Rasterization.initializeZBuffer(width, height);
//        Rasterization.fillTriangle(graphicsContext, arrX, arrY, arrZ, lightIntensities, Color.BLUE, false);
//
//        // Проверка, что треугольник нарисован
//        PixelReader pixelReader = graphicsContext.getCanvas().snapshot(null, null).getPixelReader();
//        Color pixelColor = pixelReader.getColor(150, 150);
//        assertEquals(Color.BLUE, pixelColor);
//    }
//
//    @Test
//    public void testFillTriangleWithTexture() {
//        int[] arrX = {100, 200, 150};
//        int[] arrY = {100, 100, 200};
//        float[] arrZ = {0.1f, 0.2f, 0.3f};
//        Point2f[] texCoords = {new Point2f(0, 0), new Point2f(1, 0), new Point2f(0, 1)};
//        float[] lightIntensities = {1.0f, 1.0f, 1.0f};
//        Image texture = new Image("path/to/texture.png"); // Загрузите вашу текстуру здесь
//
//        Rasterization.initializeZBuffer(width, height);
//        Rasterization.fillTriangleWithTexture(graphicsContext, arrX, arrY, arrZ, texCoords, texture, lightIntensities, false);
//
//        // Проверка, что треугольник нарисован с текстурой
//        PixelReader pixelReader = graphicsContext.getCanvas().snapshot(null, null).getPixelReader();
//        Color pixelColor = pixelReader.getColor(150, 150);
//        assertNotEquals(Color.BLUE, pixelColor); // Проверка, что цвет не синий
//    }
//
//    @Test
//    public void testTexturedTriangleRenderer() {
//        int[] arrX = {100, 200, 150};
//        int[] arrY = {100, 100, 200};
//        float[] arrZ = {0.1f, 0.2f, 0.3f};
//        Point2f[] texCoords = {new Point2f(0, 0), new Point2f(1, 0), new Point2f(0, 1)};
//        float[] lightIntensities = {1.0f, 1.0f, 1.0f};
//        Image texture = new Image("path/to/texture.png"); // Загрузите вашу текстуру здесь
//
//        TriangleRenderer triangleRenderer = new TexturedTriangleRenderer(texture);
//        Rasterization.initializeZBuffer(width, height);
//        triangleRenderer.render(graphicsContext, arrX, arrY, arrZ, texCoords, lightIntensities, false);
//
//        // Проверка, что треугольник нарисован с текстурой
//        PixelReader pixelReader = graphicsContext.getCanvas().snapshot(null, null).getPixelReader();
//        Color pixelColor = pixelReader.getColor(150, 150);
//        assertNotEquals(Color.BLUE, pixelColor); // Проверка, что цвет не синий
//    }
//
//    @Test
//    public void testNonTexturedTriangleRenderer() {
//        int[] arrX = {100, 200, 150};
//        int[] arrY = {100, 100, 200};
//        float[] arrZ = {0.1f, 0.2f, 0.3f};
//        float[] lightIntensities = {1.0f, 1.0f, 1.0f};
//
//        TriangleRenderer triangleRenderer = new NonTexturedTriangleRenderer(Color.BLUE);
//        Rasterization.initializeZBuffer(width, height);
//        triangleRenderer.render(graphicsContext, arrX, arrY, arrZ, new Point2f[3], lightIntensities, false);
//
//        // Проверка, что треугольник нарисован
//        PixelReader pixelReader = graphicsContext.getCanvas().snapshot(null, null).getPixelReader();
//        Color pixelColor = pixelReader.getColor(150, 150);
//        assertEquals(Color.BLUE, pixelColor);
//    }
//}