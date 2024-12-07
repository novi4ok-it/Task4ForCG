package com.cgvsu.math;

import java.util.Objects;

import static com.cgvsu.math.Global.EPS;

public class Vector2f implements Vector<Vector2f> {
    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }
    float x, y;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector2f vector2f)) return false;
        return Float.compare(x, vector2f.x) == 0 && Float.compare(y, vector2f.y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
    public static Vector2f addition(Vector2f v1, Vector2f v2) {
		return new Vector2f(v1.x + v2.x, v1.y + v2.y);
	}

	@Override
	public void add(Vector2f v) {
		x += v.x;
		y += v.y;
	}

	public static Vector2f subtraction(Vector2f v1, Vector2f v2) {
		return new Vector2f(v1.x - v2.x, v1.y - v2.y);
	}

	@Override
	public void sub(Vector2f v) {
		x -= v.x;
		y -= v.y;
	}

	@Override
	public Vector2f multiply(float c) {
		return new Vector2f(c * x, c * y);
	}

	@Override
	public void mult(float c) {
		x *= c;
		y *= c;
	}

	public Vector2f divide(float c) {
		if (c < EPS) {
			throw new ArithmeticException("Division by zero is not allowed.");
		}
		return new Vector2f(x / c, y / c);
	}

	@Override
	public void div(float c) {
		x /= c;
		y /= c;
	}

	@Override
	public float length() {
		return (float) Math.sqrt(x * x + y * y);
	}

	@Override
	public Vector2f normal() {
		final float length = this.length();
		if (length < EPS) {
			throw new ArithmeticException("Normalization of a zero vector is not allowed.");
		}
		float invLength = 1 / length;
		return this.multiply(invLength);
	}

	public static float dotProduct(Vector2f v1, Vector2f v2) {
		return v1.x * v2.x + v1.y * v2.y;
	}

	@Override
	public boolean equals(Vector2f other) {
		return Math.abs(x - other.x) < EPS && Math.abs(y - other.y) < EPS;
	}
}
