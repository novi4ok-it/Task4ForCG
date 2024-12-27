package com.cgvsu.render_engine;

import com.cgvsu.math.Point2f;
import com.cgvsu.math.Vector3f;

import java.util.ArrayList;

public class TriangleData {
        int[] arrX;
        int[] arrY;
        float[] arrZ;
        Point2f[] texCoords;
        float[] lightIntensities;
        public ArrayList<Vector3f> normals;
        Vector3f[] transformedVertices;

        TriangleData(int[] arrX, int[] arrY, float[] arrZ, Point2f[] texCoords, float[] lightIntensities, ArrayList<Vector3f> normals, Vector3f[] transformedVertices) {
            this.arrX = arrX;
            this.arrY = arrY;
            this.arrZ = arrZ;
            this.texCoords = texCoords;
            this.lightIntensities = lightIntensities;
            this.normals = normals;
            this.transformedVertices = transformedVertices;
        }
    }
