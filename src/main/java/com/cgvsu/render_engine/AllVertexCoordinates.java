package com.cgvsu.render_engine;

import java.util.List;

public class AllVertexCoordinates {
    private List<Integer> allX;
    private List<Integer> allY;
    private List<Float> allZ;

    public AllVertexCoordinates(List<Integer> allX, List<Integer> allY, List<Float> allZ) {
        this.allX = allX;
        this.allY = allY;
        this.allZ = allZ;
    }

    // Методы add для каждого списка
    public void addX(int x) {
        allX.add(x);
    }

    public void addY(int y) {
        allY.add(y);
    }

    public void addZ(float z) {
        allZ.add(z);
    }

    // Методы get для каждого списка
    public List<Integer> getAllX() {
        return allX;
    }

    public List<Integer> getAllY() {
        return allY;
    }

    public List<Float> getAllZ() {
        return allZ;
    }

    // Методы set для каждого списка
    public void setAllX(List<Integer> allX) {
        this.allX = allX;
    }

    public void setAllY(List<Integer> allY) {
        this.allY = allY;
    }

    public void setAllZ(List<Float> allZ) {
        this.allZ = allZ;
    }
}
