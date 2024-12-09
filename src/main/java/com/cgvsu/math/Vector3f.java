package com.cgvsu.math;

import java.util.Objects;

import static com.cgvsu.math.Global.EPS;

public class Vector3f implements Vector<Vector3f> {

    public float x;
    public float y;
    public float z;

    public Vector3f() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vector3f addition(Vector3f v1, Vector3f v2) {
        return new Vector3f(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
    }

    public Vector3f addi(Vector3f other) {
    return new Vector3f(this.x + other.x, this.y + other.y, this.z + other.z);
}
    public float x() {
        return x;
    }

    public void x(float x) {
        this.x = x;
    }

    public float y() {
        return y;
    }

    public void y(float y) {
        this.y = y;
    }

    public float z() {
        return z;
    }

    public void z(float z) {
        this.z = z;
    }

    public Vector3f cross(Vector3f other) {
        return new Vector3f(
                this.y * other.z - this.z * other.y,
                this.z * other.x - this.x * other.z,
                this.x * other.y - this.y * other.x
        );
    }

    public void crossInPlace(Vector3f other) {
        float newX = this.y * other.z - this.z * other.y;
        float newY = this.z * other.x - this.x * other.z;
        float newZ = this.x * other.y - this.y * other.x;
        this.x = newX;
        this.y = newY;
        this.z = newZ;
    }
    public static Vector3f subtraction(Vector3f v1, Vector3f v2) {
    if (v1 == null || v2 == null) {
        throw new IllegalArgumentException("Both vectors must be non-null.");
    }
    return new Vector3f(
        v1.x - v2.x,
        v1.y - v2.y,
        v1.z - v2.z
    );
}

    public float dot(Vector3f other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public final void normalize() {
        float var1 = (float)(1.0 / Math.sqrt((double)(this.x * this.x + this.y * this.y + this.z * this.z)));
        this.x *= var1;
        this.y *= var1;
        this.z *= var1;
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    @Override
    public Vector3f normal() {
        return null;
    }

    @Override
    public boolean equals(Vector3f other) {
        return false;
    }

    public void add(Vector3f vec) {
        this.x += vec.x;
        this.y += vec.y;
        this.z += vec.z;
    }

    public void sub(Vector3f vec) {
        this.x -= vec.x;
        this.y -= vec.y;
        this.z -= vec.z;
    }

    @Override
    public Vector3f multiply(float scalar) {
        return new Vector3f(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    @Override
    public void mult(float c) {

    }

    @Override
    public Vector3f divide(float c) {
        return null;
    }

    @Override
    public void div(float c) {

    }

    public void scale(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
    }
}