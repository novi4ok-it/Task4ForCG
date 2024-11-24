package com.cgvsu.math;

/**
 * A utility class for working with {@link Vector}
 */
public final class VectorMath {

    /**
     * A small number for float and double comparisons
     */
    public static final double EPSILON = 0.000000001;

    /**
     * Prevents class instantiation.
     * @throws UnsupportedOperationException when called
     */
    private VectorMath() {
        throw new UnsupportedOperationException("Cannot be instantiated.");
    }

    /**
     * Calculates the cross product of vectors (BA) x (BC).
     * @param a A coordinates
     * @param b B coordinates
     * @param c C coordinates
     * @return cross product of vectors (BA) x (BC)
     */
    public static float crossProduct(Vector a, Vector b, Vector c) {
        float dx1 = b.x() - a.x();
        float dy1 = b.y() - a.y();
        float dx2 = c.x() - a.x();
        float dy2 = c.y() - a.y();

        return dx1 * dy2 - dx2 * dy1;
    }

    /**
     * Checks whether point P is inside of triangle ABC.
     * <p>Uses the cross product of three vectors.
     * @param a A coordinates
     * @param b B coordinates
     * @param c C coordinates
     * @param p P coordinates
     * @return true if P is inside ABC
     */
    public static boolean isPointInTriangle(Vector a, Vector b, Vector c, Vector p) {
        float check1 = crossProduct(a, b, p);
        float check2 = crossProduct(p, b, c);
        float check3 = crossProduct(p, c, a);

        return (check1 >= -EPSILON && check2 >= -EPSILON && check3 >= -EPSILON) ||
         (check1 <= EPSILON && check2 <= EPSILON && check3 <= EPSILON);
    }

    /**
     * Calculates the length of vector AB.
     * @param a A coordinates
     * @param b B coordinates
     * @return length of vector AB
     */
    public static float edgeLength(Vector a, Vector b) {
        float dx = a.x() - b.x();
        float dy = a.y() - b.y();
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
}
