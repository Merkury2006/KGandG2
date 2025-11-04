package org.example.kgandg2_1;

import javafx.geometry.Point2D;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BezierCurve {
    private static final int capacity = 100;
    private List<Point2D> points = new ArrayList<>();
    private double[][] binomialCache = new double[capacity][];

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
        double[] coefficients = getBinomialCoefficients(n);

        double xPrev = points.getFirst().getX();
        double yPrev = points.getFirst().getY();

        for (double t = 0; t <= 1.0; t += step) {
            double xCur = 0;
            double yCur = 0;

            double powT = 1.0;
            double pow1_T = Math.pow(1.0 - t, n);

            for (int k = 0; k <= n; k ++) {
                double basicFunction = coefficients[k] * powT * pow1_T;
                xCur += points.get(k).getX() * basicFunction;
                yCur += points.get(k).getY() * basicFunction;

                if (t < 1.0) {
                    pow1_T /= (1.0 - t);
                }
                powT *= t;
            }

            DrawUtils.drawLine((int) Math.round(xPrev),(int) Math.round(yPrev), (int) Math.round(xCur), (int) Math.round(yCur), pixelWriter, widthCanvas, heightCanvas, color);

            xPrev = xCur;
            yPrev = yCur;
        }
    }

    private double calculateAdaptiveStep() {
        if (points.size() < 2) {
            return 0.01;
        }
        double maxDistance = -1;
        for (int i = 0; i < points.size() - 1; i ++) {
            maxDistance = Math.max(maxDistance, points.get(i).distance(points.get(i + 1)));
        }
        if (maxDistance > 150) return 0.005;
        if (maxDistance > 80) return 0.01;
        if (maxDistance > 30) return 0.02;
        return 0.03;
    }

    private double[] getBinomialCoefficients(int n) {
        double[] coefficients = new double[n + 1];

        if (n < binomialCache.length && binomialCache[n] == null) {
            binomialCache[n] = new double[n + 1];
        }

        for (int k = 0; k <= n; k ++) {
            if (n < binomialCache.length && binomialCache[n][k] != 0.0) {
                coefficients[k] = binomialCache[n][k];
                continue;
            }

            double curRes = 1;
            for (int i = 1; i <= k; i++) {
                curRes *= (double) (n - k + i) / i;
            }


            if (n < binomialCache.length) binomialCache[n][k] = curRes;
            coefficients[k] = curRes;
        }
        return coefficients;
    }
}
