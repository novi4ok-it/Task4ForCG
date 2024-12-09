package com.cgvsu.render_engine;

import java.util.ArrayList;

import com.cgvsu.math.Vector3f;
import com.cgvsu.math.Vector4f;
import javafx.scene.canvas.GraphicsContext;
import com.cgvsu.model.Model;

import com.cgvsu.math.Matrix4f;

import com.cgvsu.math.Point2f;

import static com.cgvsu.render_engine.GraphicConveyor.*;

public class RenderEngine {

    public static void render(
            final GraphicsContext graphicsContext,
            final Camera camera,
            final Model mesh,
            final int width,
            final int height) {
        // Получаем матрицы модели, вида и проекции
        Matrix4f modelMatrix = rotateScaleTranslate();
        Matrix4f viewMatrix = camera.getViewMatrix();
        Matrix4f projectionMatrix = camera.getProjectionMatrix();

        // Умножаем матрицы в порядке Model -> View -> Projection
        Matrix4f modelViewProjectionMatrix = modelMatrix
                .multiply(viewMatrix)
                .multiply(projectionMatrix);

        final int nPolygons = mesh.polygons.size();
        for (int polygonInd = 0; polygonInd < nPolygons; ++polygonInd) {
            final int nVerticesInPolygon = mesh.polygons.get(polygonInd).getVertexIndices().size();

            ArrayList<Point2f> resultPoints = new ArrayList<>();
            for (int vertexInPolygonInd = 0; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                Vector3f vertex = mesh.vertices.get(mesh.polygons.get(polygonInd).getVertexIndices().get(vertexInPolygonInd));

                // Преобразуем Vector3f в Vector4f для умножения
                Vector4f vertexVecmath = new Vector4f(vertex.x, vertex.y, vertex.z, 1);

                // Преобразуем вершину через MVP-матрицу
                Vector4f transformedVertex = modelViewProjectionMatrix.multiplyvec(vertexVecmath);

                // Преобразуем вектор обратно в 2D-точку для отрисовки
                Point2f resultPoint = vertexToPoint(transformedVertex, width, height);
                resultPoints.add(resultPoint);
            }

            // Заполнение полигона (если у него больше 2-х вершин)
            if (nVerticesInPolygon > 2) {
                double[] xPoints = new double[nVerticesInPolygon];
                double[] yPoints = new double[nVerticesInPolygon];
                for (int i = 0; i < nVerticesInPolygon; ++i) {
                    xPoints[i] = resultPoints.get(i).getX();
                    yPoints[i] = resultPoints.get(i).getY();
                }

                // Задаем цвет заливки
                graphicsContext.setFill(javafx.scene.paint.Color.LIGHTGRAY); // Цвет можно изменить по желанию
                graphicsContext.fillPolygon(xPoints, yPoints, nVerticesInPolygon);
            }

            // Отрисовка граней полигона
            graphicsContext.setStroke(javafx.scene.paint.Color.BLACK); // Цвет линий
            for (int vertexInPolygonInd = 1; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                graphicsContext.strokeLine(
                        resultPoints.get(vertexInPolygonInd - 1).getX(),
                        resultPoints.get(vertexInPolygonInd - 1).getY(),
                        resultPoints.get(vertexInPolygonInd).getX(),
                        resultPoints.get(vertexInPolygonInd).getY());
            }

            // Замыкаем контур (линия от последней вершины к первой)
            if (nVerticesInPolygon > 0) {
                graphicsContext.strokeLine(
                        resultPoints.get(nVerticesInPolygon - 1).getX(),
                        resultPoints.get(nVerticesInPolygon - 1).getY(),
                        resultPoints.get(0).getX(),
                        resultPoints.get(0).getY());
            }
        }
    }

    private static Matrix4f rotateScaleTranslate() {
        float scale = 1.0f; // Масштабирование
        float tx = 0.0f, ty = 0.0f, tz = 0.0f; // Сдвиг по осям
        return new Matrix4f(new float[]{
                scale, 0.0f, 0.0f, tx, // Первая строка
                0.0f, scale, 0.0f, ty, // Вторая строка
                0.0f, 0.0f, scale, tz, // Третья строка
                0.0f, 0.0f, 0.0f, 1.0f  // Четвёртая строка
        });
    }

    private static Point2f vertexToPoint(Vector4f vertex, int width, int height) {
        // Проверяем, что W не равно 0
        if (Math.abs(vertex.getW()) < 1e-6) {
            throw new IllegalArgumentException("Invalid W component for vertex transformation.");
        }

        // Нормализуем координаты
        float normalizedX = vertex.getX() / vertex.getW();
        float normalizedY = vertex.getY() / vertex.getW();

        // Преобразуем к экранным координатам
        float screenX = (normalizedX + 1) * 0.5f * width;
        float screenY = (1 - normalizedY) * 0.5f * height;

        return new Point2f(screenX, screenY);
    }
}