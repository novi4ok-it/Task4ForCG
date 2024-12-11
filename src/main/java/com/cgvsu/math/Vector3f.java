package com.cgvsu.math;

import java.util.Objects;

import static com.cgvsu.math.Global.EPS;

public class Vector3f implements Vector<Vector3f> {
    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vector3f() {
    }

    public float x, y, z;

    @Override
    public float x() {
        return x;
    }
    @Override
    public float y() {
        return y;
    }

    public float z() {
        return z;
    }

    public void setX(float x){
        this.x = x;
    }

    public void setY(float y){
        this.y = y;
    }
    public void setZ(float z){
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector3f vector3f)) return false;
        return Float.compare(this.x, vector3f.x) == 0 && Float.compare(this.y, vector3f.y) == 0 && Float.compare(this.z, vector3f.z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

//    public void add(Vector3f vec) {
//        this.x += vec.x;
//        this.y += vec.y;
//        this.z += vec.z;
//    }
    @Override
    public final void add(Vector3f var1) {
        this.x += var1.x;
        this.y += var1.y;
        this.z += var1.z;
    }

    public static Vector3f addition(Vector3f v1, Vector3f v2) {
        return new Vector3f(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
    }


    @Override
    public void sub(Vector3f vec) {
        this.x -= vec.x;
        this.y -= vec.y;
        this.z -= vec.z;
    }

    public final void sub(Vector3f var1, Vector3f var2) {
        this.x = var1.x - var2.x;
        this.y = var1.y - var2.y;
        this.z = var1.z - var2.z;
    }

    public static Vector3f subtraction(Vector3f v1, Vector3f v2) {
        return new Vector3f(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }
    public final float dot(Vector3f var1) {
        return this.x * var1.x + this.y * var1.y + this.z * var1.z;
    }

    @Override
    public void mult(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
    }


    @Override
    public Vector3f multiply(float scalar) {
        return new Vector3f(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    @Override
    public void div(float scalar) {
        if (scalar < EPS) {
            throw new ArithmeticException("Division by zero is not allowed.");
        }
        this.x /= scalar;
        this.y /= scalar;
        this.z /= scalar;
    }

    @Override
    public Vector3f divide(float scalar) {
        if (scalar < EPS) {
            throw new ArithmeticException("Division by zero is not allowed.");
        }
        return new Vector3f(this.x / scalar, this.y / scalar, this.z / scalar);
    }


    @Override
    public float length() {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    @Override
    public Vector3f normal() {
        final float length = this.length();
        if (length < EPS) {
            throw new ArithmeticException("Normalization of a zero vector is not allowed.");
        }
//        float invLength = 1 / length;
//        this.mult(invLength);
//        return this;
        return this.divide(length);
    }

    public final void normalize() {
        float var1 = (float)(1.0 / Math.sqrt((double)(this.x * this.x + this.y * this.y + this.z * this.z)));
        this.x *= var1;
        this.y *= var1;
        this.z *= var1;
    }

    public final void cross(Vector3f var1, Vector3f var2) {
        float var3 = var1.y * var2.z - var1.z * var2.y;
        float var4 = var2.x * var1.z - var2.z * var1.x;
        this.z = var1.x * var2.y - var1.y * var2.x;
        this.x = var3;
        this.y = var4;
    }
    public Vector3f cross(Vector3f other) {
        return new Vector3f(
                this.y * other.z - this.z * other.y,
                this.z * other.x - this.x * other.z,
                this.x * other.y - this.y * other.x
        );
    }

    public static Vector3f crossProduct(Vector3f v1, Vector3f v2) {
        return new Vector3f(
                v1.y * v2.z - v1.z * v2.y,
                v1.z * v2.x - v1.x * v2.z,
                v1.x * v2.y - v1.y * v2.x
        );
    }

    public static float dotProduct(Vector3f v1, Vector3f v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }

    @Override
    public boolean equals(Vector3f other) {
        return Math.abs(x - other.x) < EPS
                && Math.abs(y - other.y) < EPS
                && Math.abs(z - other.z) < EPS;
    }
}
