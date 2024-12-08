package com.cgvsu.model;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Polygon {

    private List<Integer> vertexIndices;
    private List<Integer> textureVertexIndices;
    private List<Integer> normalIndices;

    public Polygon() {
        vertexIndices = new ArrayList<>();
        textureVertexIndices = new ArrayList<>();
        normalIndices = new ArrayList<>();
    }

    public Polygon(List<Integer> vertexIndices) {
        this.vertexIndices = new ArrayList<>(vertexIndices);
        this.textureVertexIndices = new ArrayList<>();
        this.normalIndices = new ArrayList<>();
    }

    // Геттеры
    public List<Integer> getVertexIndices() {
        return vertexIndices;
    }

    public List<Integer> getTextureVertexIndices() {
        return textureVertexIndices;
    }

    public List<Integer> getNormalIndices() {
        return normalIndices;
    }

    // Методы для добавления индексов
    public void addVertexIndex(int index) {
        vertexIndices.add(index);
    }

    public void addTextureVertexIndex(int index) {
        textureVertexIndices.add(index);
    }

    public void addNormalIndex(int index) {
        normalIndices.add(index);
    }

    // Установка индексов
    public void setVertexIndices(List<Integer> vertexIndices) {
        if (vertexIndices.size() < 3) {
            throw new IllegalArgumentException("Полигон должен иметь минимум 3 вершины");
        }
        this.vertexIndices = new ArrayList<>(vertexIndices);
    }

    public void setTextureVertexIndices(List<Integer> textureVertexIndices) {
        if (textureVertexIndices.size() < 3) {
            throw new IllegalArgumentException("Полигон должен иметь минимум 3 текстурные вершины");
        }
        this.textureVertexIndices = new ArrayList<>(textureVertexIndices);
    }

    public void setNormalIndices(List<Integer> normalIndices) {
        if (normalIndices.size() < 3) {
            throw new IllegalArgumentException("Полигон должен иметь минимум 3 нормали");
        }
        this.normalIndices = new ArrayList<>(normalIndices);
    }

    // Получение точек полигона на основе индексов
    public List<Vector3f> getPoints(List<Vector3f> vertices) {
        List<Vector3f> points = new ArrayList<>();
        for (int index : vertexIndices) {
            points.add(vertices.get(index));
        }
        return points;
    }

    // Сравнение
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Polygon polygon = (Polygon) obj;
        return Objects.equals(vertexIndices, polygon.vertexIndices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertexIndices);
    }
    public List<Vector2f> getTexCoords(Model model) {
        List<Vector2f> texCoords = new ArrayList<>();
        for (Integer index : textureVertexIndices) {
            // Используем индекс для получения соответствующих текстурных координат из модели
            texCoords.add(model.getTextureVertices().get(index));
        }
        return texCoords;
    }
}