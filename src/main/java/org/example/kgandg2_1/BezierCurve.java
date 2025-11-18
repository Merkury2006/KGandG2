package org.example.kgandg2_1;

import javafx.geometry.Point2D;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;


import java.awt.*;
import java.util.*;
import java.util.List;

public class BezierCurve {
    class BezierInterval {
        private final double tFrom, tTo;
        private final Point2D start, end;

        public BezierInterval(double tFrom, double tTo, Point2D start, Point2D end) {
            this.tFrom = tFrom;
            this.tTo = tTo;
            this.start = start;
            this.end = end;
        }

        public double gettFrom() {
            return tFrom;
        }

        public double gettTo() {
            return tTo;
        }

        public Point2D getStart() {
            return start;
        }

        public Point2D getEnd() {
            return end;
        }
    }
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
        double[] coefficients = getBinomialCoefficients(n);

        Stack<BezierInterval> tasks = new Stack<>();
        tasks.push(new BezierInterval(0, 1, getBezierPoint(0, coefficients), getBezierPoint(1, coefficients)));

        while (!tasks.empty()) {
            BezierInterval task = tasks.pop();

            if (task.getStart().distance(task.getEnd()) > 5) {
                double tMid = (task.gettTo() + task.gettFrom()) / 2;

                Point2D midPoint = getBezierPoint(tMid, coefficients);

                tasks.push(new BezierInterval(task.gettFrom(), tMid, task.getStart(), midPoint));
                tasks.push(new BezierInterval(tMid, task.gettTo(), midPoint, task.getEnd()));
            }
            else {
                DrawUtils.drawLine(task.getStart(), task.getEnd(), pixelWriter, widthCanvas, heightCanvas, color);
            }
        }
    }

    private Point2D getBezierPoint(double t, double[] coefficients) {
        int n = points.size() - 1;
        double x = 0;
        double y = 0;

        for (int k = 0; k <= n; k ++) {
            double basicFunction = coefficients[k] * Math.pow(1 - t, n - k) * Math.pow(t, k);
            x += points.get(k).getX() * basicFunction;
            y += points.get(k).getY() * basicFunction;
        }

        return new Point2D(x, y);
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
