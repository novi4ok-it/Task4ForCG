package com.cgvsu.render_engine;

import com.cgvsu.math.Vector3f;
import javafx.scene.paint.Color;

public class ColorLighting {
    public Vector3f light;
    public Color color;

    public ColorLighting(Vector3f light, Color color) {
        this.light = light;
        this.color = color;
    }
}
