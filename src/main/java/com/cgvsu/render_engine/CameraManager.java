package com.cgvsu.render_engine;

import com.cgvsu.math.Vector3f;

import java.util.List;
import java.util.ArrayList;

public class CameraManager {
    private List<Camera> cameras;
    private int activeCameraIndex;

    public CameraManager() {
        cameras = new ArrayList<>();
        activeCameraIndex = -1;
    }

    // Добавляет новую камеру
    public void addCamera(Camera camera) {
        cameras.add(camera);
        if (activeCameraIndex == -1) {
            activeCameraIndex = 0;
        }
    }

    // Удаляет камеру по индексу
    public void removeCamera(int index) {
        if (index >= 0 && index < cameras.size()) {
            cameras.remove(index);
            if (activeCameraIndex == index) {
                activeCameraIndex = cameras.isEmpty() ? -1 : 0; //Переключение на первую камеру
            } else if (activeCameraIndex > index) {
                activeCameraIndex--;
            }
        }
    }

    public void setActiveCamera(int index) {
        if (index >= 0 && index < cameras.size()) {
            activeCameraIndex = index;
        }
    }

    public Camera getActiveCamera() {
        if (activeCameraIndex >= 0 && activeCameraIndex < cameras.size()) {
            return cameras.get(activeCameraIndex);
        }
        return null;
    }

    public List<Camera> getCameras() {
        return cameras;
    }
}

