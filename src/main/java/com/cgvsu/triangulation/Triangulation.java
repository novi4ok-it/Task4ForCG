package com.cgvsu.triangulation;

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
                List<Polygon> triangles = Triangulation.triangulate(polygon, mesh.vertices);
                for (Polygon triangle : triangles) {
                    // Копируем текстурные координаты, если они есть
                    if (!polygon.getTextureVertexIndices().isEmpty()) {
                        triangle.setTextureVertexIndices(
                                new ArrayList<>(polygon.getTextureVertexIndices().subList(0, 3))
                        );
                    }

                    // Копируем нормали, если они есть
                    if (!polygon.getNormalIndices().isEmpty()) {
                        triangle.setNormalIndices(
                                new ArrayList<>(polygon.getNormalIndices().subList(0, 3))
                        );
                    }
                }

                triangulatedPolygons.addAll(triangles);
            }

            // Перезаписываем полигоны с результатами триангуляции
            mesh.polygons = (ArrayList<Polygon>) triangulatedPolygons;
        }
    }
       public static List<Polygon> triangulate(Polygon polygon, List<Vector3f> vertices) {
        if (polygon == null || vertices == null) {
            throw new IllegalArgumentException("Polygon or vertices list cannot be null.");
        }

        List<Integer> remainingIndices = new ArrayList<>(polygon.getVertexIndices());
        List<Polygon> triangles;

        if (remainingIndices.size() < 3) {
            throw new IllegalArgumentException("Polygon must have at least three vertices.");
        }

        triangles = simpleTriangulation(remainingIndices);

        return triangles;
    }

    private static List<Polygon> simpleTriangulation(List<Integer> remainingIndices) {
        List<Polygon> triangles = new ArrayList<>();

        // Используем первую вершину как фиксированную и создаём треугольники с остальными
        int fixedIndex = remainingIndices.get(0);
        for (int i = 1; i < remainingIndices.size() - 1; i++) {
            int curr = remainingIndices.get(i);
            int next = remainingIndices.get(i + 1);
            triangles.add(new Polygon(List.of(fixedIndex, curr, next)));
        }

        return triangles;
    }
}