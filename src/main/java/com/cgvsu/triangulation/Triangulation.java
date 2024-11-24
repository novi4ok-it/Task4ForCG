package com.cgvsu.triangulation;

import com.cgvsu.math.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import com.cgvsu.math.VectorMath;

import static com.cgvsu.math.VectorMath.EPSILON;

/**
 * A utility class for triangulating a 2D polygon.
 *
 * <p>Splits a polygon defined by a set of vertices into non-overlapping triangles.
 * Assumes the polygon has no self-intersections. Works with both clockwise and
 * counter-clockwise polygons.
 *
 * <p>Implemented algorithms
 * <ul>
 *     <li>Convex polygon fan triangulation</li>
 *     <li>Ear clipping algorithm</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * List<Vector> vertices = Arrays.asList(
 *     new Vector(0, 0),
 *     new Vector(1, 0),
 *     new Vector(1, 1),
 *     new Vector(0, 1)
 * );
 * List<Integer> vertexIndices = Arrays.asList(0, 1, 2, 3)
 * List<int[]> triangles = Triangulation.earClippingTriangulate(vertices, vertexIndices)
 * }</pre>
 *
 * This returns the triangles as vertex indices corresponding to the provided list.
 *
 * @see Vector
 */

public final class Triangulation {
    /**
     * Prevents class instantiation.
     * @throws UnsupportedOperationException when called
     */
    private Triangulation() {
        throw new UnsupportedOperationException("Cannot be instantiated.");
    }

    /**
     * Checks if the number of indices in a polygon is correct.
     * <p>Throws an exception if the number of vertices is less than 3
     *
     * @param n the number of vertex indices
     * @throws IllegalArgumentException if {@code n} is less than 3
     */
    private static void checkVertexIndicesCount(int n) {
        if (n < 3) {
            throw new IllegalArgumentException("Not enough vertex indices for a polygon");
        }
    }

    /**
     * Triangulates a convex polygon into a fan triangulation.
     * <p>This a method for triangulating convex polygons from
     * vertex indices. Results in a poor topology but works in O(n).
     *
     * @param vertexIndices vertex indices in order of connection
     * @return a {@code List} consisting of {@code int[]} with 3 indices,
     * corresponding to the vertices of a triangle
     * @throws IllegalArgumentException if {@code vertexIndices} size is less than 3
     */
    public static List<int[]> convexPolygonTriangulate(List<Integer> vertexIndices) {
        int vertexIndicesCount = vertexIndices.size();
        checkVertexIndicesCount(vertexIndicesCount);

        List<int[]> triangles = new ArrayList<>(vertexIndicesCount - 2);

        for (int i = 2; i < vertexIndicesCount; i++) {
            triangles.add(new int[]{0, vertexIndices.get(i), vertexIndices.get(i - 1)});
        }

        return triangles;
    }

    /**
     * Triangulates a convex polygon into a fan triangulation.
     * <p>This a method for triangulating convex polygons with
     * vertices labeled 0 through (n - 1). Results in a poor topology
     * but works in O(n).
     *
     * @param n the number of vertices in a polygon
     * @return a {@code List} consisting of {@code int[]} with 3 indices,
     * corresponding to the vertices of a triangle
     * @throws IllegalArgumentException if {@code n} is less than 3
     */
    public static List<int[]> convexPolygonTriangulate(int n) {
        List<Integer> vertexIndices = IntStream.rangeClosed(0, n - 1).boxed().toList();

        return convexPolygonTriangulate(vertexIndices);
    }

    /**
     * Triangulates a polygon without self-intersections.
     * <p>This method utilizes an ear clipping algorithm. Although
     * it works for most of the polygons you will encounter, this
     * should be used carefully as it works in O(n^2).
     *
     * @param <T> the type of vertices, extending {@link Vector}
     * @param vertices vertices in order of connection
     * @return a {@code List} consisting of {@code int[]} with 3 indices,
     * corresponding to the vertices of a triangle
     * @throws IllegalArgumentException if {@code vertices} size is less than 3
     */
    public static <T extends Vector> List<int[]> earClippingTriangulate(List<T> vertices) {
        List<Integer> vertexIndices = IntStream.rangeClosed(0, vertices.size() - 1).boxed().toList();

        return earClippingTriangulate(vertices, vertexIndices);
    }

