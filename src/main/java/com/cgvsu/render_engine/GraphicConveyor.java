package com.cgvsu.render_engine;
import com.cgvsu.math.Vector3f;

import com.cgvsu.math.Matrix4f;
import com.cgvsu.math.Point2f;


public class GraphicConveyor {

    // Создание единичной матрицы (вращение, масштаб, перенос)
    public static Matrix4f rotateScaleTranslate() {
        Matrix4f result = new Matrix4f();
        for (int i = 0; i < 4; i++) {
            result.set(i, i, 1.0F);
        }
        return result;
    }

    // Создание матрицы вида
    public static Matrix4f lookAt(Vector3f eye, Vector3f target) {
        return lookAt(eye, target, new Vector3f(0F, 1.0F, 0F));
    }

    public static Matrix4f lookAt(Vector3f eye, Vector3f target, Vector3f up) {
        Vector3f resultX = new Vector3f();
        Vector3f resultY = new Vector3f();
        Vector3f resultZ = new Vector3f();

        resultZ.sub(target, eye);
        resultX.cross(up, resultZ);
        resultY.cross(resultZ, resultX);

        resultX.normalize();
        resultY.normalize();
        resultZ.normalize();

        Matrix4f result = new Matrix4f();
        result.set(0, 0, resultX.x());
        result.set(0, 1, resultY.x());
        result.set(0, 2, resultZ.x());
        result.set(1, 0, resultX.y());
        result.set(1, 1, resultY.y());
        result.set(1, 2, resultZ.y());
        result.set(2, 0, resultX.z());
        result.set(2, 1, resultY.z());
        result.set(2, 2, resultZ.z());
        result.set(3, 0, -resultX.dot(eye));
        result.set(3, 1, -resultY.dot(eye));
        result.set(3, 2, -resultZ.dot(eye));
        result.set(3, 3, 1.0F);

        return result;
    }

     public static Matrix4f perspective(float fov, float aspectRatio, float nearPlane, float farPlane) {
        Matrix4f result = new Matrix4f();
        float tangentMinusOnDegree = (float) (1.0F / Math.tan(fov * 0.5F));
        result.set(0, 0, tangentMinusOnDegree / aspectRatio);
        result.set(1, 1, tangentMinusOnDegree);
        result.set(2, 2, (farPlane + nearPlane) / (farPlane - nearPlane));
        result.set(2, 3, 1.0F);
        result.set(3, 2, 2 * (nearPlane * farPlane) / (nearPlane - farPlane));
        return result;
    }

    public static Vector3f multiplyMatrix4ByVector3(Matrix4f matrix, Vector3f vertex) {
        float x = vertex.x() * matrix.get(0, 0) + vertex.y() * matrix.get(1, 0) + vertex.z() * matrix.get(2, 0) + matrix.get(3, 0);
        float y = vertex.x() * matrix.get(0, 1) + vertex.y() * matrix.get(1, 1) + vertex.z() * matrix.get(2, 1) + matrix.get(3, 1);
        float z = vertex.x() * matrix.get(0, 2) + vertex.y() * matrix.get(1, 2) + vertex.z() * matrix.get(2, 2) + matrix.get(3, 2);
        float w = vertex.x() * matrix.get(0, 3) + vertex.y() * matrix.get(1, 3) + vertex.z() * matrix.get(2, 3) + matrix.get(3, 3);

        if (w != 0) {
            x /= w;
            y /= w;
            z /= w;
        }

        return new Vector3f(x, y, z);
    }

    public static Point2f vertexToPoint(final Vector3f vertex, final int width, final int height) {
        return new Point2f(vertex.x * width + width / 2.0F, -vertex.y * height + height / 2.0F);
    }
}
