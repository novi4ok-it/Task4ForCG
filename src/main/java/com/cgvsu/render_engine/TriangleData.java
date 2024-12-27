package com.cgvsu.render_engine;

import com.cgvsu.math.Point2f;
import com.cgvsu.math.Vector3f;

import java.util.ArrayList;

public class TriangleData {
        int[] arrX;
        int[] arrY;
        float[] arrZ;
        Point2f[] texCoords;
        public ArrayList<Vector3f> normals;

        TriangleData(int[] arrX, int[] arrY, float[] arrZ, Point2f[] texCoords, ArrayList<Vector3f> normals) {
            this.arrX = arrX;
            this.arrY = arrY;
            this.arrZ = arrZ;
            this.texCoords = texCoords;
            this.normals = normals;
        }
    }
