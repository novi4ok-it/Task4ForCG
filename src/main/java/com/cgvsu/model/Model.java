package com.cgvsu.model;
import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.triangulation.EarClipping;
import javafx.scene.image.WritableImage;

import java.util.*;

public class Model {

    public ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
    public ArrayList<Vector2f> textureVertices = new ArrayList<Vector2f>();
    public ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
    public ArrayList<Polygon> polygons = new ArrayList<Polygon>();
    private WritableImage texture;
      public List<Vector3f> getVertices() {
        return vertices;
    }

    public List<Vector2f> getTextureVertices() {
        return textureVertices;
    }

    public List<Vector3f> getNormals() {
        return normals;
    }

    public List<Polygon> getPolygons() {
        return polygons;
    }
    public void setTexture(WritableImage texture) {
        this.texture = texture;
    }

    // Получение текстуры модели
    public WritableImage getTexture() {
        return texture;
    }

    // Методы для добавления данных
    public void addVertex(Vector3f vertex) {
        vertices.add(vertex);
    }

    public void addTextureVertex(Vector2f textureVertex) {
        textureVertices.add(textureVertex);
    }

    public void addNormal(Vector3f normal) {
        normals.add(normal);
    }

    public void addPolygon(Polygon polygon) {
        polygons.add(polygon);
    }

    // Очистка данных модели
    public void clear() {
        vertices.clear();
        textureVertices.clear();
        normals.clear();
        polygons.clear();
    }

    // Получение триангулированных полигонов
    public List<Polygon> getTriangulatedPolygons() {
        List<Polygon> triangulatedPolygons = new ArrayList<>();
        for (Polygon polygon : polygons) {
            triangulatedPolygons.addAll(EarClipping.triangulate(polygon, vertices));
        }
        return triangulatedPolygons;
    }
}
