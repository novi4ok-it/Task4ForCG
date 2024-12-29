package com.cgvsu.triangulation;

import com.cgvsu.math.Vector3f;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class DrawWireframe {

    public static void drawWireframe(GraphicsContext gc, double[][] zBuffer, List<Integer> allX, List<Integer> allY) {
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
            }

            if (!isFrontFacing(xCoords, yCoords, zCoords)) {
                continue; // Пропускаем невидимые треугольники
            }

            // Соединяем вершины треугольника с учетом Z-буфера
            for (int j = 0; j < 3; j++) {
                int next = (j + 1) % 3;
                drawLineWithZBuffer(gc, (int) xCoords[j], (int) yCoords[j], zCoords[j], (int) xCoords[next], (int) yCoords[next], zCoords[next], zBuffer);
            }
        }
    }

    private static boolean isFrontFacing(int[] xCoords, int[] yCoords, float[] zCoords) {
        // Вершины
        int x0 = xCoords[0], y0 = yCoords[0], z0 = (int) zCoords[0];
        int x1 = xCoords[1], y1 = yCoords[1], z1 = (int) zCoords[1];
        int x2 = xCoords[2], y2 = yCoords[2], z2 = (int) zCoords[2];

        // Вычисляем векторы ребер
        int edge1X = x1 - x0, edge1Y = y1 - y0, edge1Z = z1 - z0;
        int edge2X = x2 - x0, edge2Y = y2 - y0, edge2Z = z2 - z0;

        // Вычисляем нормаль плоскости
        float normalX = edge1Y * edge2Z - edge1Z * edge2Y;
        float normalY = edge1Z * edge2X - edge1X * edge2Z;
        float normalZ = edge1X * edge2Y - edge1Y * edge2X;

        // Проверяем, направлена ли нормаль к камере
        return normalZ < 0; // Считаем, что камера смотрит в направлении -Z
    }

    public static void drawLineWithZBuffer(GraphicsContext gc, int x0, int y0, float z0, int x1, int y1, float z1, double[][] zBuffer) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;

        while (true) {
            if (x0 >= 0 && x0 < zBuffer.length && y0 >= 0 && y0 < zBuffer[0].length) {
                float t = (float) (Math.sqrt((x1 - x0) * (x1 - x0) + (y1 - y0) * (y1 - y0)) / Math.sqrt(dx * dx + dy * dy));
                float z = z0 * (1 - t) + z1 * t; // Интерполяция Z

                if (z < zBuffer[x0][y0]) {
                    zBuffer[x0][y0] = z;
                    gc.getPixelWriter().setColor(x0, y0, Color.YELLOW);
                }
            }

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

