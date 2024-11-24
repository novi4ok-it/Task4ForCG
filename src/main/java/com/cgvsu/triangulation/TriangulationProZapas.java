package com.cgvsu.triangulation;

import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TriangulationProZapas extends Model {
    // Добавляем поля для хранения информации о триангулированной модели.
    // Например, можно хранить список индексов треугольников.

    public List<int[]> triangles = new ArrayList<>();


    public TriangulationProZapas(Model model) {
        // Копируем данные из исходной модели.
        this.vertices = new ArrayList<>(model.vertices);
        this.textureVertices = new ArrayList<>(model.textureVertices);
        this.normals = new ArrayList<>(model.normals);

        triangulate(model.polygons);
    }

    private void triangulate(ArrayList<Polygon> polygons) {
        for (Polygon polygon : polygons) {
            ArrayList<Integer> vertexIndices = polygon.getVertexIndices();
            int numVertices = vertexIndices.size();

            if (numVertices < 3) {
                continue; // Невозможно создать треугольник
            }


            for (int i = 0; i < numVertices - 2; i++) {
                //Создаем треугольники, соединяя вершины
                triangles.add(new int[]{vertexIndices.get(0), vertexIndices.get(i + 1), vertexIndices.get(i + 2)});
            }
        }
    }

    public List<int[]> getTriangles(){
        return triangles;
    }
}

class Main{
    public static void main(String[] args){
        Model model = new Model();
        model.vertices.add(new Vector3f(0,0,0));
        model.vertices.add(new Vector3f(1,0,0));
        model.vertices.add(new Vector3f(1,1,0));
        model.vertices.add(new Vector3f(0,1,0));
        model.vertices.add(new Vector3f(0.5f,0.5f,0));


        Polygon polygon = new Polygon();
        polygon.setVertexIndices(new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4)));
        model.polygons.add(polygon);


        TriangulationProZapas triangulatedModel = new TriangulationProZapas(model);
        List<int[]> triangles = triangulatedModel.getTriangles();
        for(int[] triangle : triangles){
            System.out.println(Arrays.toString(triangle));
        }

    }
}

