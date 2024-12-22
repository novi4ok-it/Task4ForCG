package com.cgvsu.rasterization;

import com.cgvsu.math.Matrix4f;
import com.cgvsu.math.Point2f;
import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import com.cgvsu.render_engine.Camera;
import com.cgvsu.render_engine.GraphicConveyor;
import com.cgvsu.render_engine.TriangleData;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class TexturedTriangleRenderer implements TriangleRenderer {
    private final Image texture;
    private final PixelReader textureReader;
    private double[][] zBuffer;

    public TexturedTriangleRenderer(Image texture, double[][] zBuffer) {
        this.texture = texture;
        this.textureReader = texture.getPixelReader();
        this.zBuffer = zBuffer;
    }

    @Override
    public void render(
            final GraphicsContext graphicsContext,
            int[] arrX,
            int[] arrY,
            float[] arrZ,
            Point2f[] texCoords,
            float[] lightIntensities,
            final boolean useLighting) {

        Rasterization.sortVerticesByY(arrX, arrY, arrZ, lightIntensities, texCoords);

        for (int y = arrY[0]; y <= arrY[2]; y++) {
            if (y < 0 || y >= zBuffer[0].length) continue;

            int x1, x2;
            float z1, z2, i1, i2;
            float u1, v1, u2, v2;

            if (y <= arrY[1]) {
                x1 = Rasterization.interpolateX(y, arrY[0], arrY[1], arrX[0], arrX[1]);
                x2 = Rasterization.interpolateX(y, arrY[0], arrY[2], arrX[0], arrX[2]);
                z1 = Rasterization.interpolate(y, arrY[0], arrY[1], arrZ[0], arrZ[1]);
                z2 = Rasterization.interpolate(y, arrY[0], arrY[2], arrZ[0], arrZ[2]);
                i1 = Rasterization.interpolate(y, arrY[0], arrY[1], lightIntensities[0], lightIntensities[1]);
                i2 = Rasterization.interpolate(y, arrY[0], arrY[2], lightIntensities[0], lightIntensities[2]);
                u1 = Rasterization.interpolate(y, arrY[0], arrY[1], texCoords[0].x, texCoords[1].x);
                v1 = 1.0f - Rasterization.interpolate(y, arrY[0], arrY[1], texCoords[0].y, texCoords[1].y);
                u2 = Rasterization.interpolate(y, arrY[0], arrY[2], texCoords[0].x, texCoords[2].x);
                v2 = 1.0f - Rasterization.interpolate(y, arrY[0], arrY[2], texCoords[0].y, texCoords[2].y);
            } else {
                x1 = Rasterization.interpolateX(y, arrY[1], arrY[2], arrX[1], arrX[2]);
                x2 = Rasterization.interpolateX(y, arrY[0], arrY[2], arrX[0], arrX[2]);
                z1 = Rasterization.interpolate(y, arrY[1], arrY[2], arrZ[1], arrZ[2]);
                z2 = Rasterization.interpolate(y, arrY[0], arrY[2], arrZ[0], arrZ[2]);
                i1 = Rasterization.interpolate(y, arrY[1], arrY[2], lightIntensities[1], lightIntensities[2]);
                i2 = Rasterization.interpolate(y, arrY[0], arrY[2], lightIntensities[0], lightIntensities[2]);
                u1 = Rasterization.interpolate(y, arrY[1], arrY[2], texCoords[1].x, texCoords[2].x);
                v1 = 1.0f - Rasterization.interpolate(y, arrY[1], arrY[2], texCoords[1].y, texCoords[2].y);
                u2 = Rasterization.interpolate(y, arrY[0], arrY[2], texCoords[0].x, texCoords[2].x);
                v2 = 1.0f - Rasterization.interpolate(y, arrY[0], arrY[2], texCoords[0].y, texCoords[2].y);
            }

            if (x1 > x2) {
                int tempX = x1;
                x1 = x2;
                x2 = tempX;
                float tempZ = z1;
                z1 = z2;
                z2 = tempZ;
                float tempI = i1;
                i1 = i2;
                i2 = tempI;
                float tempU = u1;
                u1 = u2;
                u2 = tempU;
                float tempV = v1;
                v1 = v2;
                v2 = tempV;
            }

            for (int x = Math.max(0, x1); x <= Math.min(zBuffer.length - 1, x2); x++) {
                float z = Rasterization.interpolate(x, x1, x2, z1, z2);
                float intensity = useLighting ? Rasterization.interpolate(x, x1, x2, i1, i2) : 1.0f;
                float u = Rasterization.interpolate(x, x1, x2, u1, u2);
                float v = Rasterization.interpolate(x, x1, x2, v1, v2);

                if (z < zBuffer[x][y]) {
                    zBuffer[x][y] = z;
                    float uClamped = Math.min(1.0f, Math.max(0.0f, u));
                    float vClamped = Math.min(1.0f, Math.max(0.0f, v));
                    int textureX = (int) (uClamped * (texture.getWidth() - 1));
                    int textureY = (int) (vClamped * (texture.getHeight() - 1));
                    Color textureColor = textureReader.getColor(textureX, textureY);

                    double avgIntensity = 0.5 * (intensity + 1.0);
                    Color smoothedColor = textureColor.interpolate(Color.BLACK, 1 - avgIntensity);
                    graphicsContext.getPixelWriter().setColor(x, y, smoothedColor);
                }
            }
        }
    }
}
