package com.cgvsu.triangulation;

import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Polygon;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EarClippingTest {

    @Test
    public void testTriangulateConvexPolygon() {
        // Вершины выпуклого пятиугольника
        List<Vector3f> vertices = List.of(
                new Vector3f(0, 0, 0),
                new Vector3f(1, 0, 0),
                new Vector3f(1, 1, 0),
                new Vector3f(0.5f, 1.5f, 0),
                new Vector3f(0, 1, 0)
        );

        // Исходный полигон
        Polygon polygon = new Polygon(List.of(0, 1, 2, 3, 4));

        // Триангуляция
        List<Polygon> triangles = EarClipping.triangulate(polygon, vertices);

        // Проверяем количество треугольников
        assertEquals(3, triangles.size());

        // Проверяем треугольники, игнорируя порядок индексов
        List<List<Integer>> expectedTriangles = List.of(
                List.of(0, 1, 4),
                List.of(1, 2, 4),
                List.of(2, 3, 4)
        );

        for (List<Integer> expected : expectedTriangles) {
            boolean contains = triangles.stream()
                    .anyMatch(triangle -> new HashSet<>(triangle.getVertexIndices()).equals(new HashSet<>(expected)));
            assertTrue(contains, "Expected triangle " + expected + " not found.");
        }
    }

    @Test
    public void testTriangulateConcavePolygon() {
        // Вершины невыпуклого многоугольника
        List<Vector3f> vertices = List.of(
                new Vector3f(0, 0, 0),
                new Vector3f(2, 0, 0),
                new Vector3f(2, 2, 0),
                new Vector3f(1, 1, 0), // Внутренняя вершина
                new Vector3f(0, 2, 0)
        );

        // Исходный полигон
        Polygon polygon = new Polygon(List.of(0, 1, 2, 3, 4));

        // Триангуляция
        List<Polygon> triangles = EarClipping.triangulate(polygon, vertices);

        // Проверяем количество треугольников
        assertEquals(3, triangles.size());

        // Проверяем треугольники
        assertTrue(triangles.contains(new Polygon(List.of(0, 1, 3))));
        assertTrue(triangles.contains(new Polygon(List.of(1, 2, 3))));
        assertTrue(triangles.contains(new Polygon(List.of(0, 3, 4))));
    }

    @Test
    public void testTriangulateInvalidPolygonTooFewVertices() {
        // Полигон с менее чем тремя вершинами
        Polygon polygon = new Polygon(List.of(0, 1));
        List<Vector3f> vertices = List.of(
                new Vector3f(0, 0, 0),
                new Vector3f(1, 0, 0)
        );

        // Проверяем, что возникает исключение
        assertThrows(IllegalArgumentException.class, () -> {
            EarClipping.triangulate(polygon, vertices);
        });
    }

    @Test
    public void testTriangulatePolygonWithCollinearPoints() {
        // Вершины многоугольника с коллинеарными точками
        List<Vector3f> vertices = List.of(
                new Vector3f(0, 0, 0),
                new Vector3f(1, 0, 0),
                new Vector3f(2, 0, 0),
                new Vector3f(2, 1, 0),
                new Vector3f(0, 1, 0)
        );

        // Исходный полигон
        Polygon polygon = new Polygon(List.of(0, 1, 2, 3, 4));

        // Триангуляция
        List<Polygon> triangles = EarClipping.triangulate(polygon, vertices);

        // Проверяем количество треугольников
        assertEquals(3, triangles.size());

        // Ожидаемые треугольники, игнорируя порядок индексов
        List<List<Integer>> expectedTriangles = List.of(
                List.of(0, 1, 4),
                List.of(1, 3, 4),
                List.of(1, 2, 3)
        );

        // Логируем результат триангуляции для отладки
        System.out.println("Generated triangles: ");
        for (Polygon triangle : triangles) {
            System.out.println(triangle.getVertexIndices());
        }

        // Проверяем каждый ожидаемый треугольник
        for (List<Integer> expected : expectedTriangles) {
            boolean found = triangles.stream()
                    .anyMatch(triangle -> {
                        // Проверяем, что набор индексов треугольника совпадает с ожидаемым
                        HashSet<Integer> triangleSet = new HashSet<>(triangle.getVertexIndices());
                        HashSet<Integer> expectedSet = new HashSet<>(expected);
                        return triangleSet.equals(expectedSet);
                    });
            assertTrue(found, "Expected triangle " + expected + " not found.");
        }
    }

    @Test
    public void testTriangulateSimpleTriangle() {
        // Вершины треугольника
        List<Vector3f> vertices = List.of(
                new Vector3f(0, 0, 0),
                new Vector3f(1, 0, 0),
                new Vector3f(0, 1, 0)
        );

        // Исходный треугольник
        Polygon polygon = new Polygon(List.of(0, 1, 2));

        // Триангуляция
        List<Polygon> triangles = EarClipping.triangulate(polygon, vertices);

        // Проверяем, что результат содержит только один треугольник
        assertEquals(1, triangles.size());
        assertEquals(new Polygon(List.of(0, 1, 2)), triangles.get(0));
    }
}