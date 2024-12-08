package com.cgvsu.render_engine;
import com.cgvsu.math.Vector3f;
import com.cgvsu.math.Matrix4f;

public class Camera {

    private Vector3f position;
    private Vector3f target;
    private float fov;
    private float aspectRatio;
    private float nearPlane;
    private float farPlane;

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

    // Геттеры и сеттеры
    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(final Vector3f position) {
        this.position = position;
    }

    public Vector3f getTarget() {
        return target;
    }

    public void setTarget(final Vector3f target) {
        this.target = target;
    }

    public float getFov() {
        return fov;
    }

    public void setFov(final float fov) {
        this.fov = fov;
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(final float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public float getNearPlane() {
        return nearPlane;
    }

    public void setNearPlane(final float nearPlane) {
        this.nearPlane = nearPlane;
    }

    public float getFarPlane() {
        return farPlane;
    }

    public void setFarPlane(final float farPlane) {
        this.farPlane = farPlane;
    }

    // Методы перемещения
    public void movePosition(final Vector3f translation) {
        this.position.add(translation);
    }

    public void moveTarget(final Vector3f translation) {
        this.target.add(translation);
    }

    // Методы получения матриц
    public Matrix4f getViewMatrix() {
        return GraphicConveyor.lookAt(position, target);
    }

    public Matrix4f getProjectionMatrix() {
        return GraphicConveyor.perspective(fov, aspectRatio, nearPlane, farPlane);
    }
}