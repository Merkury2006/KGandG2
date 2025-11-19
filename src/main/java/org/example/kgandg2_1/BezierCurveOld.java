package org.example.kgandg2_1;

import javafx.geometry.Point2D;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BezierCurveOld {
    private static Map<Integer, Double> factorials = new HashMap<>();
    static {
        factorials.put(0, 1.0);
        factorials.put(1, 1.0);
        factorials.put(2, 2.0);
        factorials.put(3, 6.0);
        factorials.put(4, 24.0);
        factorials.put(5, 120.0);
        factorials.put(6, 720.0);
        factorials.put(7, 5040.0);
        factorials.put(8, 40320.0);
        factorials.put(9, 362880.0);
        factorials.put(10, 3628800.0);
        factorials.put(11, 39916800.0);
        factorials.put(12, 479001600.0);
        factorials.put(13, 6227020800.0);
        factorials.put(14, 87178291200.0);
        factorials.put(15, 1307674368000.0);
        factorials.put(16, 20922789888000.0);
        factorials.put(17, 355687428096000.0);
        factorials.put(18, 6402373705728000.0);
        factorials.put(19, 121645100408832000.0);
        factorials.put(20, 2432902008176640000.0);
    }

    private static double fact(int x) {
        if (factorials.containsKey(x)) {
            return factorials.get(x);
        }

        double proizv = 1;
        for (int i =1; i <= x; i ++) {
            proizv *= i;
        }
        factorials.put(x, proizv);

        return proizv;
    }

    public static double getC(int k, int n) {
        return fact(n) / (fact(k) * fact(n - k));
    }

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
                xCur += points.get(k).getX() * getC(k, n) * Math.pow(t, k) * Math.pow(1-t, n - k);
                yCur += points.get(k).getY() * getC(k, n) * Math.pow(t, k) * Math.pow(1-t, n - k);
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
