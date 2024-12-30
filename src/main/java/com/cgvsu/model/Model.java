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

    public ArrayList<Vector3f> originalVertices = new ArrayList<>();



    public Vector3f scale = new Vector3f(1, 1, 1);
    public Vector3f rotation = new Vector3f(0, 0, 0);
    public Vector3f translation = new Vector3f(0, 0, 0);


    public Vector3f position = new Vector3f(0, 0, 0);

    public Vector3f getScale(){
        return scale;
    }

    public void setScale(Vector3f scale){
        this.scale = scale;
    }

    public Vector3f getRotation(){
        return rotation;
    }

    public void setRotation(Vector3f rotation){
        this.rotation = rotation;
    }

    public Vector3f getTranslation(){
        return translation;
    }

    public void setTranslation(Vector3f translation){
        this.translation = translation;
    }
    public void setOriginalVertices() {
        originalVertices = new ArrayList<>();
        for (Vector3f vertex : vertices) {
            originalVertices.add(new Vector3f(vertex.x, vertex.y, vertex.z));
        }
    }

    public void setVertices() {
        for (int i = 0; i < vertices.size(); i++) {
            vertices.get(i).setX(originalVertices.get(i).x());
            vertices.get(i).setY(originalVertices.get(i).y());
            vertices.get(i).setZ(originalVertices.get(i).z());
        }
    }

    public boolean areVerticesEqual() {
        if (originalVertices == null || vertices == null) {
            return false;
        }

        if (originalVertices.size() != vertices.size()) {
            return false;
        }

        for (int i = 0; i < originalVertices.size(); i++) {
            if (!originalVertices.get(i).equals(vertices.get(i))) {
                return false;
            }
        }

        return true;
    }


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