package com.cgvsu.render_engine;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Polygon;
import com.cgvsu.rasterization.NonTexturedTriangleRenderer;
import com.cgvsu.rasterization.TexturedTriangleRenderer;
import com.cgvsu.rasterization.TriangleRenderer;
import com.cgvsu.triangulation.DrawWireframe;
import javafx.scene.canvas.GraphicsContext;
import com.cgvsu.model.Model;

import com.cgvsu.math.Matrix4f;
import com.cgvsu.math.Point2f;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

import static com.cgvsu.render_engine.GraphicConveyor.*;

public class RenderEngine {

    public static void render(
            final GraphicsContext graphicsContext,
            final Camera camera,
            final Model mesh,
            final int width,
            final int height,
            List<ColorLighting> lightSources,
            final boolean isTextureEnabled,
            final boolean isPolygonalGridEnabled,
            double[][] zBuffer) {

        Matrix4f modelViewProjectionMatrix = calculateMVPMatrix(camera);

        for (Polygon polygon : mesh.polygons) {
            if (polygon.getVertexIndices().size() != 3) continue; // Обрабатываем только треугольники

            TriangleData triangleData = prepareTriangleData(polygon, mesh, modelViewProjectionMatrix, width, height, camera);
            TriangleRenderer triangleRenderer = chooseTriangleRenderer(mesh, isTextureEnabled, Color.BLACK, zBuffer);
            triangleRenderer.render(graphicsContext, triangleData.arrX, triangleData.arrY, triangleData.arrZ, triangleData.texCoords, lightSources, triangleData.normals);
            zBuffer = triangleRenderer.getZBuffer();
        }

        if (isPolygonalGridEnabled) {
            drawWireframe(graphicsContext, modelViewProjectionMatrix, mesh, width, height, zBuffer);
        }

    }

    private static Matrix4f calculateMVPMatrix(Camera camera) {
        Matrix4f modelMatrix = rotateScaleTranslate();
        Matrix4f viewMatrix = camera.getViewMatrix();
        Matrix4f projectionMatrix = camera.getProjectionMatrix();

        Matrix4f modelViewProjectionMatrix = new Matrix4f(modelMatrix);
        modelViewProjectionMatrix.mul(viewMatrix);
        modelViewProjectionMatrix.mul(projectionMatrix);

        return modelViewProjectionMatrix;
    }

    private static TriangleData prepareTriangleData(
            Polygon polygon,
            Model mesh,
            Matrix4f modelViewProjectionMatrix,
            int width,
            int height,
            Camera camera) {

        int[] arrX = new int[3];
        int[] arrY = new int[3];
        float[] arrZ = new float[3];
        Point2f[] texCoords = new Point2f[3];
        float[] lightIntensities = new float[3];
        ArrayList<Vector3f> normals = new ArrayList<>();
        final int nVertices = polygon.getVertexIndices().size();
        Vector3f[] transformedVertices = new Vector3f[nVertices];
        for (int vertexInd = 0; vertexInd < 3; ++vertexInd) {
            int vertexIndex = polygon.getVertexIndices().get(vertexInd);
            Vector3f vertex = mesh.vertices.get(vertexIndex);

            Vector3f transformedVertex = multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertex);
            transformedVertices[vertexInd] = transformedVertex;
            Point2f screenPoint = vertexToPoint(transformedVertex, width, height);

            arrX[vertexInd] = (int) screenPoint.x;
            arrY[vertexInd] = (int) screenPoint.y;
            arrZ[vertexInd] = transformedVertex.z;

            if (!polygon.getTextureVertexIndices().isEmpty()) {
                int texCoordIndex = polygon.getTextureVertexIndices().get(vertexInd);
                Vector2f texCoord = mesh.textureVertices.get(texCoordIndex);
                texCoords[vertexInd] = new Point2f(texCoord.x, texCoord.y);
            } else {
                System.out.println("Капуто вальдемаро");
            }

            int normalIndex = polygon.getNormalIndices().get(vertexInd);
            Vector3f normal = mesh.normals.get(normalIndex);
            normals.add(normal);
            Vector3f lightDir = Vector3f.subtraction(camera.getPosition(), vertex).normalizek();
            lightIntensities[vertexInd] = Math.max(0, normal.dot(lightDir));
        }

