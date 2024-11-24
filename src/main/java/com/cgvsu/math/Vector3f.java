package com.cgvsu.math;

import java.util.Objects;

import static com.cgvsu.math.Global.EPS;

public class Vector3f implements Vector<Vector3f> {
    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float x, y, z;
    public float x() {
        return x;
    }

    public float y() {
        return y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector3f vector3f)) return false;
        return Float.compare(x, vector3f.x) == 0 && Float.compare(y, vector3f.y) == 0 && Float.compare(z, vector3f.z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    public static Vector3f addition(Vector3f v1, Vector3f v2) {
        return new Vector3f(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
    }

    @Override
    public void add(Vector3f v) {
        x += v.x;
        y += v.y;
        z += v.z;
    }

    public static Vector3f subtraction(Vector3f v1, Vector3f v2) {
        return new Vector3f(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }

    @Override
    public void sub(Vector3f v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
    }

    @Override
    public Vector3f multiply(float c) {
        return new Vector3f(c * x, c * y, c * z);
    }

    @Override
    public void mult(float c) {
        x *= c;
        y *= c;
        z *= c;
    }

    @Override
    public Vector3f divide(float c) {
        if (c < EPS) {
            throw new ArithmeticException("Division by zero is not allowed.");
        }
        return new Vector3f(x / c, y / c, z / c);
    }

    @Override
    public void div(float c) {
        if (c < EPS) {
            throw new ArithmeticException("Division by zero is not allowed.");
        }
        x /= c;
        y /= c;
        z /= c;
    }

    @Override
    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    @Override
    public Vector3f normal() {
        final float length = this.length();
        if (length < EPS) {
            throw new ArithmeticException("Normalization of a zero vector is not allowed.");
        }
        float invLength = 1 / length;
        this.mult(invLength);
        return this;
    }

    public static float dotProduct(Vector3f v1, Vector3f v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }

    public static Vector3f crossProduct(Vector3f v1, Vector3f v2) {
        final float x = v1.y * v2.z - v1.z * v2.y;
        final float y = v1.z * v2.x - v1.x * v2.z;
        final float z = v1.x * v2.y - v1.y * v2.x;
        return new Vector3f(x, y, z);
    }

    @Override
    public boolean equals(Vector3f other) {
        return Math.abs(x - other.x) < EPS
                && Math.abs(y - other.y) < EPS
                && Math.abs(z - other.z) < EPS;
    }
}
