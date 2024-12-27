package com.cgvsu.triangulation;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class DrawWireframe {

    public static void drawWireframe(GraphicsContext gc, double[][] zBuffer, List<Integer> allX, List<Integer> allY, List<Float> allZ) {
        // Настройки графического контекста
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(1.0);
        int[] xCoords = new int[3];
        int[] yCoords = new int[3];
        float[] zCoords = new float[3];
        for (int i = 0; i < allX.size(); i += 3) {
            // Заполняем массивы координат для текущего треугольника
            for (int j = 0; j < 3; j++) {
                xCoords[j] = allX.get(i + j);
                yCoords[j] = allY.get(i + j);
                zCoords[j] = allZ.get(i + j);
            }

            // Соединяем вершины треугольника с учётом Z-буфера
            for (int j = 0; j < 3; j++) {
                int next = (j + 1) % 3;
                drawLineWithZBuffer(
                        gc, xCoords[j], yCoords[j], zCoords[j],
                        xCoords[next], yCoords[next], zCoords[next],
                        zBuffer
                );
            }
        }
    }


    public static void drawLineWithZBuffer(GraphicsContext gc, int x0, int y0, float z0, int x1, int y1, float z1,
                                           double[][] zBuffer) {
        int width = zBuffer.length;
        int height = zBuffer[0].length;

        // Проверка, находятся ли обе вершины в пределах Z-буфера и подходят ли они
        boolean startVisible = x0 >= 0 && x0 < width && y0 >= 0 && y0 < height && z0 <= zBuffer[x0][y0];
        boolean endVisible = x1 >= 0 && x1 < width && y1 >= 0 && y1 < height && z1 <= zBuffer[x1][y1];

        if (startVisible && endVisible) {
            // Если обе вершины видимы, рисуем линию без проверки Z-буфера
            drawLineWithoutZBufferCheck(gc, x0, y0, x1, y1);
            return;
        }

        // Если хотя бы одна вершина невидима, выполняем обычную логику
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;
        float dz = z1 - z0;
        float dzx = dx != 0 ? Math.abs(dz / dx) : 0;
        float dzy = dy != 0 ? Math.abs(dz / dy) : 0;
        float z = z0;

        while (true) {
            // Проверка границ Z-буфера
            if (x0 >= 0 && x0 < width && y0 >= 0 && y0 < height) {
                // Проверяем и обновляем Z-буфер
                if (z <= zBuffer[x0][y0]) {
                    zBuffer[x0][y0] = z;
                    gc.getPixelWriter().setColor(x0, y0, Color.YELLOW);
                }
            }

            if (x0 == x1 && y0 == y1) break;

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x0 += sx;
                z += dzx * sx;
            }
            if (e2 < dx) {
                err += dx;
                y0 += sy;
                z += dzy * sy;
            }
        }
    }

    // Вспомогательный метод для рисования линии без проверки Z-буфера
    private static void drawLineWithoutZBufferCheck(GraphicsContext gc, int x0, int y0, int x1, int y1) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;

        while (true) {
            gc.getPixelWriter().setColor(x0, y0, Color.YELLOW);

            if (x0 == x1 && y0 == y1) break;

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x0 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y0 += sy;
            }
        }
    }
}
