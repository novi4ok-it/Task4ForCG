package com.cgvsu.model;
import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.math.Vector4f;
import javafx.scene.image.Image;

import java.util.*;

public class Model {

    public static ArrayList<Vector3f> vertices = new ArrayList<>();
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

    public static Vector3f scale = new Vector3f(1, 1, 1);
    public static Vector3f rotation = new Vector3f(0, 0, 0);
    public static Vector4f translation = new Vector4f(0, 0, 0, 1);

    public static ArrayList<Vector3f> getVertices(){
        return vertices;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public void setTranslation(Vector4f translation) {
        this.translation = translation;
    }
}