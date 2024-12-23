package com.cgvsu.math;
import com.cgvsu.model.Model;

import static com.cgvsu.model.Model.scale;
import static com.cgvsu.model.Model.rotation;
import static com.cgvsu.model.Model.translation;

public class AffineTransformations {





    public static void applyScaleX() {
        for (Vector3f vertex : Model.getVertices()) {
            scale3(scale.x(), scale.y(), scale.z(), vertex);
        }
    }

    public static void applyScaleY() {
        for (Vector3f vertex : Model.getVertices()){
            scale3(scale.x(), scale.y(), scale.z(), vertex);
        }
    }

    public static void applyScaleZ() {
        for (Vector3f vertex : Model.getVertices()){
            scale3(scale.x(), scale.y(), scale.z(), vertex);
        }
    }

    public static void applyRotationX(){
        for (Vector3f vertex : Model.getVertices()) {
            rotateX3(rotation.x(), vertex);
        }
    }

    public static void applyRotationY(){
        for (Vector3f vertex : Model.getVertices()) {
            rotateY3(rotation.y(), vertex);
        }
    }
    public static void applyRotationZ(){
        for (Vector3f vertex : Model.getVertices()) {
            rotateZ3(rotation.z(), vertex);
        }
    }

    public static void applyTranslationX(){
        for (Vector3f vertex : Model.getVertices()){
            vertex.setX(vertex.x() + translation.x());
//            translate3(translation.x(), translation.y(), translation.z(), vertex);
        }
    }

    public static void applyTranslationY(){
        for (Vector3f vertex : Model.getVertices()){
            vertex.setY(vertex.y() + translation.y());
//            translate3(translation.x(), translation.y(), translation.z(), vertex);
        }
    }
    public static void applyTranslationZ(){
        for (Vector3f vertex : Model.getVertices()){
            vertex.setZ(vertex.z() + translation.z());
//            translate3(translation.x(), translation.y(), translation.z(), vertex);
        }
    }

    public static void scale3(float scaleX, float scaleY, float scaleZ, Vector3f v) {
        Matrix3f matt =  new Matrix3f(new float[][] {
                {scaleX, 0, 0},
                {0, scaleY, 0},
                {0, 0, scaleZ}
        });
        matt.multiplyvec(v);
    }

    public static void rotateX3(float angle, Vector3f v) {
        float rad = (float) Math.toRadians(angle);
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);
        Matrix3f matt = new Matrix3f(new float[][] {
                {1, 0, 0},
                {0, cos, -sin},
                {0, sin, cos}
        });
        matt.multiplyvec(v);
    }

    public static void rotateY3(float angle, Vector3f v) {
        float rad = (float) Math.toRadians(angle);
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);
        Matrix3f matt =  new Matrix3f(new float[][] {
                {cos, 0, sin},
                {0, 1, 0},
                {-sin, 0, cos}
        });
        matt.multiplyvec(v);
    }

    public static void rotateZ3(float angle, Vector3f v) {
        float rad = (float) Math.toRadians(angle);
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);
        Matrix3f matt =  new Matrix3f(new float[][] {
                {cos, -sin, 0},
                {sin, cos, 0},
                {0, 0, 1}
        });
        matt.multiplyvec(v);
    }

    public static void translate3(float tx, float ty, float tz, Vector3f v) {
        Vector4f vec = new Vector4f(v.x(), v.y(), v.z(), 1);
        Matrix4f matt = new Matrix4f(new float[][] {
                {1, 0, 0, tx},
                {0, 1, 0, ty},
                {0, 0, 1, tz},
                {0, 0, 0, 1}
        });
        matt.multiplyvec(vec);
    }

    public static void scale4(float scaleX, float scaleY, float scaleZ, Vector4f v) {
        Matrix4f matt = new Matrix4f(new float[][] {
                {scaleX, 0, 0, 0},
                {0, scaleY, 0, 0},
                {0, 0, scaleZ, 0},
                {0, 0, 0, 1}
        });
        matt.multiplyvec(v);
    }

    public static void rotateX4(float angle, Vector4f v) {
        float rad = (float) Math.toRadians(angle);
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);
        Matrix4f matt = new Matrix4f(new float[][] {
                {1, 0, 0, 0},
                {0, cos, -sin, 0},
                {0, sin, cos, 0},
                {0, 0, 0, 1}
        });
        matt.multiplyvec(v);
    }

    public static void rotateY4(float angle, Vector4f v) {
        float rad = (float) Math.toRadians(angle);
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);
        Matrix4f matt =  new Matrix4f(new float[][] {
                {cos, 0, sin, 0},
                {0, 1, 0, 0},
                {-sin, 0, cos, 0},
                {0, 0, 0, 1}
        });
        matt.multiplyvec(v);
    }

    public static void rotateZ4(float angle, Vector4f v) {
        float rad = (float) Math.toRadians(angle);
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);
        Matrix4f matt =  new Matrix4f(new float[][] {
                {cos, -sin, 0, 0},
                {sin, cos, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
        matt.multiplyvec(v);
    }
}