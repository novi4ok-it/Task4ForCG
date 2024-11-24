package com.cgvsu.math;
public class Matrix4f {
    public Matrix4f(float[][] mat){
        if (mat.length != 4 || mat[0].length != 4) {
            throw new IllegalArgumentException("Matrix must be 4x4");
        }
        this.mat = mat;
    }

    public Matrix4f() {
        this.mat = new float[4][4];
    }
    public Matrix4f(float numeric) {
        this.mat = new float[4][4];
        for (int i = 0; i < 4; i++) {
            this.mat[i][i] = numeric;
        }
    }
    float[][] mat;//мб одномерный

    public static Matrix4f add(Matrix4f m1, Matrix4f m2){
        Matrix4f res = new Matrix4f(new float[4][4]);
        for(int y = 0; y<4; y++){
            for(int x = 0; x<4; x++){
                res.mat[y][x] = m1.mat[y][x] + m2.mat[y][x];
            }
        }
        return res;
    }

    public static Matrix4f sub(Matrix4f m1, Matrix4f m2){
        Matrix4f res = new Matrix4f(new float[4][4]);
        for(int y = 0; y<4; y++){
            for(int x = 0; x<4; x++){
                res.mat[y][x] = m1.mat[y][x] - m2.mat[y][x];
            }
        }
        return res;
    }

    public static Matrix4f multiply(Matrix4f m1, Matrix4f m2){
        Matrix4f res = new Matrix4f(new float[4][4]);;
        for (int m1y = 0; m1y<4; m1y++){
            for (int m2x = 0; m2x<4; m2x++){
                float a = 0;
                for (int i = 0; i<4; i++){
                    a+=m1.mat[m1y][i] * m2.mat[i][m2x];
                }
                res.mat[m1y][m2x] = a;
            }
        }
        return res;
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
