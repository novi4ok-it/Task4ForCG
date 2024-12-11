package com.cgvsu.triangulation;

import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Polygon;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TriangulationTest {

    @Test
    public void testSimplePolygon() {
        // Простой четырёхугольник
        List<Vector3f> vertices = List.of(
                new Vector3f(0, 0, 0),
                new Vector3f(1, 0, 0),
                new Vector3f(1, 1, 0),
                new Vector3f(0, 1, 0)
        );
        Polygon polygon = new Polygon(List.of(0, 1, 2, 3));

        List<Polygon> result = Triangulation.triangulate(polygon, vertices);

        assertEquals(2, result.size()); // Должно получиться 2 треугольника

        List<List<Integer>> expectedTriangles = List.of(
                List.of(0, 1, 2),
                List.of(0, 2, 3)
        );

        for (Polygon triangle : result) {
            assertTrue(expectedTriangles.contains(triangle.getVertexIndices()));
        }
    }

    @Test
    public void testPolygonWithCollinearPoints() {
        // Полигон с коллинеарными точками
        List<Vector3f> vertices = List.of(
                new Vector3f(0, 0, 0),
                new Vector3f(1, 0, 0),
                new Vector3f(2, 0, 0), // Коллинеарная точка
                new Vector3f(1, 1, 0)
        );
        Polygon polygon = new Polygon(List.of(0, 1, 2, 3));

        List<Polygon> result = Triangulation.triangulate(polygon, vertices);

        assertEquals(2, result.size()); // Должно получиться 2 треугольника

        List<List<Integer>> expectedTriangles = List.of(
                List.of(0, 1, 3),
                List.of(1, 2, 3)
        );

        for (Polygon triangle : result) {
            assertTrue(expectedTriangles.contains(triangle.getVertexIndices()));
        }
    }

    @Test
    public void testFallbackTriangulation() {
        // Сложный полигон, который не может быть обработан методом "ушей"
        List<Vector3f> vertices = List.of(
                new Vector3f(0, 0, 0),
                new Vector3f(1, 0, 0),
                new Vector3f(1, 1, 0),
                new Vector3f(0, 1, 0),
                new Vector3f(0.5f, 0.5f, 0) // Центральная точка, делающая полигон "проблемным"
        );
        Polygon polygon = new Polygon(List.of(0, 1, 2, 3, 4));

        List<Polygon> result = Triangulation.triangulate(polygon, vertices);

        assertTrue(result.size() >= 3); // Должно быть хотя бы 3 треугольника
    }

    @Test
    public void testInvalidPolygon() {
        // Полигон с недостаточным количеством точек
        List<Vector3f> vertices = List.of(
                new Vector3f(0, 0, 0),
                new Vector3f(1, 0, 0)
        );
        Polygon polygon = new Polygon(List.of(0, 1));

        assertThrows(IllegalArgumentException.class, () -> {
            Triangulation.triangulate(polygon, vertices);
        });
    }

    @Test
    public void testNullInputs() {
        // Проверка на null значения
        assertThrows(IllegalArgumentException.class, () -> {
            Triangulation.triangulate(null, null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Triangulation.triangulate(new Polygon(new ArrayList<>()), null);
        });
    }

    @Test
    public void testSingleTrianglePolygon() {
        // Полигон, который уже является треугольником
        List<Vector3f> vertices = List.of(
                new Vector3f(0, 0, 0),
                new Vector3f(1, 0, 0),
                new Vector3f(0, 1, 0)
        );
        Polygon polygon = new Polygon(List.of(0, 1, 2));

        List<Polygon> result = Triangulation.triangulate(polygon, vertices);

        assertEquals(1, result.size()); // Должен быть один треугольник
        assertEquals(new Polygon(List.of(0, 1, 2)), result.get(0));
    }

    @Test
    public void testLargeConvexPolygon() {
        // Большой выпуклый полигон
        List<Vector3f> vertices = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            float angle = (float) (2 * Math.PI * i / 10);
            vertices.add(new Vector3f((float) Math.cos(angle), (float) Math.sin(angle), 0));
            indices.add(i);
        }

        Polygon polygon = new Polygon(indices);
        List<Polygon> result = Triangulation.triangulate(polygon, vertices);

        assertEquals(8, result.size()); // Для выпуклого десятиугольника будет 8 треугольников
    }
}
