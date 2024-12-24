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

import static com.cgvsu.render_engine.GraphicConveyor.*;
public class RenderEngine {

    public static void render(
            final GraphicsContext graphicsContext,
            final Camera camera,
            final Model mesh,
            final int width,
            final int height,
            final boolean isLightingEnabled,
            final boolean isTextureEnabled,
            final boolean isPolygonalGridEnabled) {

        // Инициализируем Z-буфер
        double[][] zBuffer = initializeZBuffer(width, height);

        Matrix4f modelMatrix = rotateScaleTranslate();
        Matrix4f viewMatrix = camera.getViewMatrix();
        Matrix4f projectionMatrix = camera.getProjectionMatrix();

        Matrix4f modelViewProjectionMatrix = new Matrix4f(modelMatrix);
        modelViewProjectionMatrix.mul(viewMatrix);
        modelViewProjectionMatrix.mul(projectionMatrix);

        for (Polygon polygon : mesh.polygons) {
            if (polygon.getVertexIndices().size() != 3) continue; // Обрабатываем только треугольники

            TriangleData triangleData = prepareTriangleData(polygon, mesh, modelViewProjectionMatrix, width, height, camera);
            TriangleRenderer triangleRenderer = chooseTriangleRenderer(mesh, isTextureEnabled, Color.BLUE, zBuffer);
            triangleRenderer.render(graphicsContext, triangleData.arrX, triangleData.arrY, triangleData.arrZ, triangleData.texCoords, triangleData.lightIntensities, isLightingEnabled);
        }
        if (isPolygonalGridEnabled) {
            DrawWireframe.drawWireframe(graphicsContext, mesh, camera, width, height);
        }
    }

    public static double[][] initializeZBuffer(int width, int height) {
        double[][] zBuffer = new double[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                zBuffer[i][j] = Double.POSITIVE_INFINITY; // Инициализируем максимальной глубиной
            }
        }
        return zBuffer;
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

        for (int vertexInd = 0; vertexInd < 3; ++vertexInd) {
            int vertexIndex = polygon.getVertexIndices().get(vertexInd);
            Vector3f vertex = mesh.vertices.get(vertexIndex);

            Vector3f transformedVertex = multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertex);
            Point2f screenPoint = vertexToPoint(transformedVertex, width, height);

            arrX[vertexInd] = (int) screenPoint.x;
            arrY[vertexInd] = (int) screenPoint.y;
            arrZ[vertexInd] = transformedVertex.z;

            if (!polygon.getTextureVertexIndices().isEmpty()) {
                int texCoordIndex = polygon.getTextureVertexIndices().get(vertexInd);
                Vector2f texCoord = mesh.textureVertices.get(texCoordIndex);
                texCoords[vertexInd] = new Point2f(texCoord.x, texCoord.y);
            }
            else {
                System.out.println("Капуто вальдемаро");
            }

            int normalIndex = polygon.getNormalIndices().get(vertexInd);
            Vector3f normal = mesh.normals.get(normalIndex);
            Vector3f lightDir = Vector3f.subtraction(camera.getPosition(), vertex).normalizek();
            lightIntensities[vertexInd] = Math.max(0, normal.dot(lightDir));
        }

        return new TriangleData(arrX, arrY, arrZ, texCoords, lightIntensities);
    }

    private static TriangleRenderer chooseTriangleRenderer(Model mesh, boolean isTextureEnabled, Color baseColor, double[][] zBuffer) {
        if (mesh.hasTexture() && isTextureEnabled) {
            return new TexturedTriangleRenderer(mesh.texture, zBuffer);
        } else {
            return new NonTexturedTriangleRenderer(baseColor, zBuffer);
        }
    }
}