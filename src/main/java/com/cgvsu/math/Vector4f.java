package com.cgvsu.math;

import static com.cgvsu.math.Global.EPS;

public class Vector4f implements Vector<Vector4f> {

    public float x, y, z, w;

    public Vector4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }


    @Override
    public float x(){
        return x;
    }
    @Override
    public float y(){
        return y;
    }
    public float getZ(){
        return z;
    }
    public float getW(){
        return w;
    }


    @Override
    public void add(Vector4f v) {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
        this.w += v.w;
    }

    public static Vector4f addition(Vector4f v1, Vector4f v2) {
        return new Vector4f(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z, v1.w + v2.w);
    }


    @Override
    public void sub(Vector4f v) {
        this.x -= v.x;
        this.y -= v.y;
        this.z -= v.z;
        this.w -= v.w;
    }

    public static Vector4f subtraction(Vector4f v1, Vector4f v2) {
        return new Vector4f(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z, v1.w - v2.w);
    }

    @Override
    public void mult(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
        this.w *= scalar;
    }

    @Override
    public Vector4f multiply(float c) {
        return new Vector4f(c * x, c * y, c * z, c * w);
    }

    @Override
    public void div(float scalar) {
        if (scalar < EPS) {
            throw new ArithmeticException("Division by zero is not allowed.");
        }
        this.x /= scalar;
        this.y /= scalar;
        this.z /= scalar;
        this.w /= scalar;
    }

    @Override
    public Vector4f divide(float scalar) {
        if (scalar < EPS) {
            throw new ArithmeticException("Division by zero is not allowed.");
        }
        return new Vector4f(this.x / scalar, this.y / scalar, this.z / scalar, this.w / scalar);
    }


    @Override
    public float length() {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w);
    }

    @Override
    public Vector4f normal() {
        final float length = this.length();
        if (length < EPS) {
            throw new ArithmeticException("Normalization of a zero vector is not allowed.");
        }
        return this.divide(length);
    }

    public static float dotProduct(Vector4f v1, Vector4f v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z + v1.w * v2.w;
    }

    @Override
    public boolean equals(Vector4f other) {
        return Math.abs(x - other.x) < EPS
                && Math.abs(y - other.y) < EPS
                && Math.abs(z - other.z) < EPS
                && Math.abs(w - other.w) < EPS;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
