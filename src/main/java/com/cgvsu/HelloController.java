package com.cgvsu;

import com.cgvsu.container.ModelContainer;
import com.cgvsu.math.Vector2f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.render_engine.Camera;
import com.cgvsu.render_engine.RenderEngine;
import com.cgvsu.objwriter.ObjWriter;
import com.cgvsu.triangulation.EarClipping;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.vecmath.Vector3f;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class HelloController {
    private boolean showPoligonalGrid = false;
    private boolean showTexture = false;
    final private float TRANSLATION = 0.5F;

    @FXML
    AnchorPane anchorPane;

    @FXML
    private Canvas canvas;

    @FXML
    private CheckBox lighting;

    @FXML
    private CheckBox poligonalGrid;
    @FXML
    private CheckBox texture;

    @FXML
    private TextField pointOfDirX;

    @FXML
    private TextField pointOfDirY;

    @FXML
    private TextField pointOfDirZ;

    @FXML
    private TextField positionX;

    @FXML
    private TextField positionY;

    @FXML
    private TextField positionZ;

    @FXML
    private TextField rotateX;

    @FXML
    private TextField rotateY;

    @FXML
    private TextField rotateZ;

    @FXML
    private TextField scaleX;

    @FXML
    private TextField scaleY;

    @FXML
    private TextField scaleZ;

    @FXML
    private TextField translateX;

    @FXML
    private TextField translateY;

    @FXML
    private TextField translateZ;

    private Model mesh = null;
    private List<Model> meshes = new ArrayList<>();
    @FXML
    private javafx.scene.control.TextField index;

    @FXML
    private VBox vboxModel;

    @FXML
    private VBox vboxCamera;
    private ObservableList<HBox> hboxesMod = FXCollections.observableArrayList();
    private ObservableList<HBox> hboxesCam = FXCollections.observableArrayList();
    private int modelCounter = 1;
    private int cameraCounter = 1;
    private final int MAX_MODELS = 4;
    private final int MAX_CAMERAS = 2;

    private ObservableList<ModelContainer> modelContainers = FXCollections.observableArrayList();


    private Camera camera = new Camera(
            new Vector3f(0, 0, 100),
            new Vector3f(0, 0, 0),
            1.0F, 1, 0.01F, 100);

    private Timeline timeline;

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        canvas.setFocusTraversable(true);
        canvas.setOnMouseClicked(event -> {
            if (!canvas.isFocused()) {
                canvas.requestFocus();
                event.consume();
            }
        });

        positionX.setOnKeyReleased(event -> handlePositionChange("x"));
        positionY.setOnKeyReleased(event -> handlePositionChange("y"));
        positionZ.setOnKeyReleased(event -> handlePositionChange("z"));

        pointOfDirX.setOnKeyReleased(event -> handlePointToDirChange("x"));
        pointOfDirY.setOnKeyReleased(event -> handlePointToDirChange("y"));
        pointOfDirZ.setOnKeyReleased(event -> handlePointToDirChange("z"));

        scaleX.setOnKeyReleased(event -> handleScaleChange("x"));
        scaleY.setOnKeyReleased(event -> handleScaleChange("y"));
        scaleZ.setOnKeyReleased(event -> handleScaleChange("z"));

        rotateX.setOnKeyReleased(event -> handleRotateChange("x"));
        rotateY.setOnKeyReleased(event -> handleRotateChange("y"));
        rotateZ.setOnKeyReleased(event -> handleRotateChange("z"));

        translateX.setOnKeyReleased(event -> handleTranslateChange("x"));
        translateY.setOnKeyReleased(event -> handleTranslateChange("y"));
        translateZ.setOnKeyReleased(event -> handleTranslateChange("z"));

        poligonalGrid.setOnAction(event -> showPoligonalGrid = poligonalGrid.isSelected());
        texture.setOnAction(event -> showTexture = texture.isSelected());

        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            camera.setAspectRatio((float) (width / height));

            for (ModelContainer container : modelContainers) {
                RenderEngine.render(canvas.getGraphicsContext2D(), camera, container.mesh, (int) width, (int) height);

                // Рисуем полигональную сетку, если включена
                if (showPoligonalGrid) {
                    renderPoligonalGrid(canvas.getGraphicsContext2D(), container.mesh, (int) width, (int) height);
                }

                // Рисуем текстуру, если включена
                if (showTexture) {
                    renderTexture(canvas.getGraphicsContext2D(), container.mesh, (int) width, (int) height);
                }
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();
    }
    private void renderPoligonalGrid(GraphicsContext gc, Model model, int width, int height) {
        List<com.cgvsu.math.Vector3f> vertices = model.getVertices();
        List<Polygon> polygons = model.getTriangulatedPolygons();

        gc.setStroke(Color.BLACK);
        for (Polygon polygon : polygons) {
            List<com.cgvsu.math.Vector3f> points = polygon.getPoints(vertices);
            for (int i = 0; i < points.size(); i++) {
                com.cgvsu.math.Vector3f p1 = points.get(i);
                com.cgvsu.math.Vector3f p2 = points.get((i + 1) % points.size());

                // Преобразуем координаты и рисуем линии
                int x1 = (int) (p1.x * width);
                int y1 = (int) (p1.y * height);
                int x2 = (int) (p2.x * width);
                int y2 = (int) (p2.y * height);

                gc.strokeLine(x1, y1, x2, y2);
            }
        }
    }

    // Логика для отрисовки текстуры
    private void renderTexture(GraphicsContext gc, Model model, int width, int height) {
        // Получаем данные текстуры из модели
        Image texture = model.getTexture();
        if (texture == null) {
            return; // Если текстуры нет, ничего не делаем
        }

        List<com.cgvsu.math.Vector3f> vertices = model.getVertices();
        List<Vector2f> texCoords = model.getTextureVertices();

        List<Polygon> polygons = model.getTriangulatedPolygons();

        for (Polygon polygon : polygons) {
            List<com.cgvsu.math.Vector3f> points = polygon.getPoints();
            List<Vector2f> uvs = polygon.getTexCoords();

            // Преобразуем координаты для рисования
            double[] xPoints = new double[points.size()];
            double[] yPoints = new double[points.size()];
            for (int i = 0; i < points.size(); i++) {
                xPoints[i] = points.get(i).x * width;
                yPoints[i] = points.get(i).y * height;
            }

            // Используем текстурные координаты для наложения текстуры
            gc.drawImage(texture,
                         0, 0, texture.getWidth(), texture.getHeight(),
                         xPoints[0], yPoints[0], width, height);
        }
    }

    @FXML
    private void onOpenModelMenuItemClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Load Model");

        File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        Path fileName = Path.of(file.getAbsolutePath());

        try {
            String fileContent = Files.readString(fileName);
            mesh = ObjReader.read(fileContent);
            meshes.add(mesh);
        } catch (IOException exception) {
            throw new RuntimeException("Неверный файл");
        }
    }

    private void handlePositionChange(String axis) {
        try {
            float value = Float.parseFloat(getTextFieldValue(axis + "Position"));
            updateCamPosition(axis, value);
        } catch (NumberFormatException e) {
            showErrorAlert("Предупреждение","Неверный ввод координаты");
        }
    }

    private void handlePointToDirChange(String axis) {
        try {
            float value = Float.parseFloat(getTextFieldValue(axis + "PointToDir"));
            updateCamPosition(axis, value);
        } catch (NumberFormatException e) {
            showErrorAlert("Предупреждение","Неверный ввод координаты");
        }
    }

    private void handleScaleChange(String axis) {
        try {
            float value = Float.parseFloat(getTextFieldValue(axis + "Scale"));
            updateModelPosition(axis, value);
        } catch (NumberFormatException e) {
            showErrorAlert("Предупреждение","Неверный ввод координаты");
        }
    }

    private void handleRotateChange(String axis) {
        try {
            float value = Float.parseFloat(getTextFieldValue(axis + "Rotate"));
            updateModelPosition(axis, value);
        } catch (NumberFormatException e) {
            showErrorAlert("Предупреждение","Неверный ввод координаты");
        }
    }

    private void handleTranslateChange(String axis) {
        try {
            float value = Float.parseFloat(getTextFieldValue(axis + "Translate"));
            updateModelPosition(axis, value);
        } catch (NumberFormatException e) {
            showErrorAlert("Предупреждение","Неверный ввод координаты");
        }
    }

    private String getTextFieldValue(String axis) {
        switch (axis) {
            case "xPosition": return positionX.getText();
            case "yPosition": return positionY.getText();
            case "zPosition": return positionZ.getText();

            case "xPointToDir": return pointOfDirX.getText();
            case "yPointToDir": return pointOfDirY.getText();
            case "zPointToDir": return pointOfDirZ.getText();

            case "xScale": return scaleX.getText();
            case "yScale": return scaleY.getText();
            case "zScale": return scaleZ.getText();

            case "xRotate": return rotateX.getText();
            case "yRotate": return rotateY.getText();
            case "zRotate": return rotateZ.getText();

            case "xTranslate": return translateX.getText();
            case "yTranslate": return translateY.getText();
            case "zTranslate": return translateZ.getText();

            default: return "";
        }
    }

    private void updateModelPosition(String axis, float value) {
        switch (axis) {
            case "x":
                //mesh.translate(new Vector3f(value, 0, 0));
                break;
            case "y":
                //mesh.translate(new Vector3f(0, value, 0));
                break;
            case "z":
                //mesh.translate(new Vector3f(0, 0, value));
                break;
        }
        // Обновить отображение вашей модели на канвасе
        //renderScene();
    }

    private void updateCamPosition(String axis, float value) {
        switch (axis) {
            case "x":
                camera.setPosition(new Vector3f(value, 0, 0));
                break;
            case "y":
                camera.setPosition(new Vector3f(0, value, 0));
                break;
            case "z":
                camera.setPosition(new Vector3f(0, 0, value));
                break;
        }
        // Обновить отображение вашей камеры на канвасе
        //renderScene();
    }
    @FXML
    private void addHBoxModel() {
        onOpenModelMenuItemClick();
        if (mesh == null) {
            return;
        }
        if (hboxesMod.size() >= MAX_MODELS) {
            showAlert("Предупреждение", "Вы достигли максимального количества моделей (4).");
            return;
        }
        if (hboxesMod.isEmpty()){
            modelCounter = 1;
        }
        HBox hboxMod = new HBox(10);

        Button modelButton = new Button("Модель " + modelCounter);
        Button saveObjModInFileButton = new Button("Сохранить");
        Button deleteModButton = new Button("Удалить");
        Button addTextureButton = new Button("Добавить текстуру");
        Button deleteTextureButton = new Button("Удалить текстуру");
        //TextField deleteVertexButton = new TextField("Удалить вершину");

        saveObjModInFileButton.setOnAction(e -> saveModelToFile(mesh));
        deleteModButton.setOnAction(e -> removeHBoxMod(hboxMod));
        //deleteVertexButton.setOnAction(e -> deleteButtonIsPressed(deleteVertexButton));

        hboxMod.getChildren().addAll(modelButton, saveObjModInFileButton, deleteModButton, addTextureButton, deleteTextureButton);

        hboxesMod.add(hboxMod);
        vboxModel.getChildren().add(hboxMod);
        modelCounter++;
        modelContainers.add(new ModelContainer(hboxMod, mesh));
    }
    private void saveModelToFile(Model mesh) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save OBJ Model");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("OBJ files (*.obj)", "*.obj")
        );

        File file = fileChooser.showSaveDialog((Stage) canvas.getScene().getWindow());
        if (file != null) {
            ObjWriter.write(mesh, file.getAbsolutePath()); // Используем ваш ObjWriter
            showSuccessAlert("Успех!", "Модель успешно сохранена в файл: " + file.getAbsolutePath());
        } else {
            showErrorAlert("Ошибка!", "Ошибка при сохранении модели");
        }
    }

    private void removeHBoxMod(HBox hboxMod) {
        ModelContainer containerToRemove = null;
        for(ModelContainer container : modelContainers){
            if(container.hbox == hboxMod){
                containerToRemove = container;
                break;
            }
        }
        if(containerToRemove != null) {
            modelContainers.remove(containerToRemove);
            hboxesMod.remove(hboxMod);
            vboxModel.getChildren().remove(hboxMod);
            meshes.remove(containerToRemove.mesh);
        }
    }

    @FXML
    private void addHBoxCamera() {
        if (hboxesCam.size() >= MAX_CAMERAS) {
            showAlert("Предупреждение", "Вы достигли максимального количества камер (2).");
            return;
        }
        if (hboxesCam.isEmpty()){
            cameraCounter = 1;
        }
        HBox hboxCam = new HBox(10);

        Button camButton = new Button("Камера " + cameraCounter);
        Button deleteCamButton = new Button("Удалить");


        deleteCamButton.setOnAction(e -> removeHBoxCam(hboxCam));

        hboxCam.getChildren().addAll(camButton, deleteCamButton);

        hboxesCam.add(hboxCam);
        vboxCamera.getChildren().add(hboxCam);
        cameraCounter++;
    }


    private void removeHBoxCam(HBox hboxCam) {
        hboxesCam.remove(hboxCam);
        vboxCamera.getChildren().remove(hboxCam);
    }
    @FXML
    private void deleteButtonIsPressed(TextField deleteVertexButton) {
        //mesh = Eraser.vertexDelete(mesh, List.of(Integer.valueOf(deleteVertexButton.getText())),true,true,true,true);
    }

    @FXML
    public void handleCameraForward(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, 0, -TRANSLATION));
    }

    @FXML
    public void handleCameraBackward(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, 0, TRANSLATION));
    }

    @FXML
    public void handleCameraLeft(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraRight(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(-TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraUp(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, TRANSLATION, 0));
    }

    @FXML
    public void handleCameraDown(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, -TRANSLATION, 0));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
