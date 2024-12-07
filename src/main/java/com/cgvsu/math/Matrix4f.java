package com.cgvsu.math;

public class Matrix4f {

    float[][] mat;//мб одномерный


    public Matrix4f(float[][] mat){
        if (mat.length != 4 || mat[0].length != 4) {
            throw new IllegalArgumentException("Matrix must be 4x4");
        }
        this.mat = mat;
    }

    public Matrix4f(float numeric) {
        this.mat = new float[4][4];
        for (int i = 0; i < 4; i++) {
            this.mat[i][i] = numeric;
        }
    }



    public Matrix4f add(Matrix4f other) {
        float[][] result = new float[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i][j] = this.mat[i][j] + other.mat[i][j];
            }
        }
        return new Matrix4f(result);
    }
    public Matrix4f sub(Matrix4f other) {
        float[][] result = new float[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i][j] = this.mat[i][j] - other.mat[i][j];
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
                    result[i][j] += this.mat[i][k] * other.mat[k][j];
                }
            }
        }
        return new Matrix4f(result);
    }

    public Vector4f multiplyvec(Vector4f vector) {
        return new Vector4f(
                this.mat[0][0] * vector.x() + this.mat[0][1] * vector.y() + this.mat[0][2] * vector.getZ() + this.mat[0][3] * vector.getW(),
                this.mat[1][0] * vector.x() + this.mat[1][1] * vector.y() + this.mat[1][2] * vector.getZ() + this.mat[1][3] * vector.getW(),
                this.mat[2][0] * vector.x() + this.mat[2][1] * vector.y() + this.mat[2][2] * vector.getZ() + this.mat[2][3] * vector.getW(),
                this.mat[3][0] * vector.x() + this.mat[3][1] * vector.y() + this.mat[3][2] * vector.getZ() + this.mat[3][3] * vector.getW()
        );
    }

    public void transpose(){
        for(int y = 0; y<4; y++) {
            for (int x = y + 1; x < 4; x++) {
                float a = this.mat[y][x];
                this.mat[y][x] = this.mat[x][y];
                this.mat[x][y] = a;
            }
        }
    }

    public float determinant() {
        float det = 0;
        for (int i = 0; i < 4; i++) {
            float[][] minor = getMinor(0, i);
            Matrix3f minorMatrix = new Matrix3f(minor);
            det += (float) (Math.pow(-1, i) * mat[0][i] * minorMatrix.determinant());
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
                minor[minorRow][minorCol] = mat[i][j];
                minorCol++;
            }
            minorRow++;
        }
        return minor;
    }
}