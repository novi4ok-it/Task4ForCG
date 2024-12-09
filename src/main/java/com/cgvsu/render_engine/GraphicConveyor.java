package com.cgvsu.render_engine;

import com.cgvsu.math.Matrix4f;
import com.cgvsu.math.Vector3f;

import javax.vecmath.Point2f;

public class GraphicConveyor {

    public static Matrix4f rotateScaleTranslate() {
        float[] matrix = new float[]{
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1};
        return new Matrix4f(matrix);
    }

    public static Matrix4f lookAt(Vector3f eye, Vector3f target) {
        return lookAt(eye, target, new Vector3f(0F, 1.0F, 0F));
    }

    public static Matrix4f lookAt(Vector3f eye, Vector3f target, Vector3f up) {
        Vector3f resultX = new Vector3f();
        Vector3f resultY = new Vector3f();
        Vector3f resultZ = new Vector3f();

        resultZ.sub(target);
        resultZ.sub(eye);
        resultX.cross(up);
        resultX.cross(resultZ);
        resultY.cross(resultZ);
        resultY.cross(resultX);

        resultX.normalize();
        resultY.normalize();
        resultZ.normalize();

        Matrix4f result = new Matrix4f();
        result.set(0, 0, resultX.x());
        result.set(0, 1, resultY.x());
        result.set(0, 2, resultZ.x());
        result.set(0, 3, 0);

        result.set(1, 0, resultX.y());
        result.set(1, 1, resultY.y());
        result.set(1, 2, resultZ.y());
        result.set(1, 3, 0);

        result.set(2, 0, resultX.z());
        result.set(2, 1, resultY.z());
        result.set(2, 2, resultZ.z());
        result.set(2, 3, 0);

        result.set(3, 0, -resultX.dot(eye));
        result.set(3, 1, -resultY.dot(eye));
        result.set(3, 2, -resultZ.dot(eye));
        result.set(3, 3, 1);

        return result;
    }

    public static Matrix4f perspective(
            final float fov,
            final float aspectRatio,
            final float nearPlane,
            final float farPlane) {
        float tanHalfFov = (float) Math.tan(fov / 2.0);
        Matrix4f result = new Matrix4f(new float[]{
                1 / (aspectRatio * tanHalfFov), 0, 0, 0,
                0, 1 / tanHalfFov, 0, 0,
                0, 0, -(farPlane + nearPlane) / (farPlane - nearPlane), -1,
                0, 0, -(2 * farPlane * nearPlane) / (farPlane - nearPlane), 0
        });
        return result;
    }

//    public static Vector3f multiplyMatrix4ByVector3(final Matrix4f matrix, final com.cgvsu.math.Vector3f vertex) {
//        final float x = (vertex.x * matrix.m00) + (vertex.y * matrix.m10) + (vertex.z * matrix.m20) + matrix.m30;
//        final float y = (vertex.x * matrix.m01) + (vertex.y * matrix.m11) + (vertex.z * matrix.m21) + matrix.m31;
//        final float z = (vertex.x * matrix.m02) + (vertex.y * matrix.m12) + (vertex.z * matrix.m22) + matrix.m32;
//        final float w = (vertex.x * matrix.m03) + (vertex.y * matrix.m13) + (vertex.z * matrix.m23) + matrix.m33;
//        return new Vector3f(x / w, y / w, z / w);
//    }

    public static Point2f vertexToPoint(final Vector3f vertex, final int width, final int height) {
        return new Point2f(vertex.x * width + width / 2.0F, -vertex.y * height + height / 2.0F);
    }
}