        return new TriangleData(arrX, arrY, arrZ, texCoords, lightIntensities, normals, transformedVertices);
    }

    private static TriangleRenderer chooseTriangleRenderer(Model mesh, boolean isTextureEnabled, Color baseColor, double[][] zBuffer) {
        if (mesh.hasTexture() && isTextureEnabled) {
            return new TexturedTriangleRenderer(mesh.texture, zBuffer);
        } else {
            return new NonTexturedTriangleRenderer(baseColor, zBuffer);
        }
    }

    public static void drawWireframe(GraphicsContext gc, Matrix4f modelViewProjectionMatrix, Model mesh, int width, int height, double[][] zBuffer) {
        // Настройки графического контекста
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(1.0);

        for (Polygon triangle : mesh.polygons) {
            final int nVertices = triangle.getVertexIndices().size();
            double[] xCoords = new double[nVertices];
            double[] yCoords = new double[nVertices];
            float[] zCoords = new float[nVertices];

            // Трансформация вершин
            for (int i = 0; i < nVertices; i++) {
                int vertexIndex = triangle.getVertexIndices().get(i);
                Vector3f vertex = mesh.vertices.get(vertexIndex);

                Vector3f transformedVertex = multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertex);
                Point2f screenPoint = vertexToPoint(transformedVertex, width, height);

                xCoords[i] = screenPoint.x;
                yCoords[i] = screenPoint.y;
                zCoords[i] = transformedVertex.z; // Сохраняем Z-координату
            }

            // Соединяем вершины треугольника с учётом Z-буфера
            for (int i = 0; i < nVertices; i++) {
                int next = (i + 1) % nVertices;
                drawLineWithZBuffer(
                        gc, (int) xCoords[i], (int) yCoords[i], zCoords[i],
                        (int) xCoords[next], (int) yCoords[next], zCoords[next],
                        zBuffer
                );
            }
        }
    }

    public static void drawLineWithZBuffer(GraphicsContext gc, int x0, int y0, float z0, int x1, int y1, float z1, double[][] zBuffer) {
        int width = zBuffer.length;
        int height = zBuffer[0].length;

        // Проверка, находятся ли обе вершины в пределах Z-буфера и подходят ли они
        boolean startVisible = x0 >= 0 && x0 < width && y0 >= 0 && y0 < height && z0 <= zBuffer[x0][y0];
        boolean endVisible = x1 >= 0 && x1 < width && y1 >= 0 && y1 < height && z1 <= zBuffer[x1][y1];

        if (startVisible && endVisible) {
            // Если обе вершины видимы, рисуем линию без проверки Z-буфера
            drawLineWithoutZBufferCheck(gc, x0, y0, x1, y1);
            return;
        }

        // Если хотя бы одна вершина невидима, выполняем обычную логику
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;
        float dz = z1 - z0;
        float dzx = dx != 0 ? Math.abs(dz / dx) : 0;
        float dzy = dy != 0 ? Math.abs(dz / dy) : 0;
        float z = z0;

        while (true) {
            // Проверка границ Z-буфера
            if (x0 >= 0 && x0 < width && y0 >= 0 && y0 < height) {
                // Проверяем и обновляем Z-буфер
                if (z <= zBuffer[x0][y0]) {
                    zBuffer[x0][y0] = z;
                    gc.getPixelWriter().setColor(x0, y0, Color.YELLOW);
                }
            }

            if (x0 == x1 && y0 == y1) break;

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x0 += sx;
                z += dzx * sx;
            }
            if (e2 < dx) {
                err += dx;
                y0 += sy;
                z += dzy * sy;
            }
        }
    }

    // Вспомогательный метод для рисования линии без проверки Z-буфера
    private static void drawLineWithoutZBufferCheck(GraphicsContext gc, int x0, int y0, int x1, int y1) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;

        while (true) {
            gc.getPixelWriter().setColor(x0, y0, Color.YELLOW);

            if (x0 == x1 && y0 == y1) break;

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x0 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y0 += sy;
            }
        }
    }
}