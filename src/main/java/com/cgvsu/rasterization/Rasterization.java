package com.cgvsu.rasterization;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.math.VectorMath;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.Comparator;

import static java.util.Arrays.sort;

public class Rasterization {
    public static void fillTriangle(
            GraphicsContext graphicsContext,
            Vector3f[] vertices,
            Color fillColor) {

        PixelWriter pixelWriter = graphicsContext.getPixelWriter();
        int width = (int) graphicsContext.getCanvas().getWidth();
        int height = (int) graphicsContext.getCanvas().getHeight();
        double[][] zBuffer = initializeZBuffer(width, height);

        // Сортировка вершин треугольника по Y
        Arrays.sort(vertices, Comparator.comparingDouble(Vector3f::y));

        // Растеризация треугольника
        rasterizeTriangle(vertices, fillColor, zBuffer, pixelWriter, width, height);
    }

    private static void rasterizeTriangle(
            Vector3f[] vertices,
            Color fillColor,
            double[][] zBuffer,
            PixelWriter pixelWriter,
            int width,
            int height) {

        for (boolean isLower : new boolean[]{true, false}) {
            Vector3f top = isLower ? vertices[1] : vertices[0];
            Vector3f bottom = vertices[2];
            Vector3f middle = isLower ? vertices[0] : vertices[1];

            for (int y = (int) top.y(); y <= (int) bottom.y(); y++) {
                if (y < 0 || y >= height) continue;

                float alpha = (y - top.y()) / (bottom.y() - top.y());
                float beta = (y - middle.y()) / (bottom.y() - middle.y());

                Vector3f edge1 = interpolate(top, bottom, alpha);
                Vector3f edge2 = interpolate(middle, bottom, beta);

                if (edge1.x > edge2.x) {
                    Vector3f temp = edge1;
                    edge1 = edge2;
                    edge2 = temp;
                }

                for (int x = Math.max(0, (int) edge1.x()); x <= Math.min(width - 1, (int) edge2.x()); x++) {
                    Vector3f pixelPos = new Vector3f(x, y, 0);
                    double[] baryCoords = calculateBarycentricCoords(pixelPos, vertices);

                    double z = baryCoords[0] * vertices[0].z +
                            baryCoords[1] * vertices[1].z +
                            baryCoords[2] * vertices[2].z;

                    if (z < zBuffer[x][y]) {
                        zBuffer[x][y] = z;
                        pixelWriter.setColor(x, y, fillColor);
                    }
                }
            }
        }
    }

    public static void fillTriangleWithTexture(
            GraphicsContext graphicsContext,
            Vector3f[] vertices,
            Vector2f[] texCoords, // Текстурные координаты (u, v)
            Image texture) {

        PixelWriter pixelWriter = graphicsContext.getPixelWriter();
        PixelReader textureReader = texture.getPixelReader();
        int width = (int) graphicsContext.getCanvas().getWidth();
        int height = (int) graphicsContext.getCanvas().getHeight();
        double[][] zBuffer = initializeZBuffer(width, height);

        // Сортировка вершин треугольника по Y
        Arrays.sort(vertices, Comparator.comparingDouble(Vector3f::y));

        // Растеризация треугольника
        rasterizeTriangleWithTexture(vertices, texCoords, zBuffer, pixelWriter, textureReader, texture, width, height);
    }

