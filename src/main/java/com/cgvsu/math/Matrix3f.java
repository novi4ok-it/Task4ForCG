package com.cgvsu.math;

public class Matrix3f {

    public float[][] mat;//мб одномерный

    public Matrix3f(float[][] mat){
        if (mat.length != 3 || mat[0].length != 3) {
            throw new IllegalArgumentException("Matrix must be 3x3");
        }
        this.mat = mat;
    }

    public Matrix3f(float numeric) {
        this.mat = new float[3][3];
        for (int i = 0; i < 3; i++) {
            this.mat[i][i] = numeric;
        }
    }


    public Matrix3f add(Matrix3f other) {
        float[][] result = new float[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result[i][j] = this.mat[i][j] + other.mat[i][j];
            }
        }
        return new Matrix3f(result);
    }


    public Matrix3f sub(Matrix3f other) {
        float[][] result = new float[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result[i][j] = this.mat[i][j] - other.mat[i][j];
            }
        }
        return new Matrix3f(result);
    }

    public Matrix3f multiply(Matrix3f other) {
        float[][] result = new float[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result[i][j] = 0;
                for (int k = 0; k < 3; k++) {
                    result[i][j] += this.mat[i][k] * other.mat[k][j];
                }
            }
        }
        return new Matrix3f(result);
    }

    public void multiplyvec(Vector3f vector) {
        vector.setX(this.mat[0][0] * vector.x() + this.mat[0][1] * vector.y() + this.mat[0][2] * vector.z());
        vector.setY(this.mat[1][0] * vector.x() + this.mat[1][1] * vector.y() + this.mat[1][2] * vector.z());
        vector.setZ(this.mat[2][0] * vector.x() + this.mat[2][1] * vector.y() + this.mat[2][2] * vector.z());
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