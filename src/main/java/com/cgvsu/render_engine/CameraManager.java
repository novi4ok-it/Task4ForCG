package com.cgvsu.render_engine;

import com.cgvsu.math.Vector3f;

import java.util.List;
import java.util.ArrayList;

public class CameraManager {
    private List<Camera> cameras;
    private int activeCameraIndex;

    public CameraManager() {
        cameras = new ArrayList<>();
        activeCameraIndex = -1; // -1 означает, что активной камеры нет
    }

    // Добавляет новую камеру
    public void addCamera(Camera camera) {
        cameras.add(camera);
        if (activeCameraIndex == -1) {
            activeCameraIndex = 0; // Первая добавленная камера становится активной
        }
    }

    // Удаляет камеру по индексу
    public void removeCamera(int index) {
        if (index >= 0 && index < cameras.size()) {
            cameras.remove(index);
            if (activeCameraIndex == index) {
                activeCameraIndex = cameras.isEmpty() ? -1 : 0; // Переключение на первую камеру
            } else if (activeCameraIndex > index) {
                activeCameraIndex--;
            }
        }
    }

    // Устанавливает активную камеру по индексу
    public void setActiveCamera(int index) {
        if (index >= 0 && index < cameras.size()) {
            activeCameraIndex = index;
        }
    }

    // Возвращает активную камеру или null, если камеры отсутствуют
    public Camera getActiveCamera() {
        if (activeCameraIndex >= 0 && activeCameraIndex < cameras.size()) {
            return cameras.get(activeCameraIndex);
        }
        return null;
    }

    // Возвращает список всех камер
    public List<Camera> getCameras() {
        return cameras;
    }
}

