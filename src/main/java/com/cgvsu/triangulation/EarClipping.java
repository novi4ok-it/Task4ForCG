package com.cgvsu.triangulation;

import com.cgvsu.math.Vector3f;
import com.cgvsu.math.VectorMath;
import com.cgvsu.model.Polygon;

import java.util.ArrayList;
import java.util.List;

public class EarClipping {

    public static List<Polygon> triangulate(Polygon polygon, List<Vector3f> vertices) {
        List<Integer> remainingIndices = new ArrayList<>(polygon.getVertexIndices());
        List<Polygon> triangles = new ArrayList<>();

        if (remainingIndices.size() < 3) {
            throw new IllegalArgumentException("Polygon must have at least three vertices.");
        }

        // Удаляем уши до тех пор, пока не останется 3 вершины
        while (remainingIndices.size() > 3) {
            boolean earFound = false;

            for (int i = 0; i < remainingIndices.size(); i++) {
                int prev = remainingIndices.get((i - 1 + remainingIndices.size()) % remainingIndices.size());
                int curr = remainingIndices.get(i);
                int next = remainingIndices.get((i + 1) % remainingIndices.size());

                // Проверка коллинеарности: если коллинеарны, то это не "ухо", но не пропускаем их полностью
                if (isCollinear(vertices.get(prev), vertices.get(curr), vertices.get(next))) {
                    continue; // Пропускаем только те коллинеарные точки, которые не образуют валидный треугольник
                }

                if (isEar(prev, curr, next, remainingIndices, vertices)) {
                    // Генерируем новый треугольник, используя текущие вершины
                    Polygon triangle = new Polygon(List.of(prev, curr, next));
                    triangles.add(triangle);
                    remainingIndices.remove(i);  // Удаляем текущую вершину (где i)
                    earFound = true;
                    break;
                }
            }

            if (!earFound) {
                throw new IllegalStateException("Cannot find an ear for the polygon. Polygon may be invalid.");
            }
        }

        // Последний треугольник из оставшихся 3 вершин
        triangles.add(new Polygon(remainingIndices));
        return triangles;
    }

    private static boolean isEar(int prev, int curr, int next, List<Integer> remainingIndices, List<Vector3f> vertices) {
        Vector3f vPrev = vertices.get(prev);
        Vector3f vCurr = vertices.get(curr);
        Vector3f vNext = vertices.get(next);

        // Проверка на выпуклость
        if (!isConvex(vPrev, vCurr, vNext)) {
            return false;
        }

        // Проверка, что нет других точек внутри треугольника
        for (int index : remainingIndices) {
            if (index == prev || index == curr || index == next) {
                continue;
            }
            Vector3f vPoint = vertices.get(index);
            if (VectorMath.isPointInTriangle(vPrev, vCurr, vNext, vPoint)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isCollinear(Vector3f p1, Vector3f p2, Vector3f p3) {
        return Math.abs(VectorMath.crossProduct(p1, p2, p3)) < VectorMath.EPSILON; // Проверка на коллинеарность
    }

    private static boolean isConvex(Vector3f prev, Vector3f curr, Vector3f next) {
        // Проверка на выпуклость через кросс-продукт
        float cross = VectorMath.crossProduct(prev, curr, next);
        return cross > 0 || Math.abs(cross) < VectorMath.EPSILON; // Если кросс-продукт близок к нулю, считаем точки коллинеарными
    }
}