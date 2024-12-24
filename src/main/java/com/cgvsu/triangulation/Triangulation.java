package com.cgvsu.triangulation;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;

import java.util.ArrayList;
import java.util.List;

public class Triangulation {

    public static void triangulateModel(Model mesh) {
        if (mesh != null) {
            List<Polygon> triangulatedPolygons = new ArrayList<>();

            for (Polygon polygon : mesh.polygons) {
                List<Polygon> triangles = Triangulation.triangulate(polygon, mesh.vertices, mesh.textureVertices);
                triangulatedPolygons.addAll(triangles);
            }

            // Перезаписываем полигоны с результатами триангуляции
            mesh.polygons = new ArrayList<>(triangulatedPolygons);
        }
    }

    public static List<Polygon> triangulate(Polygon polygon, List<Vector3f> vertices, List<Vector2f> textureVertices) {
        if (polygon == null || vertices == null || textureVertices == null) {
            throw new IllegalArgumentException("Polygon, vertices, or texture vertices list cannot be null.");
        }

        List<Integer> vertexIndices = new ArrayList<>(polygon.getVertexIndices());
        List<Integer> textureIndices = new ArrayList<>(polygon.getTextureVertexIndices());
        List<Integer> normalIndices = new ArrayList<>(polygon.getNormalIndices());

        if (vertexIndices.size() < 3) {
            throw new IllegalArgumentException("Polygon must have at least three vertices.");
        }

        return simpleTriangulation(vertexIndices, textureIndices, normalIndices);
    }

    private static List<Polygon> simpleTriangulation(List<Integer> vertexIndices, List<Integer> textureIndices, List<Integer> normalIndices) {
        List<Polygon> triangles = new ArrayList<>();

        // Используем первую вершину как фиксированную и создаём треугольники с остальными
        int fixedVertexIndex = vertexIndices.get(0);
        int fixedTextureIndex = textureIndices.isEmpty() ? -1 : textureIndices.get(0);
        int fixedNormalIndex = normalIndices.isEmpty() ? -1 : normalIndices.get(0);

        for (int i = 1; i < vertexIndices.size() - 1; i++) {
            int currVertexIndex = vertexIndices.get(i);
            int nextVertexIndex = vertexIndices.get(i + 1);

            int currTextureIndex = textureIndices.isEmpty() ? -1 : textureIndices.get(i);
            int nextTextureIndex = textureIndices.isEmpty() ? -1 : textureIndices.get(i + 1);

            int currNormalIndex = normalIndices.isEmpty() ? -1 : normalIndices.get(i);
            int nextNormalIndex = normalIndices.isEmpty() ? -1 : normalIndices.get(i + 1);

            Polygon triangle = new Polygon(List.of(fixedVertexIndex, currVertexIndex, nextVertexIndex));

            if (!textureIndices.isEmpty()) {
                triangle.setTextureVertexIndices(
                        new ArrayList<>(List.of(fixedTextureIndex, currTextureIndex, nextTextureIndex))
                );
            }

            if (!normalIndices.isEmpty()) {
                triangle.setNormalIndices(
                        new ArrayList<>(List.of(fixedNormalIndex, currNormalIndex, nextNormalIndex))
                );
            }

            triangles.add(triangle);
        }

        return triangles;
    }
}