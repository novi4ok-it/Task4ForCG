package com.cgvsu.math;

import java.util.Objects;

import static com.cgvsu.math.Global.EPS;

public class Vector2f implements Vector<Vector2f> {

	public float x, y;

	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public float x() {
		return x;
	}

	@Override
	public float y() {
		return y;
	}
	public void setX(float x){
		this.x = x;
	}

	public void setY(float y){
		this.y = y;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Vector2f vector2f)) return false;
		return Float.compare(this.x, vector2f.x) == 0 && Float.compare(this.y, vector2f.y) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public void add(Vector2f v) {
		this.x += v.x;
		this.y += v.y;
	}

	public static Vector2f addition(Vector2f v1, Vector2f v2) {
		return new Vector2f(v1.x + v2.x, v1.y + v2.y);
	}

	@Override
	public void sub(Vector2f v) {
		this.x -= v.x;
		this.y -= v.y;
	}

	public static Vector2f subtraction(Vector2f v1, Vector2f v2) {
		return new Vector2f(v1.x - v2.x, v1.y - v2.y);
	}

	@Override
	public void mult(float scalar) {
		this.x *= scalar;
		this.y *= scalar;
	}

	@Override
	public Vector2f multiply(float scalar) {
		return new Vector2f(this.x * scalar, this.y * scalar);
	}

	@Override
	public void div(float scalar) {
		this.x /= scalar;
		this.y /= scalar;
	}


	public Vector2f divide(float scalar) {
		if (scalar < EPS) {
			throw new ArithmeticException("Division by zero is not allowed.");
		}
		return new Vector2f(this.x / scalar, this.y / scalar);
	}



	@Override
	public Vector2f normal() {
		final float length = this.length();
		if (length < EPS) {
			throw new ArithmeticException("Normalization of a zero vector is not allowed.");
		}
		return this.divide(length);
	}

	@Override
	public float length() {
		return (float) Math.sqrt(this.x * this.x + this.y * this.y);
	}

	public String toString() {
		return "Vector2f: x = " + this.x + ", y = " + this.y;
	}

	@Override
	public boolean equals(Vector2f other) {
		return Math.abs(this.x - other.x) < EPS && Math.abs(this.y - other.y) < EPS;
	}
}