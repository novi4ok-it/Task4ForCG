package com.cgvsu.render_engine;

import java.util.ArrayList;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Polygon;
import com.cgvsu.rasterization.Rasterization;
import javafx.scene.canvas.GraphicsContext;
import com.cgvsu.model.Model;

import com.cgvsu.math.Matrix4f;
import com.cgvsu.math.Point2f;
import javafx.scene.paint.Color;

import static com.cgvsu.render_engine.GraphicConveyor.*;

public class RenderEngine {

    public static void render(
            final GraphicsContext graphicsContext,
            final Camera camera,
            final Model mesh,
            final int width,
            final int height,
            final boolean isLightingEnabled) {

        // Инициализируем Z-буфер
        Rasterization.initializeZBuffer(width, height);

        Matrix4f modelMatrix = rotateScaleTranslate();
        Matrix4f viewMatrix = camera.getViewMatrix();
        Matrix4f projectionMatrix = camera.getProjectionMatrix();

        Matrix4f modelViewProjectionMatrix = new Matrix4f(modelMatrix);
        modelViewProjectionMatrix.mul(viewMatrix);
        modelViewProjectionMatrix.mul(projectionMatrix);

        final int nPolygons = mesh.polygons.size();
        for (Polygon polygon : mesh.polygons) {
            final int nVerticesInPolygon = polygon.getVertexIndices().size();

            if (nVerticesInPolygon != 3) continue; // Обрабатываем только треугольники

            int[] arrX = new int[3];
            int[] arrY = new int[3];
            float[] arrZ = new float[3];
            Point2f[] texCoords = new Point2f[3];
            float[] lightIntensities = new float[3];

            for (int vertexInd = 0; vertexInd < nVerticesInPolygon; ++vertexInd) {
                int vertexIndex = polygon.getVertexIndices().get(vertexInd);
                Vector3f vertex = mesh.vertices.get(vertexIndex);

                // Преобразуем вершину в пространство экрана
                Vector3f transformedVertex = multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertex);

                // Преобразуем координаты в экранные
                Point2f screenPoint = vertexToPoint(transformedVertex, width, height);

                // Заполняем массивы для растеризации
                arrX[vertexInd] = (int) screenPoint.x;
                arrY[vertexInd] = (int) screenPoint.y;
                arrZ[vertexInd] = transformedVertex.z; // Z-координата

                // Получаем текстурные координаты, если они есть
                if (!polygon.getTextureVertexIndices().isEmpty()) {
                    int texCoordIndex = polygon.getTextureVertexIndices().get(vertexInd);
                    Vector2f texCoord = mesh.textureVertices.get(texCoordIndex);
                    texCoords[vertexInd] = new Point2f(texCoord.x, texCoord.y);
                } else {
                    texCoords[vertexInd] = new Point2f(0, 0);
                }
                int normalIndex = polygon.getNormalIndices().get(vertexInd);
                Vector3f normal = mesh.normals.get(normalIndex);

                // Направление на источник света (камера является источником света)
                Vector3f lightDir = Vector3f.subtraction(camera.getPosition(), vertex).normalizek();
                // Интенсивность света
                lightIntensities[vertexInd] = Math.max(0, normal.dot(lightDir));
            }

            // Растеризация треугольника с текстурой или стандартным цветом
            if (mesh.hasTexture()) {
                Rasterization.fillTriangleWithTexture(graphicsContext, arrX, arrY, arrZ, texCoords, mesh.texture, lightIntensities, isLightingEnabled);
            } else {
                // Средняя интенсивность света для треугольника (если текстуры нет)
                Rasterization.fillTriangle(
                        graphicsContext,
                        arrX,
                        arrY,
                        arrZ,
                        lightIntensities, // Передаем интенсивности света
                        Color.BLUE,
                        isLightingEnabled
                );
            }
        }
    }
}