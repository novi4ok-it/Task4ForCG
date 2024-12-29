package com.cgvsu.math;

public interface Vector3D<T extends Vector3D<T>> {
    float x();
    float y();
    float z();
    void setX(float x);
    void setY(float y);
    void setZ(float z);
    void add(T v);
    void sub(T v);
    T multiply(float c);
    void mult(float c);
    T divide(float c);
    void div(float c);
    float length();
    T normal();
    boolean equals(T other);
}
