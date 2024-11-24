package com.cgvsu.math;

public class Matrix3f {
    public Matrix3f(float[][] mat){
        if (mat.length != 3 || mat[0].length != 3) {
            throw new IllegalArgumentException("Matrix must be 3x3");
        }
        this.mat = mat;
    }

    public Matrix3f() {
        this.mat = new float[3][3];
    }
    public Matrix3f(float numeric) {
        this.mat = new float[3][3];
        for (int i = 0; i < 3; i++) {
            this.mat[i][i] = numeric;
        }
    }
    float[][] mat;//мб одномерный

    public static Matrix3f add(Matrix3f m1, Matrix3f m2){
        Matrix3f res = new Matrix3f(new float[3][3]);
        for(int y = 0; y<3; y++){
            for(int x = 0; x<3; x++){
                res.mat[y][x] = m1.mat[y][x] + m2.mat[y][x];
            }
        }
        return res;
    }

    public static Matrix3f sub(Matrix3f m1, Matrix3f m2){
        Matrix3f res = new Matrix3f(new float[3][3]);
        for(int y = 0; y<3; y++){
            for(int x = 0; x<3; x++){
                res.mat[y][x] = m1.mat[y][x] - m2.mat[y][x];
            }
        }
        return res;
    }

    public static Matrix3f multiply(Matrix3f m1, Matrix3f m2){
        Matrix3f res = new Matrix3f(new float[3][3]);;
        for (int arr1y = 0; arr1y<3; arr1y++){
            for (int arr2x = 0; arr2x<3; arr2x++){
                float a = 0;
                for (int i = 0; i<3; i++){
                    a+=m1.mat[arr1y][i] * m2.mat[i][arr2x];
                }
                res.mat[arr1y][arr2x] = a;
            }
        }
        return res;
    }

    public void transpose(){
        for(int y = 0; y<3; y++) {
            for (int x = y + 1; x < 3; x++) {
                float a = this.mat[y][x];
                this.mat[y][x] = this.mat[x][y];
                this.mat[x][y] = a;
            }
        }
    }

    public float determinant() {
        return mat[0][0] * (mat[1][1] * mat[2][2] - mat[1][2] * mat[2][1])
                - mat[0][1] * (mat[1][0] * mat[2][2] - mat[1][2] * mat[2][0])
                + mat[0][2] * (mat[1][0] * mat[2][1] - mat[1][1] * mat[2][0]);
    }



}