    private static void rasterizeTriangleWithTexture(
            Vector3f[] vertices,
            Vector2f[] texCoords,
            double[][] zBuffer,
            PixelWriter pixelWriter,
            PixelReader textureReader,
            Image texture,
            int width,
            int height) {

        for (boolean isLower : new boolean[]{true, false}) {
            Vector3f top = isLower ? vertices[1] : vertices[0];
            Vector3f bottom = vertices[2];
            Vector3f middle = isLower ? vertices[0] : vertices[1];

            Vector2f topTex = isLower ? texCoords[1] : texCoords[0];
            Vector2f bottomTex = texCoords[2];
            Vector2f middleTex = isLower ? texCoords[0] : texCoords[1];

            for (int y = (int) top.y(); y <= (int) bottom.y(); y++) {
                if (y < 0 || y >= height) continue;

                float alpha = (y - top.y()) / (bottom.y() - top.y());
                float beta = (y - middle.y()) / (bottom.y() - middle.y());

                Vector3f edge1 = interpolate(top, bottom, alpha);
                Vector3f edge2 = interpolate(middle, bottom, beta);

                Vector2f tex1 = interpolate(topTex, bottomTex, alpha);
                Vector2f tex2 = interpolate(middleTex, bottomTex, beta);

                if (edge1.x > edge2.x) {
                    Vector3f temp = edge1;
                    edge1 = edge2;
                    edge2 = temp;

                    Vector2f tempTex = tex1;
                    tex1 = tex2;
                    tex2 = tempTex;
                }

                for (int x = Math.max(0, (int) edge1.x()); x <= Math.min(width - 1, (int) edge2.x()); x++) {
                    Vector3f pixelPos = new Vector3f(x, y, 0);
                    double[] baryCoords = calculateBarycentricCoords(pixelPos, vertices);

                    double z = baryCoords[0] * vertices[0].z +
                            baryCoords[1] * vertices[1].z +
                            baryCoords[2] * vertices[2].z;

                    if (z < zBuffer[x][y]) {
                        zBuffer[x][y] = z;

                        // Интерполяция текстурных координат
                        double u = baryCoords[0] * texCoords[0].x +
                                baryCoords[1] * texCoords[1].x +
                                baryCoords[2] * texCoords[2].x;

                        double v = baryCoords[0] * texCoords[0].y +
                                baryCoords[1] * texCoords[1].y +
                                baryCoords[2] * texCoords[2].y;

                        // Получение цвета из текстуры
                        int texX = (int) (u * (texture.getWidth() - 1));
                        int texY = (int) (v * (texture.getHeight() - 1));

                        Color texColor = textureReader.getColor(texX, texY);
                        pixelWriter.setColor(x, y, texColor);
                    }
                }
            }
        }
    }

    private static double[][] initializeZBuffer(int width, int height) {
        double[][] zBuffer = new double[width][height];
        for (int x = 0; x < width; x++) {
            Arrays.fill(zBuffer[x], Double.POSITIVE_INFINITY);
        }
        return zBuffer;
    }

    public static void drawTriangle(
            GraphicsContext graphicsContext,
            Vector3f[] vertices,
            Color fillColor) {
        fillTriangle(graphicsContext, vertices, fillColor);
    }

    public static void drawTriangle(
            GraphicsContext graphicsContext,
            Vector3f[] vertices,
            Vector2f[] texCoords,
            Image texture) {
        fillTriangleWithTexture(graphicsContext, vertices, texCoords, texture);
    }

    static Vector3f interpolate(Vector3f v1, Vector3f v2, float alpha) {
        return Vector3f.addition(v1.multiply(1 - alpha), v2.multiply(alpha));
    }

    private static Vector2f interpolate(Vector2f v1, Vector2f v2, float alpha) {
        return new Vector2f(
                v1.x * (1 - alpha) + v2.x * alpha,
                v1.y * (1 - alpha) + v2.y * alpha
        );
    }

    static double[] calculateBarycentricCoords(Vector3f point, Vector3f[] vertices) {
        Vector3f a = vertices[0];
        Vector3f b = vertices[1];
        Vector3f c = vertices[2];

        float area = VectorMath.crossProduct(a, b, c);
        float alpha = VectorMath.crossProduct(point, b, c) / area;
        float beta = VectorMath.crossProduct(point, c, a) / area;
        float gamma = 1 - alpha - beta;

        return new double[]{alpha, beta, gamma};
    }
}