    /**
     * Triangulates a polygon without self-intersections.
     * <p>This method utilizes an ear clipping algorithm. Although
     * it works for most of the polygons you will encounter, this
     * should be used carefully as it works in O(n^2).
     *
     * @param <T> the type of vertices, extending {@link Vector}
     * @param vertices vertices to select from
     * @param vertexIndices vertex indices in order of connection
     * @return a {@code List} consisting of {@code int[]} with 3 indices,
     * corresponding to the vertices of a triangle
     * @throws IllegalArgumentException if {@code vertices} size is less than 3
     */
    public static <T extends Vector> List<int[]> earClippingTriangulate(List<T> vertices, List<Integer> vertexIndices) {
        int vertexIndicesCount = vertexIndices.size();
        checkVertexIndicesCount(vertexIndicesCount);

        int vertexCount = vertices.size();
        for (Integer vertexIndex : vertexIndices) {
            if (vertexIndex >= vertexCount) {
                throw new IllegalArgumentException(
                    String.format("Vertex index %d is outside of vertex list of length %d", vertexIndex, vertexCount)
                );
            }
        }

        List<int[]> triangles = new ArrayList<>(vertexIndicesCount - 2);
        // copy vertexIndices to avoid side effects on input data
        List<Integer> potentialEars = new ArrayList<>(vertexIndices);
        int potentialEarsCount = vertexIndicesCount;

        boolean isCCW = isCounterClockwise(vertices, vertexIndices);

        for (boolean hasClippedEars = true; hasClippedEars;) {
            hasClippedEars = false;
            for (int i = 1; i < potentialEarsCount - 1; i++) {
                int prevVertexIndex = potentialEars.get(i - 1);
                int curVertexIndex = potentialEars.get(i);
                int nextVertexIndex = potentialEars.get(i + 1);

                Vector prevVertex = vertices.get(prevVertexIndex);
                Vector curVertex = vertices.get(curVertexIndex);
                Vector nextVertex = vertices.get(nextVertexIndex);

                float crossProduct = VectorMath.crossProduct(prevVertex, curVertex, nextVertex);
                // check if convex
                if ((isCCW ? crossProduct : -crossProduct) < EPSILON) {
                    continue;
                }
                
                boolean isEar = true;

                // check if no other points in triangle
                for (int j = 0; j < vertexIndicesCount; j++) {
                    int checkedVertexIndex = vertexIndices.get(j);
                    if (
                        checkedVertexIndex == prevVertexIndex ||
                        checkedVertexIndex == curVertexIndex ||
                        checkedVertexIndex == nextVertexIndex
                    ) {
                        continue;
                    }

                    Vector checkedVertex = vertices.get(checkedVertexIndex);
                    if (VectorMath.isPointInTriangle(prevVertex, curVertex, nextVertex, checkedVertex)) {
                        isEar = false;
                        break;
                    }
                }

                if (isEar) {
                    triangles.add(new int[] {prevVertexIndex, curVertexIndex, nextVertexIndex});
                    potentialEars.remove(i);
                    potentialEarsCount--;
                    i--;
                    hasClippedEars = true;
                }
            }
        }

        if (triangles.size() != vertexIndicesCount - 2) {
            throw new TriangulationException("Polygon has self-intersections");
        }

        return triangles;
    }

    /**
     * Determines whether the polygon is clockwise or counter-clockwise.
     * <p>Calculates the polygon area using the shoelace formula.
     * The direction is determined by the sign of the area.
     *
     * @param <T> the type of vertices, extending {@link Vector}
     * @param vertices List of vertices implementing {@link Vector}
     * @param vertexIndices vertex indices in order of connection
     * @return true if counter-clockwise
     */
    private static <T extends Vector> boolean isCounterClockwise(List<T> vertices, List<Integer> vertexIndices) {
        float area = 0;
        int vertexIndicesCount = vertexIndices.size();

        Vector prevVertex = vertices.get(vertexIndices.get(0));
        for (int i = 1; i <= vertexIndicesCount; i++) {
            Vector currentVertex = vertices.get(vertexIndices.get(i % vertexIndicesCount));

            area += (currentVertex.x() - prevVertex.x()) * (currentVertex.y() + prevVertex.y());
            prevVertex = currentVertex;
        }

        return area < 0;
    }
}