package com.cgvsu.container;

import com.cgvsu.model.Model;
import javafx.scene.layout.HBox;

public class ModelContainer {
    public HBox hbox;
    public Model mesh;

    public ModelContainer(HBox hbox, Model mesh) {
        this.hbox = hbox;
        this.mesh = mesh;
    }
}
