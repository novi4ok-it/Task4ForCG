package com.cgvsu.math;

public interface Vector4D<T extends Vector4D<T>> {
    float x();
    float y();
    float z();
    float w();
    void setX(float x);
    void setY(float y);
    void setZ(float z);
    void setW(float w);
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