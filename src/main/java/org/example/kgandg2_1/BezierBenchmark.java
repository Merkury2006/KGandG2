package org.example.kgandg2_1;

import javafx.geometry.Point2D;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

public class BezierBenchmark {
    private static final int SIZE_ITERATIONS = 50;
    private static final int STEP_ITERATIONS = 5;

    public static void main(String[] args) {
        WritableImage image = new WritableImage(800, 600);
        PixelWriter pixelWriter = image.getPixelWriter();
        for (int i = 0; i <= SIZE_ITERATIONS; i += STEP_ITERATIONS) {
            testWithPoints(i, pixelWriter);
        }
    }

    private static void testWithPoints(int pointCount, PixelWriter pixelWriter) {
        System.out.println("Кривая с " + pointCount + " контрольными точками:");

        List<Point2D> curve = generateCurve(pointCount);

        long oldTime = testOldVersion(curve, pixelWriter);
        long newTime = testNewVersion(curve, pixelWriter);

        System.out.println("Старая версия: " + oldTime);
        System.out.println("Новая версия:  " + newTime);

        double ratio = (double) oldTime / newTime;
        System.out.println("Новая версия в "  + ratio + "раз быстрее!");

        System.out.println("---------------------------");
    }

    private static List<Point2D> generateCurve(int pointCount) {
        List<Point2D> points = new ArrayList<>();
        for (int i = 0; i < pointCount; i++) {
            double x = 100 + i * 30;
            double y = 200 + Math.sin(i * 0.5) * 100 + Math.cos(i * 0.3) * 50;
            points.add(new Point2D(x, y));
        }
        return points;
    }

    private static long testOldVersion(List<Point2D> curve, PixelWriter pixelWriter) {
        BezierCurveOld bezier = new BezierCurveOld();
        for (Point2D point : curve) bezier.addPoint(point);

        long start = System.nanoTime();
        bezier.draw(pixelWriter, 800, 600, Color.BLACK);
        return System.nanoTime() - start;
    }

    private static long testNewVersion(List<Point2D> curve, PixelWriter pixelWriter) {
        BezierCurve bezier = new BezierCurve();
        for (Point2D point : curve) bezier.addPoint(point);

        long start = System.nanoTime();
        bezier.draw(pixelWriter, 800, 600, Color.BLACK);
        return System.nanoTime() - start;
    }
}