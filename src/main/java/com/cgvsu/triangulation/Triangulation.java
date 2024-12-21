package com.cgvsu.triangulation;

import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Polygon;

import java.util.ArrayList;
import java.util.List;

public class Triangulation {

       public static List<Polygon> triangulate(Polygon polygon, List<Vector3f> vertices) {
        if (polygon == null || vertices == null) {
            throw new IllegalArgumentException("Polygon or vertices list cannot be null.");
        }

        List<Integer> remainingIndices = new ArrayList<>(polygon.getVertexIndices());
        List<Polygon> triangles;

        if (remainingIndices.size() < 3) {
            throw new IllegalArgumentException("Polygon must have at least three vertices.");
        }

        // Используем самую простую триангуляцию: первый треугольник с первой вершиной.
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