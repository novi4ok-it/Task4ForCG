package com.cgvsu.math;
import com.cgvsu.model.Model;
import com.cgvsu.render_engine.GraphicConveyor;
import com.cgvsu.math.Matrix4f;
//import static com.cgvsu.model.Model.scale;
//import static com.cgvsu.model.Model.rotation;
//import static com.cgvsu.model.Model.translation;

public class AffineTransformations {
    public static void applyScale(Model model) {
        for (Vector3f vertex : model.vertices) {
            scale3(model.scale.x(), model.scale.y(), model.scale.z(), vertex);
        }
    }
    public static void applyRotationX(Model model){
        for (Vector3f vertex : model.vertices) {
//            rotateX3(model.rotation.x(), vertex);
            rotate3(model.rotation.x(), model.rotation.y(), model.rotation.z(), vertex);
        }
    }

    public static void applyRotationY(Model model){
        for (Vector3f vertex : model.vertices) {
            rotateY3(model.rotation.y(), vertex);
        }
    }
    public static void applyRotationZ(Model model){
        for (Vector3f vertex : model.vertices) {
            rotateZ3(model.rotation.z(), vertex);
        }
    }



//
    public static void applyTranslationX(Model model){
        for (Vector3f vertex : model.vertices){
            Vector4f vec = new Vector4f(vertex.x(), vertex.y(), vertex.z(), 1);
            translate3(model.translation.x(), model.translation.y(), model.translation.z(), vec);
            vertex.setX(vec.x());
            vertex.setY(vec.y());
            vertex.setZ(vec.z());
//            translate3(translation.x(), translation.y(), translation.z(), vertex);
        }
    }
//
//    public static void applyTranslationY(){
//        for (Vector3f vertex : Model.getVertices()){
//            vertex.setY(vertex.y() + translation.y());
////            translate3(translation.x(), translation.y(), translation.z(), vertex);
//        }
//    }
//    public static void applyTranslationZ(){
//        for (Vector3f vertex : Model.getVertices()){
//            vertex.setZ(vertex.z() + translation.z());
////            translate3(translation.x(), translation.y(), translation.z(), vertex);
//        }
//    }
//
    public static void scale3(float scaleX, float scaleY, float scaleZ, Vector3f v) {
        Matrix3f matt =  new Matrix3f(new float[][] {
                {scaleX, 0, 0},
                {0, scaleY, 0},
                {0, 0, scaleZ}
        });
        matt.multiplyvec(v);
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
//
    public static void rotateX3(float angle, Vector3f v) {
        float rad = (float) Math.toRadians(angle);
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);
        Matrix3f matt = new Matrix3f(new float[][] {
                {1, 0, 0},
                {0, cos, sin},
                {0, -sin, cos}
        });
        matt.multiplyvec(v);
    }
//
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
                {cos, sin, 0},
                {-sin, cos, 0},
                {0, 0, 1}
        });
        matt.multiplyvec(v);
    }
    public static void rotate3(float x, float y, float z, Vector3f vec){
        float radx = (float) Math.toRadians(x);
        float cosx = (float) Math.cos(radx);
        float sinx = (float) Math.sin(radx);
        Matrix3f mattx = new Matrix3f(new float[][] {
                {1, 0, 0},
                {0, cosx, sinx},
                {0, -sinx, cosx}
        });
        float rady = (float) Math.toRadians(y);
        float cosy = (float) Math.cos(rady);
        float siny = (float) Math.sin(rady);
        Matrix3f matty =  new Matrix3f(new float[][] {
                {cosy, 0, siny},
                {0, 1, 0},
                {-siny, 0, cosy}
        });
        float radz = (float) Math.toRadians(z);
        float cosz = (float) Math.cos(radz);
        float sinz = (float) Math.sin(radz);
        Matrix3f mattz =  new Matrix3f(new float[][] {
                {cosz, sinz, 0},
                {-sinz, cosz, 0},
                {0, 0, 1}
        });
        Matrix3f res = mattz.multiply(matty);
        Matrix3f res1 = mattx.multiply(res);
        res1.multiplyvec(vec);
    }

    public static Matrix4f rotateX4(float angleX) {
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

    public static Matrix4f rotateY4(float angleY) {
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

    public static Matrix4f rotateZ4(float angleZ) {
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
        return Matrix4f.multiplymatt(rotateZ4(angleZ), Matrix4f.multiplymatt(rotateY4(angleY), rotateX4(angleX)));
    }

    public static void translate3(float tx, float ty, float tz, Vector4f v) {
        Matrix4f matt = new Matrix4f(new float[][] {
                {1, 0, 0, tx},
                {0, 1, 0, ty},
                {0, 0, 1, tz},
                {0, 0, 0, 1}
        });
        matt.multiplyvec(v);
    }
    public static Matrix4f rotateScaleTranslate(
            float scaleX, float scaleY, float scaleZ,
            float angleX, float angleY, float angleZ,
            float translationX, float translationY, float translationZ
    ) {
        Matrix4f matt = new Matrix4f(new float[][] {
                {1, 0, 0, translationX},
                {0, 1, 0, translationY},
                {0, 0, 1, translationZ},
                {0, 0, 0, 1}
        });
        return Matrix4f.multiplymatt(matt,
               Matrix4f.multiplymatt(rotateMatrix4f(angleX,angleY,angleZ), scaleMatrix4f(scaleX,scaleY,scaleZ)));
    }
//
//    public static void scale4(float scaleX, float scaleY, float scaleZ, Vector4f v) {
//        Matrix4f matt = new Matrix4f(new float[][] {
//                {scaleX, 0, 0, 0},
//                {0, scaleY, 0, 0},
//                {0, 0, scaleZ, 0},
//                {0, 0, 0, 1}
//        });
//        matt.multiplyvec(v);
//    }
//
//    public static void rotateX4(float angle, Vector4f v) {
//        float rad = (float) Math.toRadians(angle);
//        float cos = (float) Math.cos(rad);
//        float sin = (float) Math.sin(rad);
//        Matrix4f matt = new Matrix4f(new float[][] {
//                {1, 0, 0, 0},
//                {0, cos, -sin, 0},
//                {0, sin, cos, 0},
//                {0, 0, 0, 1}
//        });
//        matt.multiplyvec(v);
//    }
//
//    public static void rotateY4(float angle, Vector4f v) {
//        float rad = (float) Math.toRadians(angle);
//        float cos = (float) Math.cos(rad);
//        float sin = (float) Math.sin(rad);
//        Matrix4f matt =  new Matrix4f(new float[][] {
//                {cos, 0, sin, 0},
//                {0, 1, 0, 0},
//                {-sin, 0, cos, 0},
//                {0, 0, 0, 1}
//        });
//        matt.multiplyvec(v);
//    }
//
//    public static void rotateZ4(float angle, Vector4f v) {
//        float rad = (float) Math.toRadians(angle);
//        float cos = (float) Math.cos(rad);
//        float sin = (float) Math.sin(rad);
//        Matrix4f matt =  new Matrix4f(new float[][] {
//                {cos, -sin, 0, 0},
//                {sin, cos, 0, 0},
//                {0, 0, 1, 0},
//                {0, 0, 0, 1}
//        });
//        matt.multiplyvec(v);
//    }
}