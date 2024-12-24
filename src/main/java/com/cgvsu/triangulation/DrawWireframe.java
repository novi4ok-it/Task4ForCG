package com.cgvsu.triangulation;

import com.cgvsu.math.Matrix4f;
import com.cgvsu.math.Point2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import com.cgvsu.render_engine.Camera;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static com.cgvsu.render_engine.GraphicConveyor.*;
import static com.cgvsu.render_engine.RenderEngine.initializeZBuffer;


public class DrawWireframe {

    public static void drawWireframe(GraphicsContext gc, Model mesh, Camera camera, int width, int height) {
        // Инициализируем Z-буфер
        double[][] zBuffer = initializeZBuffer(width, height);

        Matrix4f modelMatrix = rotateScaleTranslate();
        Matrix4f viewMatrix = camera.getViewMatrix();
        Matrix4f projectionMatrix = camera.getProjectionMatrix();

        Matrix4f modelViewProjectionMatrix = new Matrix4f(modelMatrix);
        modelViewProjectionMatrix.mul(viewMatrix);
        modelViewProjectionMatrix.mul(projectionMatrix);

        gc.setStroke(Color.GRAY);
        gc.setLineWidth(1.0);

        for (Polygon triangle : mesh.polygons) {
            final int nVertices = triangle.getVertexIndices().size();
            double[] xCoords = new double[nVertices];
            double[] yCoords = new double[nVertices];
            float[] zCoords = new float[nVertices];
            Vector3f[] transformedVertices = new Vector3f[nVertices];

            for (int i = 0; i < nVertices; i++) {
                int vertexIndex = triangle.getVertexIndices().get(i);
                Vector3f vertex = mesh.vertices.get(vertexIndex);

                Vector3f transformedVertex = multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertex);
                transformedVertices[i] = transformedVertex;
                Point2f screenPoint = vertexToPoint(transformedVertex, width, height);

                xCoords[i] = screenPoint.x;
                yCoords[i] = screenPoint.y;
                zCoords[i] = transformedVertex.z; // Сохраняем глубину
            }

            // Проверяем ориентацию нормали
            if (!isFrontFacing(transformedVertices)) {
                continue; // Пропускаем невидимые треугольники
            }

            // Соединяем вершины треугольника с учетом Z-буфера
            for (int i = 0; i < nVertices; i++) {
                int next = (i + 1) % nVertices;
                drawLineWithZBuffer(gc, (int) xCoords[i], (int) yCoords[i], zCoords[i], (int) xCoords[next], (int) yCoords[next], zCoords[next], zBuffer);
            }
        }
    }

    private static boolean isFrontFacing(Vector3f[] vertices) {
        Vector3f v0 = vertices[0];
        Vector3f v1 = vertices[1];
        Vector3f v2 = vertices[2];

        // Вычисляем нормаль плоскости
        Vector3f edge1 = Vector3f.subtraction(v1, v0);
        Vector3f edge2 = Vector3f.subtraction(v2, v0);
        Vector3f normal = Vector3f.vectorProduct(edge1, edge2);

        // Проверяем, направлена ли нормаль к камере
        return normal.z < 0; // Считаем, что камера смотрит в направлении -Z
    }

    public static void drawLineWithZBuffer(GraphicsContext gc, int x0, int y0, float z0, int x1, int y1, float z1, double[][] zBuffer) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;

        while (true) {
            if (x0 >= 0 && x0 < zBuffer.length && y0 >= 0 && y0 < zBuffer[0].length) {
                float t = (float) (Math.sqrt((x1 - x0) * (x1 - x0) + (y1 - y0) * (y1 - y0)) / Math.sqrt(dx * dx + dy * dy));
                float z = z0 * (1 - t) + z1 * t; // Интерполяция Z

                if (z < zBuffer[x0][y0]) {
                    zBuffer[x0][y0] = z;
                    gc.getPixelWriter().setColor(x0, y0, Color.YELLOW);
                }
            }

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
