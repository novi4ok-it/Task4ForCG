package com.cgvsu.math;

public class Matrix4f {

    private final float[] mat; // Одномерный массив для хранения элементов матрицы.

    public Matrix4f() {
        this.mat = new float[16];
        for (int i = 0; i < 16; i++) {
            mat[i] = (i % 5 == 0) ? 1.0f : 0.0f; // Единичная матрица.
        }
    }

    public Matrix4f(float[] mat) {
        if (mat.length != 16) {
            throw new IllegalArgumentException("Matrix must contain exactly 16 elements.");
        }
        this.mat = mat.clone();
    }

    public float get(int row, int col) {
        return mat[row * 4 + col];
    }

    public void set(int row, int col, float value) {
        mat[row * 4 + col] = value;
    }

    public Matrix4f add(Matrix4f other) {
        float[] result = new float[16];
        for (int i = 0; i < 16; i++) {
            result[i] = this.mat[i] + other.mat[i];
        }
        return new Matrix4f(result);
    }

    public Matrix4f sub(Matrix4f other) {
        float[] result = new float[16];
        for (int i = 0; i < 16; i++) {
            result[i] = this.mat[i] - other.mat[i];
        }
        return new Matrix4f(result);
    }

    public Matrix4f multiply(Matrix4f other) {
        float[] result = new float[16];
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                result[row * 4 + col] = 0;
                for (int k = 0; k < 4; k++) {
                    result[row * 4 + col] += this.get(row, k) * other.get(k, col);
                }
            }
        }
        return new Matrix4f(result);
    }

    public void transpose() {
        for (int row = 0; row < 4; row++) {
            for (int col = row + 1; col < 4; col++) {
                float temp = this.get(row, col);
                this.set(row, col, this.get(col, row));
                this.set(col, row, temp);
            }
        }
    }

    public float determinant() {
        // Используем метод для 4x4 матрицы
        return 0; // Оставлено для расширения.
    }
}