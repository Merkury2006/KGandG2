package org.example.kgandg2_1;

import javafx.geometry.Point2D;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;


import java.util.ArrayList;
import java.util.List;

public class BezierCurve {
    private List<Point2D> points = new ArrayList<>();

    public List<Point2D> getPoints() {
        return points;
    }

    public void addPoint(Point2D point) {
        points.add(point);
    }

    public void draw(PixelWriter pixelWriter, int widthCanvas, int heightCanvas, Color color) {
        if (points.size() < 2) return;
        int n = points.size() - 1;
        double step = calculateAdaptiveStep();
        double xPrev = points.getFirst().getX();
        double yPrev = points.getFirst().getY();
        for (double t = 0; t <= 1.0; t += step) {
            double xCur = 0;
            double yCur = 0;
            for (int k = 0; k <= n; k ++) {
                xCur += points.get(k).getX() * DrawUtils.getC(k, n) * Math.pow(t, k) * Math.pow(1-t, n - k);
                yCur += points.get(k).getY() * DrawUtils.getC(k, n) * Math.pow(t, k) * Math.pow(1-t, n - k);
            }
            DrawUtils.drawLine((int) Math.round(xPrev),(int) Math.round(yPrev), (int) Math.round(xCur), (int) Math.round(yCur), pixelWriter, widthCanvas, heightCanvas, color);
            xPrev = xCur; yPrev = yCur;
        }
    }


    private double calculateAdaptiveStep() {
        if (points.size() <= 3) return 0.02;
        if (points.size() <= 5) return 0.01;
        if (points.size() <= 10) return 0.005;
        return 0.002;
    }

}
