package com.cgvsu.math;

import java.util.Objects;

public class Point2f {
    public float x;
    public float y;

    public Point2f(float var1, float var2) {
        this.x = var1;
        this.y = var2;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Point2f point2f = (Point2f) obj;
        return Float.compare(point2f.x, x) == 0 && Float.compare(point2f.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
