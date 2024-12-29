package com.cgvsu.render_engine;
import com.cgvsu.math.Vector3f;
import com.cgvsu.math.Matrix4f;

public class Camera {

    public Camera(
            final Vector3f position,
            final Vector3f target,
            final float fov,
            final float aspectRatio,
            final float nearPlane,
            final float farPlane) {
        this.position = position;
        this.target = target;
        this.fov = fov;
        this.aspectRatio = aspectRatio;
        this.nearPlane = nearPlane;
        this.farPlane = farPlane;
    }

    public void setPosition(final Vector3f position) {
        this.position = position;
    }

    public void setTarget(final Vector3f target) {
        this.target = target;
    }

    public void setAspectRatio(final float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getTarget() {
        return target;
    }

    public void movePosition(final Vector3f translation) {
        this.position.add(translation);
    }

    public void moveTarget(final Vector3f translation) {
        this.target.add(target);
    }

    public Matrix4f getViewMatrix() {
        return GraphicConveyor.lookAt(position, target);
    }

    public Matrix4f getProjectionMatrix() {
        return GraphicConveyor.perspective(fov, aspectRatio, nearPlane, farPlane);
    }
    public void rotateAroundTarget(float yaw, float pitch, float roll) {
        // Вращение вокруг оси Y (yaw)
        Vector3f direction = target.subtraction(target, position);
        float yawRad = (float) Math.toRadians(yaw);
        float cosYaw = (float) Math.cos(yawRad);
        float sinYaw = (float) Math.sin(yawRad);
        float newX = direction.x * cosYaw - direction.z * sinYaw;
        float newZ = direction.x * sinYaw + direction.z * cosYaw;
        direction.x = newX;
        direction.z = newZ;

        // Вращение вокруг оси X (pitch)
        float pitchRad = (float) Math.toRadians(pitch);
        float cosPitch = (float) Math.cos(pitchRad);
        float sinPitch = (float) Math.sin(pitchRad);
        float newY = direction.y * cosPitch - direction.z * sinPitch;
        float newZ2 = direction.y * sinPitch + direction.z * cosPitch;
        direction.y = newY;
        direction.z = newZ2;

        // Вращение вокруг оси Z (roll)?????????????????
        float rollRad = (float) Math.toRadians(roll);
        float cosRoll = (float) Math.cos(rollRad);
        float sinRoll = (float) Math.sin(rollRad);
        float newX2 = direction.x * cosRoll - direction.y * sinRoll;
        float newY2 = direction.x * sinRoll + direction.y * cosRoll;
        direction.x = newX2;
        direction.y = newY2;

        // Обновляем позицию камеры
        position = target.subtraction(target, direction);
    }

    public void movePositionAndTarget(final Vector3f translation) {
        this.position.add(translation);
        this.target.add(translation);
    }

    private Vector3f position;
    private Vector3f target;
    private float fov;
    private float aspectRatio;
    private float nearPlane;
    private float farPlane;
}