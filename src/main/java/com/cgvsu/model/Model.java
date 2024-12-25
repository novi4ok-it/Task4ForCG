package com.cgvsu.model;
import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.math.Vector4f;
import javafx.scene.image.Image;

import java.util.*;

public class Model {

    public ArrayList<Vector3f> vertices = new ArrayList<>();
    public ArrayList<Vector2f> textureVertices = new ArrayList<>();
    public ArrayList<Vector3f> normals = new ArrayList<>();
    public ArrayList<Polygon> polygons = new ArrayList<>();

    // Текстура модели
    public Image texture;

    public Model() {}

    public Model(Image texture) {
        this.texture = texture;
    }

    public boolean hasTexture() {
        return texture != null;
    }
}