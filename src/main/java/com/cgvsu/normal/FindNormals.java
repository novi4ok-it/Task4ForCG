package com.cgvsu.normal;

import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Polygon;

import java.util.ArrayList;
import java.util.List;

public class FindNormals {
    public static ArrayList<Vector3f> findNormals(List<Polygon> polygons, List<Vector3f> vertices) {
        ArrayList<Vector3f> temporaryNormals = new ArrayList<>();
        ArrayList<Vector3f> normals = new ArrayList<>();

        // Вычисляем нормали для каждого полигона
        for (Polygon p : polygons) {
            Vector3f normal = findPolygonNormal(
                vertices.get(p.getVertexIndices().get(0)),
                vertices.get(p.getVertexIndices().get(1)),
                vertices.get(p.getVertexIndices().get(2))
            );
            temporaryNormals.add(normal);
        }

        // Вычисляем нормали для каждой вершины
        for (int i = 0; i < vertices.size(); i++) {
            List<Vector3f> polygonNormalsList = new ArrayList<>();
            List<Float> weights = new ArrayList<>();

            for (int j = 0; j < polygons.size(); j++) {
                if (polygons.get(j).getVertexIndices().contains(i)) {
                    polygonNormalsList.add(temporaryNormals.get(j));

                    // Вычисляем вес по площади треугольника
                    Polygon p = polygons.get(j);
                    Vector3f v0 = vertices.get(p.getVertexIndices().get(0));
                    Vector3f v1 = vertices.get(p.getVertexIndices().get(1));
                    Vector3f v2 = vertices.get(p.getVertexIndices().get(2));
                    float area = vectorProduct(
                        Vector3f.subtraction(v1, v0),
                        Vector3f.subtraction(v2, v0)
                    ).length();
                    weights.add(area);
                }
            }

            normals.add(findVertexNormal(polygonNormalsList, weights));
        }

        return normals;
    }

    public static Vector3f findPolygonNormal(Vector3f... vs) {
        Vector3f a = Vector3f.subtraction(vs[1], vs[0]);
        Vector3f b = Vector3f.subtraction(vs[2], vs[0]);

        // Векторное произведение
        Vector3f normal = vectorProduct(a, b);

        // Нормализация
        return normalize(normal);
    }

    public static Vector3f findVertexNormal(List<Vector3f> normals, List<Float> weights) {
        float xs = 0, ys = 0, zs = 0;

        for (int i = 0; i < normals.size(); i++) {
            Vector3f normal = normals.get(i);
            float weight = weights.get(i);

            xs += normal.x * weight;
            ys += normal.y * weight;
            zs += normal.z * weight;
        }

        return normalize(new Vector3f(xs, ys, zs));
    }

    public static double determinant(Vector3f a, Vector3f b, Vector3f c) {
        return a.x * (b.y * c.z - b.z * c.y) -
               a.y * (b.x * c.z - b.z * c.x) +
               a.z * (b.x * c.y - b.y * c.x);
    }

    public static Vector3f normalize(Vector3f v) {
        if (v == null) return new Vector3f(0, 0, 0);

        double length = Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z);
        if (length == 0) return new Vector3f(0, 0, 0);

        return new Vector3f(v.x / (float) length, v.y / (float) length, v.z / (float) length);
    }

    public static Vector3f vectorProduct(Vector3f a, Vector3f b) {
        return new Vector3f(
            a.y * b.z - a.z * b.y,
            a.z * b.x - a.x * b.z,
            a.x * b.y - a.y * b.x
        );
    }
}