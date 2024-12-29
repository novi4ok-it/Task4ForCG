package com.cgvsu;

import com.cgvsu.container.ModelContainer;
import com.cgvsu.math.*;
import com.cgvsu.math.Matrix4f;
import com.cgvsu.math.Point2f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import com.cgvsu.normal.FindNormals;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.render_engine.*;
import com.cgvsu.render_engine.Camera;
import com.cgvsu.render_engine.CameraManager;
import com.cgvsu.render_engine.GraphicConveyor;
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
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class HelloController {

    final private float TRANSLATION = 0.5F;

    @FXML
    AnchorPane anchorPane;

    @FXML
    private Canvas canvas;

    @FXML
    private Button addCameraButton;

    @FXML
    private Button addLightButton;

    private List<ColorLighting> coloredLightSources = new ArrayList<>();

    @FXML
    private Button deleteLightButton;

    @FXML
    private ColorPicker colorPicker;

    private Color selectedColor;

    @FXML
    private CheckBox poligonalGrid;

    @FXML
    private CheckBox texture;

    @FXML
    private CheckBox themeSwitchButton;

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
    private TextField lightingCoordX;

    @FXML
    private TextField lightingCoordY;

    @FXML
    private TextField lightingCoordZ;

    @FXML
    private TextField translateX;

    @FXML
    private TextField translateY;

    @FXML
    private TextField translateZ;

    private Model mesh = null;
    private Model tempMesh = null;
    private List<Model> meshes = new ArrayList<>();


    @FXML
    private VBox vboxModel;

    @FXML
    private VBox vboxCamera;
    private ObservableList<HBox> hboxesMod = FXCollections.observableArrayList();
    private ObservableList<HBox> hboxesCam = FXCollections.observableArrayList();
    private int modelCounter = 1;

    private int activeModelIndex = -1;
    private int cameraCounter = 1;
    private final int MAX_MODELS = 4;
    private final int MAX_CAMERAS = 4;

    private double lastMouseX = 0;
    private double lastMouseY = 0;
    private boolean isMousePressed = false;

    private ObservableList<ModelContainer> modelContainers = FXCollections.observableArrayList();

    private CameraManager cameraManager = new CameraManager();

    private Timeline timeline;
    private List<Integer> selectedVertices = new ArrayList<>();
    private List<Integer> selectedPolygons = new ArrayList<>();


    private boolean fileSelected;
    private boolean windowIsCalled = false;
    private boolean isDarkTheme = false;

    @FXML
    private void initialize() {
        anchorPane.getStylesheets().add(getClass().getResource("/com/cgvsu/fxml/styles.css").toExternalForm());

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

        addLightButton.setOnAction(event -> addLightSource());
        deleteLightButton.setOnAction(event -> deleteLightSource());

        colorPicker.setOnAction(event -> {
            selectedColor = colorPicker.getValue();
        });

        positionX.setOnKeyReleased(event -> handlePositionChange("x"));
        positionY.setOnKeyReleased(event -> handlePositionChange("y"));
        positionZ.setOnKeyReleased(event -> handlePositionChange("z"));

        pointOfDirX.setOnKeyReleased(event -> handlePointToDirChange("x"));
        pointOfDirY.setOnKeyReleased(event -> handlePointToDirChange("y"));
        pointOfDirZ.setOnKeyReleased(event -> handlePointToDirChange("z"));

        canvas.setFocusTraversable(true);
        canvas.setOnMouseClicked(event -> {
            if (!canvas.isFocused()) {
                canvas.requestFocus();
                event.consume();
            }
        });

        poligonalGrid.selectedProperty().addListener((observable, oldValue, newValue) -> {
            isPolygonalGridEnabled = newValue;
            renderScene();
        });
        texture.selectedProperty().addListener((observable, oldValue, newValue) -> {
            isTextureEnabled = newValue; // Обновляем состояние текстур
            renderScene(); // Перерисовываем сцену
        });



        // Анимация для обновления кадра
        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> renderScene());
        timeline.getKeyFrames().add(frame);
        timeline.play();


        themeSwitchButton.setSelected(false);
        themeSwitchButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            switchTheme();
        });
        canvas.setOnMousePressed(event -> handleMousePressed(event));
        canvas.setOnMouseDragged(event -> handleMouseDragged(event));
        canvas.setOnMouseReleased(event -> handleMouseReleased(event));
        canvas.setOnScroll(event -> handleMouseScrolled(event)); // колесо

    }

    // Основной рендер сцены
    private void renderScene() {
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        double[][] zBuffer = initializeZBuffer((int) width, (int) height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);

        // Получаем активную камеру
        Camera activeCamera = cameraManager.getActiveCamera();
        if (activeCamera != null) {
            activeCamera.setAspectRatio((float) (width / height));
        }

        for (ModelContainer container : modelContainers) {
            RenderContext context = new RenderContext(
                    gc, activeCamera, container.mesh, (int) width, (int) height, coloredLightSources, isTextureEnabled, isPolygonalGridEnabled, zBuffer);
            RenderEngine.render(context);
        }
    }

    private static double[][] initializeZBuffer(int width, int height) {
        double[][] zBuffer = new double[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                zBuffer[i][j] = Double.POSITIVE_INFINITY; // Инициализируем максимальной глубиной
            }
        }
        return zBuffer;
    }

    private boolean isPolygonalGridEnabled = false;

    private boolean isTextureEnabled = false;

    // Метод для добавления нового источника света
    private void addLightSource() {
        Vector3f newLight = getLightingCoordinates();
        Color color = (getSelectedColor() != null) ? getSelectedColor() : Color.WHITE;
        ColorLighting colorLighting = new ColorLighting(newLight, color);

        coloredLightSources.add(colorLighting);

        renderScene();
    }

    private void deleteLightSource() {
        if (!coloredLightSources.isEmpty()) {
            coloredLightSources.remove(coloredLightSources.size() - 1);
            renderScene();
        }
    }

    public Color getSelectedColor() {
        return selectedColor;
    }

    @FXML
    private Vector3f getLightingCoordinates() {
        try {
            // Считывание значений из текстовых полей
            float x = Float.parseFloat(lightingCoordX.getText().trim());
            float y = Float.parseFloat(lightingCoordY.getText().trim());
            float z = Float.parseFloat(lightingCoordZ.getText().trim());

            // Создание объекта Vector3f
            return new Vector3f(x, y, z);
        } catch (NumberFormatException e) {
            // Обработка ошибок: неверный формат чисел
            System.err.println("Ошибка ввода координат освещения: " + e.getMessage());
            return new Vector3f(0, 0, 0); // Возвращаем значение по умолчанию
        }
    }

    @FXML
    private void onOpenModelMenuItemClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Load Model");


        File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());
        if (file == null) {
            fileSelected = false;
            return;
        }

        fileSelected = true;

        Path fileName = Path.of(file.getAbsolutePath());

        try {
            String fileContent = Files.readString(fileName);
            mesh = ObjReader.read(fileContent);
            meshes.add(mesh);
            //normalizeTextureCoordinates(mesh.textureVertices);
            Triangulation.triangulateModel(mesh);
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

    private void handleMousePressed(MouseEvent event) {
        lastMouseX = event.getSceneX();
        lastMouseY = event.getSceneY();
        isMousePressed = true;
    }

    private void handleMouseDragged(MouseEvent event) {
        if (isMousePressed) {
            double deltaX = event.getSceneX() - lastMouseX;
            double deltaY = event.getSceneY() - lastMouseY;

            // Обновляем вращение камеры в зависимости от движения мыши
            updateCameraRotation(deltaX, deltaY);

            lastMouseX = event.getSceneX();
            lastMouseY = event.getSceneY();
        }
    }

    private void handleMouseReleased(MouseEvent event) {
        isMousePressed = false;
    }

    private void updateCameraRotation(double deltaX, double deltaY) {
        float sensitivity = 0.5f; // Чувствительность мыши
        float yaw = (float) (-deltaX * sensitivity); // Инвертируем направление вращения по горизонтали
        float pitch = (float) (-deltaY * sensitivity);
        float roll = (float) (deltaY * sensitivity);

        // Обновляем углы вращения камеры
        cameraManager.getActiveCamera().rotateAroundTarget(yaw, pitch, roll);
    }

    @FXML
    private void handleMouseScrolled(ScrollEvent event) {
        double deltaY = event.getDeltaY();
        if (deltaY > 0) {
            cameraManager.getActiveCamera().movePosition(new Vector3f(0, 0, -TRANSLATION));
        } else {
            cameraManager.getActiveCamera().movePosition(new Vector3f(0, 0, TRANSLATION));
        }
    }

    @FXML
    private void applyScale() {
        handleScaleChange("x");
        handleScaleChange("y");
        handleScaleChange("z");
    }

    @FXML
    private void applyRotate() {
        handleRotateChange("x");
        handleRotateChange("y");
        handleRotateChange("z");
    }

    @FXML
    private void applyTranslate() {
        if (!mesh.areVerticesEqual()) {
            mesh.setVertices();
        }
        handleScaleChange("x");
        handleScaleChange("y");
        handleScaleChange("z");
        handleRotateChange("x");
        handleRotateChange("y");
        handleRotateChange("z");
        handleTranslateChange("x");
        handleTranslateChange("y");
        handleTranslateChange("z");
    }

    @FXML
    private void applyCamPosition() {
        handlePositionChange("x");
        handlePositionChange("y");
        handlePositionChange("z");
    }

    @FXML
    private void applyPointOfDir() {
        handlePointToDirChange("x");
        handlePointToDirChange("y");
        handlePointToDirChange("z");
    }


    private void handlePositionChange(String axis) {
        try {
            float value = Float.parseFloat(getTextFieldValue(axis + "Position"));
            updateCamPosition(axis, value);
        } catch (NumberFormatException e) {
            showErrorAlert("Предупреждение", "Пустое поле координаты (или неверное)");
        }
    }

    private void handlePointToDirChange(String axis) {
        try {
            float value = Float.parseFloat(getTextFieldValue(axis + "PointToDir"));
            updateCamPosition(axis, value);
        } catch (NumberFormatException e) {
            showErrorAlert("Предупреждение", "Пустое поле координаты (или неверное)");
        }
    }

    private void handleScaleChange(String axis) {
        try {
            float value = Float.parseFloat(getTextFieldValue(axis + "Scale"));
            updateScale(axis, value);
        } catch (NumberFormatException e) {
            showErrorAlert("Предупреждение", "Пустое поле координаты (или неверное)");
        }
    }

    private void handleRotateChange(String axis) {
        try {
            float value = Float.parseFloat(getTextFieldValue(axis + "Rotate"));
            updateRotation(axis, value);
        } catch (NumberFormatException e) {
            showErrorAlert("Предупреждение", "Пустое поле координаты (или неверное)");
        }
    }

    private void handleTranslateChange(String axis) {
        try {
            float value = Float.parseFloat(getTextFieldValue(axis + "Translate"));
            updateTranslation(axis, value);
        } catch (NumberFormatException e) {
            showErrorAlert("Предупреждение", "Пустое поле координаты (или неверное)");
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

    //////////
    private void updateScale(String axis, float value) {
        switch (axis) {
            case "x":
                mesh.setScale(new Vector3f(value, 1, 1));
                AffineTransformations.applyScale(mesh);
                break;
            case "y":
                mesh.setScale(new Vector3f(1, value, 1));
                AffineTransformations.applyScale(mesh);
                break;
            case "z":
                mesh.setScale(new Vector3f(1, 1, value));
                AffineTransformations.applyScale(mesh);
                break;
        }
        // Обновить отображение вашей модели на канвасе
        //renderScene();
    }

    private void updateRotation(String axis, float value) {
        switch (axis) {
            case "x":
                mesh.setRotation(new Vector3f(value, 0, 0));
                AffineTransformations.applyRotationX(mesh);
                break;
            case "y":
                mesh.setRotation(new Vector3f(0, value, 0));
                AffineTransformations.applyRotationX(mesh);
                break;
            case "z":
                mesh.setRotation(new Vector3f(0, 0, value));
                AffineTransformations.applyRotationX(mesh);
                break;
        }
        // Обновить отображение вашей модели на канвасе
        //renderScene();
    }

    private void updateTranslation(String axis, float value) {
        switch (axis) {
            case "x":
                mesh.setTranslation(new Vector3f(value, 0, 0));
                AffineTransformations.applyTranslationX(mesh);
                break;
            case "y":
                mesh.setTranslation(new Vector3f(0, value, 0));
                AffineTransformations.applyTranslationX(mesh);
                break;
            case "z":
                mesh.setTranslation(new Vector3f(0, 0, value));
                AffineTransformations.applyTranslationX(mesh);
                break;
        }
        // Обновить отображение вашей модели на канвасе
        //renderScene();
    }

    @FXML
    private void addHBoxModel() {
        mesh = null;
        if (!windowIsCalled) {
            onOpenModelMenuItemClick();
        }
        if (!fileSelected || mesh == null) {
            if (mesh == null && tempMesh != null){
                mesh = tempMesh;
            }
            if (mesh == null){
                return;
            }
            if (!fileSelected){
                return;
            }
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


        int currentModelIndex = hboxesMod.size();

        modelButton.setOnAction(e -> selectActiveModel(currentModelIndex));
        saveObjModInFileButton.setOnAction(e -> saveModelToFile(mesh));
        deleteModButton.setOnAction(e -> removeHBoxMod(hboxMod));
        addTextureButton.setOnAction(e -> addTexture());

        hboxMod.getChildren().addAll(modelButton, saveObjModInFileButton, deleteModButton, addTextureButton, deleteTextureButton);

        windowIsCalled = false;
        hboxesMod.add(hboxMod);
        vboxModel.getChildren().add(hboxMod);
        modelCounter++;
        modelContainers.add(new ModelContainer(hboxMod, mesh));
        /////
        scaleX.setText("1");
        scaleY.setText("1");
        scaleZ.setText("1");

        rotateX.setText("0");
        rotateY.setText("0");
        rotateZ.setText("0");

        translateX.setText("0");
        translateY.setText("0");
        translateZ.setText("0");
        /////
    }

    private void selectActiveModel(int modelIndex) {
        if (modelIndex >= 0 && modelIndex < meshes.size()) {
            activeModelIndex = modelIndex;
            System.out.println("Активная модель: " + activeModelIndex);
        } else {
            activeModelIndex = -1;
            System.out.println("Нет активной модели");
        }
    }


    private void addTexture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Texture (*.png)", "*.png", "*.jpg"));

        File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        try {
            // Загружаем изображение из файла
            Image texture = new Image(file.toURI().toString());
            // Устанавливаем текстуру для модели
            if (activeModelIndex != -1){
                meshes.get(activeModelIndex).texture = texture;
            }
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
            scaleX.setText("");
            scaleY.setText("");
            scaleZ.setText("");

            rotateX.setText("");
            rotateY.setText("");
            rotateZ.setText("");

            translateX.setText("");
            translateY.setText("");
            translateZ.setText("");

            modelCounter--;
            modelContainers.remove(containerToRemove);
            hboxesMod.remove(hboxMod);
            vboxModel.getChildren().remove(hboxMod);
            meshes.remove(containerToRemove.mesh);

            updateModelIndices();
        }
    }
    private void updateModelIndices() {
        for (int i = 0; i < hboxesMod.size(); i++) {
            HBox hbox = hboxesMod.get(i);
            Button modelButton = (Button) hbox.getChildren().get(0);
            modelButton.setText("Модель " + (i + 1));
            int finalI = i;
            modelButton.setOnAction(e -> selectActiveModel(finalI));
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
            cameraCounter--;
        });

        hboxCam.getChildren().addAll(camButton, deleteCamButton);

        hboxesCam.add(hboxCam);
        vboxCamera.getChildren().add(hboxCam);
        cameraCounter++;
    }


    private void removeHBoxCam(HBox hboxCam) {
        positionX.setText("");
        positionX.setText("");
        positionX.setText("");

        pointOfDirX.setText("");
        pointOfDirX.setText("");
        pointOfDirX.setText("");
        hboxesCam.remove(hboxCam);
        vboxCamera.getChildren().remove(hboxCam);
    }

    @FXML
    private void switchTheme() {
        if (isDarkTheme) {
            applyLightTheme();
        } else {
            applyDarkTheme();
        }
        isDarkTheme = !isDarkTheme;
    }

    private void applyLightTheme() {
        anchorPane.getStyleClass().remove("dark-theme");
        anchorPane.getStyleClass().add("light-theme");
        anchorPane.getStyleClass().remove("dark-theme-title");
        anchorPane.getStyleClass().add("light-theme-title");
        updateStyleClass("light-theme", "light-theme-title");
    }

    private void applyDarkTheme() {
        anchorPane.getStyleClass().remove("light-theme");
        anchorPane.getStyleClass().add("dark-theme");
        anchorPane.getStyleClass().remove("light-theme-title");
        anchorPane.getStyleClass().add("dark-theme-title");
        updateStyleClass("dark-theme", "dark-theme-title");
    }

    private void updateStyleClass(String themeClass, String themeClass1) {
        Set<Node> nodes = anchorPane.lookupAll(".theme-element");
        for (Node node : nodes) {
            node.getStyleClass().removeAll("light-theme", "dark-theme");
            node.getStyleClass().add(themeClass);
        }

        Set<Node> nodes1 = anchorPane.lookupAll(".theme-element-title");
        for (Node node : nodes1) {
            node.getStyleClass().removeAll("light-theme-title", "dark-theme-title");
            node.getStyleClass().add(themeClass1);
        }
    }

    @FXML
    private void handleDragOver(DragEvent event) {
        if (event.getGestureSource() != canvas && event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
    }

    @FXML
    private void handleDragDropped(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            success = true;
            List<File> files = db.getFiles();
            for (File file : files) {
                if (file.getName().endsWith(".obj")) {
                    processDroppedFileModel(file);
                } else if (file.getName().endsWith(".png") || file.getName().endsWith(".jpg")) {
                    processDroppedFileTexture(file);
                } else {
                    showErrorAlert("Ошибка", "Неподдерживаемый тип файла. Пожалуйста, перетащите файл .obj, .png или .jpg.");
                }
            }
        }
        event.setDropCompleted(success);
        event.consume();
    }

    private void processDroppedFileModel(File file) {
        try {
            String fileContent = Files.readString(file.toPath());
            Model mesh = ObjReader.read(fileContent);
            meshes.add(mesh);
            Triangulation.triangulateModel(mesh);
            List<Vector3f> recalculatedNormals = FindNormals.findNormals(mesh.polygons, mesh.vertices);
            mesh.normals.clear();
            mesh.normals.addAll(recalculatedNormals);

            for (Polygon polygon : mesh.polygons) {
                List<Integer> normalIndices = new ArrayList<>();
                for (int vertexIndex : polygon.getVertexIndices()) {
                    normalIndices.add(vertexIndex);
                }
                polygon.setNormalIndices(new ArrayList<>(normalIndices));
            }

            tempMesh = mesh;
            windowIsCalled = true;
            fileSelected = true;
            addHBoxModel();
            renderScene();
            tempMesh = null;
            fileSelected = false;
        } catch (IOException e) {
            showErrorAlert("Ошибка", "Не удалось загрузить модель из файла: " + e.getMessage());
        }
    }

    private void processDroppedFileTexture(File file) {
        try {
            Image texture = new Image(file.toURI().toString());
            if (activeModelIndex != -1){
                meshes.get(activeModelIndex).texture = texture;
            }
            renderScene();
        } catch (Exception exception) {
            throw new RuntimeException("Ошибка при загрузке текстуры: " + exception.getMessage(), exception);
        }
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
        cameraManager.getActiveCamera().movePositionAndTarget(new Vector3f(TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraRight(ActionEvent actionEvent) {
        cameraManager.getActiveCamera().movePositionAndTarget(new Vector3f(-TRANSLATION, 0, 0));
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

    @FXML
    private void handleApplyTransformations(ActionEvent event) {
        try {
            float scaleX = Float.parseFloat(this.scaleX.getText());
            float scaleY = Float.parseFloat(this.scaleY.getText());
            float scaleZ = Float.parseFloat(this.scaleY.getText());

            float rotateX = Float.parseFloat(this.rotateX.getText());
            float rotateY = Float.parseFloat(this.rotateY.getText());
            float rotateZ = Float.parseFloat(this.rotateZ.getText());

            float translateX = Float.parseFloat(this.translateX.getText());
            float translateY = Float.parseFloat(this.translateY.getText());
            float translateZ = Float.parseFloat(this.translateZ.getText());

            mesh.setScale(new Vector3f(scaleX, scaleY, scaleZ));
            mesh.setRotation(new Vector3f(rotateX, rotateY, rotateZ));
            mesh.setTranslation(new Vector3f(translateX, translateY, translateZ));

            timeline.playFromStart();
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректные числа для трансформации.");
        }
    }
}
