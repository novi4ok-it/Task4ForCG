package com.cgvsu.math;

public class AffineTransformations {

    public static Vector3f scale3(float scaleX, float scaleY, float scaleZ, Vector3f v) {
        Matrix3f matt =  new Matrix3f(new float[][] {
                {scaleX, 0, 0},
                {0, scaleY, 0},
                {0, 0, scaleZ}
        });
        return matt.multiplyvec(v);
    }

    public static Vector3f rotateX3(float angle, Vector3f v) {
        float rad = (float) Math.toRadians(angle);
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);
        Matrix3f matt = new Matrix3f(new float[][] {
                {1, 0, 0},
                {0, cos, -sin},
                {0, sin, cos}
        });
        return matt.multiplyvec(v);
    }

    public static Vector3f rotateY3(float angle, Vector3f v) {
        float rad = (float) Math.toRadians(angle);
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);
        Matrix3f matt =  new Matrix3f(new float[][] {
                {cos, 0, sin},
                {0, 1, 0},
                {-sin, 0, cos}
        });
        return matt.multiplyvec(v);
    }

    public static Vector3f rotateZ3(float angle, Vector3f v) {
        float rad = (float) Math.toRadians(angle);
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);
        Matrix3f matt =  new Matrix3f(new float[][] {
                {cos, -sin, 0},
                {sin, cos, 0},
                {0, 0, 1}
        });
        return matt.multiplyvec(v);
    }

    public static Vector4f translate3(float tx, float ty, float tz, Vector4f v) {
        Matrix4f matt = new Matrix4f(new float[][] {
                {1, 0, 0, tx},
                {0, 1, 0, ty},
                {0, 0, 1, tz},
                {0, 0, 0, 1}
        });
        return matt.multiplyvec(v);
    }

    public static Vector4f scale4(float scaleX, float scaleY, float scaleZ, Vector4f v) {
        Matrix4f matt = new Matrix4f(new float[][] {
                {scaleX, 0, 0, 0},
                {0, scaleY, 0, 0},
                {0, 0, scaleZ, 0},
                {0, 0, 0, 1}
        });
        return matt.multiplyvec(v);
    }

    public static Vector4f rotateX4(float angle, Vector4f v) {
        float rad = (float) Math.toRadians(angle);
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);
        Matrix4f matt = new Matrix4f(new float[][] {
                {1, 0, 0, 0},
                {0, cos, -sin, 0},
                {0, sin, cos, 0},
                {0, 0, 0, 1}
        });
        return matt.multiplyvec(v);
    }

    public static Vector4f rotateY4(float angle, Vector4f v) {
        float rad = (float) Math.toRadians(angle);
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);
        Matrix4f matt =  new Matrix4f(new float[][] {
                {cos, 0, sin, 0},
                {0, 1, 0, 0},
                {-sin, 0, cos, 0},
                {0, 0, 0, 1}
        });
        return matt.multiplyvec(v);
    }

    public static Vector4f rotateZ4(float angle, Vector4f v) {
        float rad = (float) Math.toRadians(angle);
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);
        Matrix4f matt =  new Matrix4f(new float[][] {
                {cos, -sin, 0, 0},
                {sin, cos, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
        return matt.multiplyvec(v);
    }
}