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

    public float dot(Vector3f other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public void normalize() {
        float len = length();
        if (len < EPS) {
            throw new ArithmeticException("Cannot normalize a zero-length vector.");
        }
        this.x /= len;
        this.y /= len;
        this.z /= len;
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
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

    public void scale(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
    }
}