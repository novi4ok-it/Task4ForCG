package com.cgvsu.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Polygon {

    private ArrayList<Integer> vertexIndices;
    private ArrayList<Integer> textureVertexIndices;
    private ArrayList<Integer> normalIndices;

    public Polygon() {
        vertexIndices = new ArrayList<>();
        textureVertexIndices = new ArrayList<>();
        normalIndices = new ArrayList<>();
    }

    public Polygon(List<Integer> vertexIndices) {
        this.vertexIndices = new ArrayList<>(vertexIndices);
        textureVertexIndices = new ArrayList<>();
        normalIndices = new ArrayList<>();
    }

    public void setVertexIndices(ArrayList<Integer> vertexIndices) {
        assert vertexIndices.size() >= 3 : "Polygon must have at least 3 vertices.";
        this.vertexIndices = vertexIndices;
    }

    public void setTextureVertexIndices(ArrayList<Integer> textureVertexIndices) {
        assert textureVertexIndices.size() == vertexIndices.size() :
            "Number of texture vertex indices must match number of vertex indices.";
        this.textureVertexIndices = textureVertexIndices;
    }

    public void setNormalIndices(ArrayList<Integer> normalIndices) {
        assert normalIndices.size() == vertexIndices.size() :
            "Number of normal indices must match number of vertex indices.";
        this.normalIndices = normalIndices;
    }

    public ArrayList<Integer> getVertexIndices() {
        return vertexIndices;
    }

    public ArrayList<Integer> getTextureVertexIndices() {
        return textureVertexIndices;
    }

    public ArrayList<Integer> getNormalIndices() {
        return normalIndices;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Polygon polygon = (Polygon) obj;
        return vertexIndices.equals(polygon.vertexIndices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertexIndices);
    }
}