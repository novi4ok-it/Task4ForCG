package com.cgvsu.math;

import com.cgvsu.normal.FindNormals;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MathTest {
    @Test
	void normalizeTest1() {
		assertEquals(Math.round(FindNormals.normalize(new Vector3f(0.33f, 0.33f, 0.33f)).x * 100), 58);
	}

	@Test
	void normalizeTest2() {
		assertEquals(Math.round(FindNormals.normalize(new Vector3f(1, 1, 1)).x * 100), 58);
	}

	@Test
	void normalizeTest3() {
		assertEquals(Math.round(FindNormals.normalize(new Vector3f(2, 2, 2)).x * 100), 58);
	}

	@Test
	void normalizeTest4() {
		assertEquals(FindNormals.normalize(new Vector3f(0, 0, 0)).x, 0);
	}

	@Test
	void normalizeTest5() {
		assertEquals(FindNormals.normalize(null), null);
	}

	@Test
	void determinantTest1() {
		assertEquals(FindNormals.determinant(new Vector3f(1, 1, 1), new Vector3f(1, 1, 1), new Vector3f(1, 1, 1)), 1.0);
	}

	@Test
	void determinantTest2() {
		assertEquals(FindNormals.determinant(new Vector3f(1, 1, 1), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1)), 0.0);
	}

	@Test
	void determinantTest3() {
		assertEquals(FindNormals.determinant(new Vector3f(1, 1, 1), new Vector3f(1, 1, 1), new Vector3f(0, 0, 0)), 0.0);
	}

	@Test
	void determinantTest4() {
		Vector3f a = new Vector3f(1, 0, 0);
		Vector3f b = new Vector3f(0, -1, 0);
		assertEquals(FindNormals.determinant(a, b, FindNormals.vectorProduct(a, b)), 1.0);
		assertEquals(FindNormals.determinant(b, a, FindNormals.vectorProduct(a, b)), -1.0);
	}

	@Test
	void vectorProductTest1() {
		assertEquals(FindNormals.vectorProduct(new Vector3f(1, 1, 1), new Vector3f(0, 0, 0)).x, 0.0);
		assertEquals(FindNormals.vectorProduct(new Vector3f(1, 1, 1), new Vector3f(0, 0, 0)).y, 0.0);
		assertEquals(FindNormals.vectorProduct(new Vector3f(1, 1, 1), new Vector3f(0, 0, 0)).z, 0.0);
	}

	@Test
	void vectorProductTest2() {
		assertEquals(FindNormals.vectorProduct(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0)).x, 0.0);
		assertEquals(FindNormals.vectorProduct(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0)).y, 0.0);
		assertEquals(FindNormals.vectorProduct(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0)).z, 0.0);
	}

	@Test
	void vectorProductTest3() {
		assertEquals(FindNormals.vectorProduct(new Vector3f(1, 21, 17), new Vector3f(3, 0, -8)).x, -168.0);
		assertEquals(FindNormals.vectorProduct(new Vector3f(1, 21, 17), new Vector3f(3, 0, -8)).y, 59.0);
		assertEquals(FindNormals.vectorProduct(new Vector3f(1, 21, 17), new Vector3f(3, 0, -8)).z, -63.0);
	}
}
