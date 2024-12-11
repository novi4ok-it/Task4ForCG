package com.cgvsu.math;

public class Matrix4f {

    float[][] matrix;//мб одномерный

public Matrix4f() {
        matrix = new float[4][4];
    }

    // Конструктор копирования
    public Matrix4f(Matrix4f other) {
        this();
        for (int i = 0; i < 4; i++) {
            System.arraycopy(other.matrix[i], 0, this.matrix[i], 0, 4);
        }
    }

    // Конструктор из массива
    public Matrix4f(float[] values) {
        if (values.length != 16) {
            throw new IllegalArgumentException("Array must have exactly 16 elements.");
        }
        matrix = new float[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                matrix[i][j] = values[i * 4 + j];
            }
        }
    }

    // Умножение матрицы на другую матрицу
    public void mul(Matrix4f other) {
        float[][] result = new float[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i][j] = 0;
                for (int k = 0; k < 4; k++) {
                    result[i][j] += this.matrix[i][k] * other.matrix[k][j];
                }
            }
        }
        this.matrix = result;
    }

    // Метод для получения элемента матрицы
    public float get(int row, int col) {
        return matrix[row][col];
    }

    // Метод для установки элемента матрицы
    public void set(int row, int col, float value) {
        matrix[row][col] = value;
    }

    // Метод для преобразования матрицы в строку (для отладки)
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                sb.append(matrix[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    public Matrix4f(float[][] matrix){
        if (matrix.length != 4 || matrix[0].length != 4) {
            throw new IllegalArgumentException("Matrix must be 4x4");
        }
        this.matrix = matrix;
    }

    public Matrix4f(float numeric) {
        this.matrix = new float[4][4];
        for (int i = 0; i < 4; i++) {
            this.matrix[i][i] = numeric;
        }
    }



    public Matrix4f add(Matrix4f other) {
        float[][] result = new float[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i][j] = this.matrix[i][j] + other.matrix[i][j];
            }
        }
        return new Matrix4f(result);
    }
    public Matrix4f sub(Matrix4f other) {
        float[][] result = new float[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i][j] = this.matrix[i][j] - other.matrix[i][j];
            }
        }
        return new Matrix4f(result);
    }

    public Matrix4f multiply(Matrix4f other) {
        float[][] result = new float[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i][j] = 0;
                for (int k = 0; k < 4; k++) {
                    result[i][j] += this.matrix[i][k] * other.matrix[k][j];
                }
            }
        }
        return new Matrix4f(result);
    }

    public Vector4f multiplyvec(Vector4f vector) {
        return new Vector4f(
                this.matrix[0][0] * vector.x() + this.matrix[0][1] * vector.y() + this.matrix[0][2] * vector.getZ() + this.matrix[0][3] * vector.getW(),
                this.matrix[1][0] * vector.x() + this.matrix[1][1] * vector.y() + this.matrix[1][2] * vector.getZ() + this.matrix[1][3] * vector.getW(),
                this.matrix[2][0] * vector.x() + this.matrix[2][1] * vector.y() + this.matrix[2][2] * vector.getZ() + this.matrix[2][3] * vector.getW(),
                this.matrix[3][0] * vector.x() + this.matrix[3][1] * vector.y() + this.matrix[3][2] * vector.getZ() + this.matrix[3][3] * vector.getW()
        );
    }

    public void transpose(){
        for(int y = 0; y<4; y++) {
            for (int x = y + 1; x < 4; x++) {
                float a = this.matrix[y][x];
                this.matrix[y][x] = this.matrix[x][y];
                this.matrix[x][y] = a;
            }
        }
    }

    public float determinant() {
        float det = 0;
        for (int i = 0; i < 4; i++) {
            float[][] minor = getMinor(0, i);
            Matrix3f minorMatrix = new Matrix3f(minor);
            det += (float) (Math.pow(-1, i) * matrix[0][i] * minorMatrix.determinant());
        }
        return det;
    }

    private float[][] getMinor(int row, int col) {
        float[][] minor = new float[3][3];
        int minorRow = 0;
        for (int i = 0; i < 4; i++) {
            if (i == row) continue;
            int minorCol = 0;
            for (int j = 0; j < 4; j++) {
                if (j == col) continue;
                minor[minorRow][minorCol] = matrix[i][j];
                minorCol++;
            }
            minorRow++;
        }
        return minor;
    }
}