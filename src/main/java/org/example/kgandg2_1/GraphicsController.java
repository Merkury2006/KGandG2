package org.example.kgandg2_1;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;


public class GraphicsController {
    @FXML
    private VBox vBox;
    @FXML
    private Button buttonLines;
    @FXML
    private Button buttonPoints;
    @FXML
    private Canvas canvas;
    @FXML
    private Pane canvasContainer;
    @FXML
    private HBox toolBar;
    @FXML
    private ComboBox<Integer> curvesComboBox;

    private final GraphicsManager graphicsManager;
    private boolean showLines = true;
    private boolean showPoints = true;


    public GraphicsController(GraphicsManager graphicsManager) {
        this.graphicsManager = graphicsManager;
    }

    @FXML
    private void initialize() {
        canvas.widthProperty().bind(canvasContainer.widthProperty());
        canvas.heightProperty().bind(canvasContainer.heightProperty());

        canvasContainer.widthProperty().addListener((ov, oldValue, newValue) -> graphicsManager.onCanvasResize());
        canvasContainer.heightProperty().addListener((ov, oldValue, newValue) -> graphicsManager.onCanvasResize());

        graphicsManager.setCanvas(canvas);
        graphicsManager.onCanvasResize();

        canvas.setOnMouseClicked(event -> {
            switch (event.getButton()) {
                case PRIMARY -> graphicsManager.handleMouseClick(new Point2D(event.getX(), event.getY()));
                case MIDDLE -> graphicsManager.handleMouseDeleteClick(new Point2D(event.getX(), event.getY()));
            }
        });

        canvas.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                graphicsManager.handleMousePressed(new Point2D(event.getX(), event.getY()));
            }
        });

        canvas.setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                graphicsManager.handleMouseDragged(new Point2D(event.getX(), event.getY()));
            }
        });

        canvas.setOnMouseReleased(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                graphicsManager.handleMouseReleased();
            }
        });

        graphicsManager.getCurves().addListener((ListChangeListener<BezierCurve>) change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    graphicsManager.redrawAll();
                    updateCurvesComboBox();
                }
            }
        });
        curvesComboBox.setOnAction(event -> {
            int selectedIndex = curvesComboBox.getSelectionModel().getSelectedIndex();
            graphicsManager.selectCurve(selectedIndex);
        });

        updateCurvesComboBox();
    }

    @FXML
    private void showOrHideLines() {
        showLines = !showLines;
        buttonLines.setText(showLines ? "Скрыть линии" : "Показать линии");
        graphicsManager.showOrHideLines(showLines);
    }

    @FXML
    private void showOrHidePoints() {
        showPoints = !showPoints;
        buttonPoints.setText(showPoints ? "Скрыть точки" : "Показать точки");
        graphicsManager.showOrHidePoints(showPoints);
    }

    @FXML
    private void handleNewCurve() {
        graphicsManager.startNewCurve();
        updateCurvesComboBox();
    }

    @FXML
    private void handleClearAll() {
        graphicsManager.clearAllCurves();
        updateCurvesComboBox();
    }

    private void updateCurvesComboBox() {
        curvesComboBox.getItems().clear();
        for (int i = 1; i <= graphicsManager.getCurves().size(); i++) {
            curvesComboBox.getItems().add(i);
        }
    }
}
