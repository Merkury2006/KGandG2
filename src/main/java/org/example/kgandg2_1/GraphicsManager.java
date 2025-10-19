package org.example.kgandg2_1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

import java.util.List;

public class GraphicsManager {
    private Canvas displayCanvas;
    private GraphicsContext displayGC;
    private WritableImage buffer;
    private PixelWriter bufferWriter;

    private Point2D selectPoint = null;
    private int selectPointIndex = -1;

    private ObservableList<BezierCurve> curves = FXCollections.observableArrayList();
    private BezierCurve currentCurve = new BezierCurve();

    private boolean showLines = true;
    private boolean showPoints = true;
    private boolean isCreatingNewCurve = false;

    public GraphicsManager() {
        curves.add(currentCurve);
    }

    public void setCanvas(Canvas canvas) {
        this.displayCanvas = canvas;
        this.displayGC = displayCanvas.getGraphicsContext2D();
        initializeBuffer();
    }

    private void initializeBuffer() {
        if (displayCanvas == null) return;

        int width = (int) Math.max(1, displayCanvas.getWidth());
        int height = (int) Math.max(1, displayCanvas.getHeight());
        this.buffer = new WritableImage(width, height);
        this.bufferWriter = buffer.getPixelWriter();
        redrawAll();
    }

    public void onCanvasResize() {
        initializeBuffer();
    }

    public ObservableList<BezierCurve> getCurves() {
        return curves;
    }

    public void selectCurve(int selectCurveIndex) {
        if (selectCurveIndex >= 0 && selectCurveIndex < curves.size()) {
            currentCurve = curves.get(selectCurveIndex);
            redrawAll();
        }
    }

    public void startNewCurve() {
        isCreatingNewCurve = true;
    }

    public void clearAllCurves() {
        curves.clear();
        currentCurve = new BezierCurve();
        curves.add(currentCurve);
        redrawAll();
    }

    public void handleMouseClick(Point2D point) {
        if (isCreatingNewCurve) {
            currentCurve = new BezierCurve();
            curves.add(currentCurve);
            isCreatingNewCurve = false;
        }
        currentCurve.addPoint(point);
        redrawAll();
    }

    public void handleMouseDeleteClick(Point2D point) {
        Point2D deletePoint = findNearestPoint(point);
        currentCurve.getPoints().remove(deletePoint);
        redrawAll();
    }

    public void handleMousePressed(Point2D point) {
        selectPoint = findNearestPoint(point);
        selectPointIndex = findSelectedPointIndex();
    }

    public void handleMouseDragged(Point2D point) {
        if (selectPoint != null) {
            currentCurve.getPoints().set(selectPointIndex, point);
            redrawAll();
        }
    }

    public void handleMouseReleased() {
        selectPoint = null;
        selectPointIndex = -1;
    }

    public void showOrHideLines(boolean showLines) {
        this.showLines = showLines;
        redrawAll();
    }

    public void showOrHidePoints(boolean showPoints) {
        this.showPoints = showPoints;
        redrawAll();
    }

    private Point2D findNearestPoint(Point2D point) {
        double minDistance = Double.MAX_VALUE;
        Point2D nearestPoint = null;
        for (Point2D curPoint : currentCurve.getPoints()) {
            double distance = curPoint.distance(point);
            if (distance < minDistance && distance < 10) {
                minDistance = distance;
                nearestPoint = curPoint;
            }
        }
        return nearestPoint;
    }

    private int findSelectedPointIndex() {
        if (selectPoint != null) {
            List<Point2D> points = currentCurve.getPoints();
            for (int i = 0; i < points.size(); i ++ ) {
                if (selectPoint.equals(points.get(i))) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void redrawAll() {
        if (bufferWriter == null) return;
        bufferClear();

        List<Point2D> points = currentCurve.getPoints();

        if (showPoints) {
            points.forEach(point -> DrawUtils.drawPoint(bufferWriter, (int) buffer.getWidth(), (int) buffer.getHeight(), point));
        }

        if(showLines) {
            for (int i = 0; i < points.size() - 1; i++) {
                DrawUtils.drawLine(points.get(i), points.get(i + 1), bufferWriter, (int) buffer.getWidth(), (int) buffer.getHeight());
            }
        }

        for (BezierCurve bezierCurve : curves) {
            bezierCurve.draw(bufferWriter, (int) buffer.getWidth(), (int) buffer.getHeight(),
                    bezierCurve.equals(currentCurve) ? Config.colorOfBezierCurrentCurve : Config.colorOfBezierCurves);
        }


        displayGC.drawImage(buffer, 0, 0);
    }

    private void bufferClear() {
        for (int x = 0; x < buffer.getWidth(); x ++) {
            for(int y = 0; y < buffer.getHeight(); y ++) {
                bufferWriter.setColor(x, y, Color.WHITE);
            }
        }
    }
}
