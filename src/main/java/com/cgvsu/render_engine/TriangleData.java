package com.cgvsu.render_engine;

import com.cgvsu.math.Point2f;

public class TriangleData {
        int[] arrX;
        int[] arrY;
        float[] arrZ;
        Point2f[] texCoords;
        float[] lightIntensities;

        TriangleData(int[] arrX, int[] arrY, float[] arrZ, Point2f[] texCoords, float[] lightIntensities) {
            this.arrX = arrX;
            this.arrY = arrY;
            this.arrZ = arrZ;
            this.texCoords = texCoords;
            this.lightIntensities = lightIntensities;
        }
    }
