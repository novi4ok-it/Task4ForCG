package com.cgvsu;

import com.cgvsu.container.ModelContainer;
import com.cgvsu.math.Matrix4f;
import com.cgvsu.math.Point2f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import com.cgvsu.normal.FindNormals;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.render_engine.Camera;
import com.cgvsu.render_engine.CameraManager;
import com.cgvsu.render_engine.RenderEngine;
import com.cgvsu.objwriter.ObjWriter;
import com.cgvsu.triangulation.Triangulation;
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

import com.cgvsu.math.Vector3f;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.cgvsu.render_engine.GraphicConveyor.*;

public class HelloController {

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
    private final int MAX_CAMERAS = 4;

    private ObservableList<ModelContainer> modelContainers = FXCollections.observableArrayList();

    private CameraManager cameraManager = new CameraManager();

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

        poligonalGrid.selectedProperty().addListener((observable, oldValue, newValue) -> {
            isPolygonalGridEnabled = newValue;
            renderScene();
        });
        lighting.selectedProperty().addListener((observable, oldValue, newValue) -> {
            isLightingEnabled = newValue; // Обновляем флаг освещения
            renderScene(); // Перерисовываем сцену с учетом нового состояния освещения
        });
        texture.selectedProperty().addListener((observable, oldValue, newValue) -> {
            isTextureEnabled = newValue; // Обновляем состояние текстур
            renderScene(); // Перерисовываем сцену
        });
        // Анимация для обновления кадра
        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> renderScene());
        timeline.getKeyFrames().add(frame);
        timeline.play();
    }
    private double[][] zBuffer;

    private void initializeZBuffer(int width, int height) {
        zBuffer = new double[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                zBuffer[x][y] = Double.POSITIVE_INFINITY; // Инициализируем максимальной глубиной
            }
        }
    }

    private void drawWireframe(GraphicsContext gc, Model mesh, Camera camera, int width, int height) {
        // Инициализируем Z-буфер
        initializeZBuffer(width, height);

        Matrix4f modelMatrix = rotateScaleTranslate();
        Matrix4f viewMatrix = camera.getViewMatrix();
        Matrix4f projectionMatrix = camera.getProjectionMatrix();

        Matrix4f modelViewProjectionMatrix = new Matrix4f(modelMatrix);
        modelViewProjectionMatrix.mul(viewMatrix);
        modelViewProjectionMatrix.mul(projectionMatrix);

        gc.setStroke(Color.GRAY);
        gc.setLineWidth(1.0);

        for (Polygon triangle : mesh.polygons) {
            final int nVertices = triangle.getVertexIndices().size();
            double[] xCoords = new double[nVertices];
            double[] yCoords = new double[nVertices];
            float[] zCoords = new float[nVertices];
            Vector3f[] transformedVertices = new Vector3f[nVertices];

            for (int i = 0; i < nVertices; i++) {
                int vertexIndex = triangle.getVertexIndices().get(i);
                Vector3f vertex = mesh.vertices.get(vertexIndex);

                Vector3f transformedVertex = multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertex);
                transformedVertices[i] = transformedVertex;
                Point2f screenPoint = vertexToPoint(transformedVertex, width, height);

                xCoords[i] = screenPoint.x;
                yCoords[i] = screenPoint.y;
                zCoords[i] = transformedVertex.z; // Сохраняем глубину
            }

            // Проверяем ориентацию нормали
            if (!isFrontFacing(transformedVertices)) {
                continue; // Пропускаем невидимые треугольники
            }

            // Соединяем вершины треугольника с учетом Z-буфера
            for (int i = 0; i < nVertices; i++) {
                int next = (i + 1) % nVertices;
                drawLineWithZBuffer(gc, (int) xCoords[i], (int) yCoords[i], zCoords[i], (int) xCoords[next], (int) yCoords[next], zCoords[next]);
            }
        }
    }

    private boolean isFrontFacing(Vector3f[] vertices) {
        Vector3f v0 = vertices[0];
        Vector3f v1 = vertices[1];
        Vector3f v2 = vertices[2];

        // Вычисляем нормаль плоскости
        Vector3f edge1 = Vector3f.subtraction(v1, v0);
        Vector3f edge2 = Vector3f.subtraction(v2, v0);
        Vector3f normal = Vector3f.vectorProduct(edge1, edge2);

        // Проверяем, направлена ли нормаль к камере
        return normal.z < 0; // Считаем, что камера смотрит в направлении -Z
    }

    private void drawLineWithZBuffer(GraphicsContext gc, int x0, int y0, float z0, int x1, int y1, float z1) {
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
                    gc.getPixelWriter().setColor(x0, y0, Color.GRAY);
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

    private boolean isLightingEnabled = false;


    // Основной рендер сцены
    private void renderScene() {
        double width = canvas.getWidth();
        double height = canvas.getHeight();

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);

        // Получаем активную камеру
        Camera activeCamera = cameraManager.getActiveCamera();
        if (activeCamera != null) {
            activeCamera.setAspectRatio((float) (width / height));
        }

        for (ModelContainer container : modelContainers) {
            // Сначала закрашиваем полигоны
            RenderEngine.render(gc, activeCamera, container.mesh, (int) width, (int) height, isLightingEnabled, isTextureEnabled);
            // Затем рисуем поверх них триангулированную сетку (если включена)
            if (isPolygonalGridEnabled) {
                drawWireframe(gc, container.mesh, activeCamera, (int) width, (int) height);
            }
        }
    }

    // Флаг для отображения триангулированной сетки
    private boolean isPolygonalGridEnabled = false;

    private void triangulateModel() {
        // Преобразуем полигоны модели в треугольники
        if (mesh != null) {
            List<Polygon> triangulatedPolygons = new ArrayList<>();

            for (Polygon polygon : mesh.polygons) {
                List<Polygon> triangles = Triangulation.triangulate(polygon, mesh.vertices);
                triangulatedPolygons.addAll(triangles);
            }

            // Перезаписываем полигоны с результатами триангуляции
            mesh.polygons = (ArrayList<Polygon>) triangulatedPolygons;
        }
    }

    private boolean isTextureEnabled = false;

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
            triangulateModel();
            // Пересчёт нормалей
            List<Vector3f> recalculatedNormals = FindNormals.findNormals(mesh.polygons, mesh.vertices);
            mesh.normals.clear();
            mesh.normals.addAll(recalculatedNormals);

            // Обновляем индексы нормалей для каждого полигона
            for (Polygon polygon : mesh.polygons) {
                List<Integer> normalIndices = new ArrayList<>();
                for (int vertexIndex : polygon.getVertexIndices()) {
                    normalIndices.add(vertexIndex); // Индексы нормалей совпадают с индексами вершин
                }
                polygon.setNormalIndices(new ArrayList<>(normalIndices));
            }

        } catch (IOException exception) {
            throw new RuntimeException("Неверный файл");
        }
    }


    private void handlePositionChange(String axis) {
        try {
            float value = Float.parseFloat(getTextFieldValue(axis + "Position"));
            updateCamPosition(axis, value);
        } catch (NumberFormatException e) {
            showErrorAlert("Предупреждение", "Неверный ввод координаты");
        }
    }

    private void handlePointToDirChange(String axis) {
        try {
            float value = Float.parseFloat(getTextFieldValue(axis + "PointToDir"));
            updateCamPosition(axis, value);
        } catch (NumberFormatException e) {
            showErrorAlert("Предупреждение", "Неверный ввод координаты");
        }
    }

    private void handleScaleChange(String axis) {
        try {
            float value = Float.parseFloat(getTextFieldValue(axis + "Scale"));
            updateModelPosition(axis, value);
        } catch (NumberFormatException e) {
            showErrorAlert("Предупреждение", "Неверный ввод координаты");
        }
    }

    private void handleRotateChange(String axis) {
        try {
            float value = Float.parseFloat(getTextFieldValue(axis + "Rotate"));
            updateModelPosition(axis, value);
        } catch (NumberFormatException e) {
            showErrorAlert("Предупреждение", "Неверный ввод координаты");
        }
    }

    private void handleTranslateChange(String axis) {
        try {
            float value = Float.parseFloat(getTextFieldValue(axis + "Translate"));
            updateModelPosition(axis, value);
        } catch (NumberFormatException e) {
            showErrorAlert("Предупреждение", "Неверный ввод координаты");
        }
    }

    private String getTextFieldValue(String axis) {
        switch (axis) {
            case "xPosition":
                return positionX.getText();
            case "yPosition":
                return positionY.getText();
            case "zPosition":
                return positionZ.getText();

            case "xPointToDir":
                return pointOfDirX.getText();
            case "yPointToDir":
                return pointOfDirY.getText();
            case "zPointToDir":
                return pointOfDirZ.getText();

            case "xScale":
                return scaleX.getText();
            case "yScale":
                return scaleY.getText();
            case "zScale":
                return scaleZ.getText();

            case "xRotate":
                return rotateX.getText();
            case "yRotate":
                return rotateY.getText();
            case "zRotate":
                return rotateZ.getText();

            case "xTranslate":
                return translateX.getText();
            case "yTranslate":
                return translateY.getText();
            case "zTranslate":
                return translateZ.getText();

            default:
                return "";
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
                cameraManager.getActiveCamera().setPosition(new Vector3f(value, 0, 0));
                break;
            case "y":
                cameraManager.getActiveCamera().setPosition(new Vector3f(0, value, 0));
                break;
            case "z":
                cameraManager.getActiveCamera().setPosition(new Vector3f(0, 0, value));
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
        if (hboxesMod.isEmpty()) {
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
        addTextureButton.setOnAction(e -> addTexture(hboxMod, mesh));
        //deleteVertexButton.setOnAction(e -> deleteButtonIsPressed(deleteVertexButton));

        hboxMod.getChildren().addAll(modelButton, saveObjModInFileButton, deleteModButton, addTextureButton, deleteTextureButton);

        hboxesMod.add(hboxMod);
        vboxModel.getChildren().add(hboxMod);
        modelCounter++;
        modelContainers.add(new ModelContainer(hboxMod, mesh));
    }


    private void addTexture(HBox hboxMod, Model model) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Texture (*.png)", "*.png", "*.jpg"));

        File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());
        if (file == null) {
            return; // Если файл не выбран, просто выйти из метода
        }

        try {
            // Загружаем изображение из файла
            Image texture = new Image(file.toURI().toString());
            // Устанавливаем текстуру для модели
            model.texture = texture;
        } catch (Exception exception) {
            throw new RuntimeException("Ошибка при загрузке текстуры: " + exception.getMessage(), exception);
        }
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
        for (ModelContainer container : modelContainers) {
            if (container.hbox == hboxMod) {
                containerToRemove = container;
                break;
            }
        }
        if (containerToRemove != null) {
            modelContainers.remove(containerToRemove);
            hboxesMod.remove(hboxMod);
            vboxModel.getChildren().remove(hboxMod);
            meshes.remove(containerToRemove.mesh);
        }
    }

    @FXML
    private void addHBoxCamera() {
        if (hboxesCam.size() >= MAX_CAMERAS) {
            showAlert("Предупреждение", "Вы достигли максимального количества камер (4).");
            return;
        }
        if (hboxesCam.isEmpty()) {
            cameraCounter = 1;
        }
        HBox hboxCam = new HBox(10);

        Camera camera = new Camera(
                new Vector3f(0, 0, 100),
                new Vector3f(0, 0, 0),
                1.0F, 1, 0.01F, 100);
        cameraManager.addCamera(camera);

        int cameraIndex = cameraManager.getCameras().size() - 1; // Индекс только что добавленной камеры

        Button camButton = new Button("Камера " + cameraCounter);
        Button deleteCamButton = new Button("Удалить");

        // Обработчик переключения на текущую камеру
        camButton.setOnAction(e -> {
            cameraManager.setActiveCamera(cameraIndex);
            renderScene();
        });

        // Обработчик удаления камеры
        deleteCamButton.setOnAction(e -> {
            removeHBoxCam(hboxCam);
            cameraManager.removeCamera(cameraIndex);
        });

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
        cameraManager.getActiveCamera().movePosition(new Vector3f(0, 0, -TRANSLATION));
    }

    @FXML
    public void handleCameraBackward(ActionEvent actionEvent) {
        cameraManager.getActiveCamera().movePosition(new Vector3f(0, 0, TRANSLATION));
    }

    @FXML
    public void handleCameraLeft(ActionEvent actionEvent) {
        cameraManager.getActiveCamera().movePosition(new Vector3f(TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraRight(ActionEvent actionEvent) {
        cameraManager.getActiveCamera().movePosition(new Vector3f(-TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraUp(ActionEvent actionEvent) {
        cameraManager.getActiveCamera().movePosition(new Vector3f(0, TRANSLATION, 0));
    }

    @FXML
    public void handleCameraDown(ActionEvent actionEvent) {
        cameraManager.getActiveCamera().movePosition(new Vector3f(0, -TRANSLATION, 0));
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
