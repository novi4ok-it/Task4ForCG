package com.cgvsu.render_engine;

import com.cgvsu.math.*;
import com.cgvsu.model.Polygon;
import com.cgvsu.rasterization.NonTexturedTriangleRenderer;
import com.cgvsu.rasterization.TexturedTriangleRenderer;
import com.cgvsu.rasterization.TriangleRenderer;
import com.cgvsu.triangulation.DrawWireframe;
import javafx.scene.canvas.GraphicsContext;
import com.cgvsu.model.Model;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

import static com.cgvsu.render_engine.GraphicConveyor.*;

public class RenderEngine {

    public static void render(final RenderContext context) {
        Matrix4f modelViewProjectionMatrix = calculateMVPMatrix(context.getCamera(), context);

        AllVertexCoordinates allVertexCoordinates = new AllVertexCoordinates(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        for (Polygon polygon : context.getMesh().polygons) {
            if (polygon.getVertexIndices().size() != 3) continue; // Обрабатываем только треугольники

            TriangleData triangleData = prepareTriangleData(
                    polygon,
                    context.getMesh(),
                    modelViewProjectionMatrix,
                    context.getWidth(),
                    context.getHeight(),
                    allVertexCoordinates);

            TriangleRenderer triangleRenderer = chooseTriangleRenderer(
                    context.getMesh(),
                    context.isTextureEnabled(),
                    Color.BLACK,
                    context.getZBuffer());

            triangleRenderer.render(
                    context.getGraphicsContext(),
                    triangleData.arrX,
                    triangleData.arrY,
                    triangleData.arrZ,
                    triangleData.texCoords,
                    context.getLightSources(),
                    triangleData.normals);
            context.setZBuffer(triangleRenderer.getZBuffer());
        }

        if (context.isPolygonalGridEnabled()) {
            DrawWireframe.drawWireframe(
                    context.getGraphicsContext(),
                    context.getZBuffer(),
                    allVertexCoordinates.getAllX(),
                    allVertexCoordinates.getAllY());
        }
    }

    private static Matrix4f calculateMVPMatrix(Camera camera, final RenderContext context) {
        Matrix4f modelMatrix = AffineTransformations.rotateScaleTranslate(
            context.getMesh().getScale().x(), context.getMesh().getScale().y(), context.getMesh().getScale().z(),
            context.getMesh().getRotation().x(), context.getMesh().getRotation().y(), context.getMesh().getRotation().z(),
            context.getMesh().getTranslation().x(), context.getMesh().getTranslation().y(), context.getMesh().getTranslation().z()
        );
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
            AllVertexCoordinates allVertexCoordinates) {

        int[] arrX = new int[3];
        int[] arrY = new int[3];
        float[] arrZ = new float[3];
        Point2f[] texCoords = new Point2f[3];
        ArrayList<Vector3f> normals = new ArrayList<>();
        for (int vertexInd = 0; vertexInd < 3; ++vertexInd) {
            int vertexIndex = polygon.getVertexIndices().get(vertexInd);
            Vector3f vertex = mesh.vertices.get(vertexIndex);

            Vector3f transformedVertex = multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertex);
            Point2f screenPoint = vertexToPoint(transformedVertex, width, height);

            arrX[vertexInd] = (int) screenPoint.x;
            arrY[vertexInd] = (int) screenPoint.y;
            arrZ[vertexInd] = transformedVertex.z;

            // Добавляем элементы в общие списки
            allVertexCoordinates.addX(arrX[vertexInd]);
            allVertexCoordinates.addY(arrY[vertexInd]);
            allVertexCoordinates.addZ(arrZ[vertexInd]);

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
        }
        return new TriangleData(arrX, arrY, arrZ, texCoords, normals);
    }

    private static TriangleRenderer chooseTriangleRenderer(Model mesh, boolean isTextureEnabled, Color baseColor,
                                                           double[][] zBuffer) {
        if (mesh.hasTexture() && isTextureEnabled) {
            return new TexturedTriangleRenderer(mesh.texture, zBuffer);
        } else {
            return new NonTexturedTriangleRenderer(baseColor, zBuffer);
        }
    }
}