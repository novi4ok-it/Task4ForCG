package com.cgvsu.math;

public class Matrix4f {

    float[][] matrix;

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

    public Matrix4f(float[][] matrix) {
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

    public static Matrix4f multiplymatt(Matrix4f first, Matrix4f second) {
        if (first == null || second == null) {
            throw new NullPointerException("Матрица не может быть нулевая");
        }
        float[][] result = new float[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i][j] = 0;
                for (int k = 0; k < 4; k++) {
                    result[i][j] += first.matrix[i][k] * second.matrix[k][j];
                }
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

    //Умножение матрицы на вектор
    public void multiplyvec(Vector4f vector) {
        vector.setX(this.matrix[0][0] * vector.x() + this.matrix[0][1] * vector.y() + this.matrix[0][2] * vector.z() + this.matrix[0][3] * vector.w());
        vector.setY(this.matrix[1][0] * vector.x() + this.matrix[1][1] * vector.y() + this.matrix[1][2] * vector.z() + this.matrix[1][3] * vector.w());
        vector.setZ(this.matrix[2][0] * vector.x() + this.matrix[2][1] * vector.y() + this.matrix[2][2] * vector.z() + this.matrix[2][3] * vector.w());
        vector.setW(this.matrix[3][0] * vector.x() + this.matrix[3][1] * vector.y() + this.matrix[3][2] * vector.z() + this.matrix[3][3] * vector.w());
    }

    public void transpose() {
        for (int y = 0; y < 4; y++) {
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
    public static Matrix4f scaleMatrix4f(float scaleX, float scaleY, float scaleZ) {
        float[][] matrix = new float[][]
                {
                        {scaleX, 0,0,0},
                        {0, scaleY,0,0},
                        {0,0,scaleZ,0},
                        {0,0,0,1}
                };
        return new Matrix4f(matrix);
    }

    public static Matrix4f rotateX(float angleX) {
        float cos = (float) Math.cos(Math.toRadians(angleX));
        float sin = (float) Math.sin(Math.toRadians(angleX));
        float[][] matrix = new float[][]
                {
                        {1,0,0,0},
                        {0,cos, sin,0},
                        {0,-sin, cos, 0},
                        {0,0,0,1}
                };
        return new Matrix4f(matrix);
    }

    public static Matrix4f rotateY(float angleY) {
        float cos = (float) Math.cos(Math.toRadians(angleY));
        float sin = (float) Math.sin(Math.toRadians(angleY));
        float[][] matrix = new float[][]
                {
                        {cos,0,sin,0},
                        {0,1,0,0},
                        {-sin,0,cos,0},
                        {0,0,0,1}
                };
        return new Matrix4f(matrix);
    }

    public static Matrix4f rotateZ(float angleZ) {
        float cos = (float) Math.cos(Math.toRadians(angleZ));
        float sin = (float) Math.sin(Math.toRadians(angleZ));
        float[][] matrix = new float[][]
                {
                        {cos,sin,0,0},
                        {-sin, cos,0,0},
                        {0,0,1,0},
                        {0,0,0,1}
                };
        return new Matrix4f(matrix);
    }

    public static Matrix4f rotateMatrix4f(float angleX, float angleY, float angleZ) {
        return Matrix4f.multiplymatt(rotateZ(angleZ), Matrix4f.multiplymatt(rotateY(angleY), rotateX(angleX)));
    }

    public static Matrix4f translationMatrix4f(float translationX, float translationY, float translationZ) {
        float[][] matrix = new float[][]
                {
                        {1,0,0,translationX},
                        {0,1,0,translationY},
                        {0,0,1,translationZ},
                        {0,0,0,1}
                };
        return new Matrix4f(matrix);
    }
}