package org.example.kgandg2_1;

import javafx.geometry.Point2D;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;



public class DrawUtils {
    public static void drawLine(Point2D firstPoint, Point2D lastPoint, PixelWriter pixelWriter, int widthCanvas, int heightCanvas, Color color) {
        drawLine((int) firstPoint.getX(), (int) firstPoint.getY(), (int) lastPoint.getX(), (int) lastPoint.getY(), pixelWriter, widthCanvas, heightCanvas, color);
    }

    public static void drawLine(Point2D firstPoint, Point2D lastPoint, PixelWriter pixelWriter, int widthCanvas, int heightCanvas) {
        drawLine((int) firstPoint.getX(), (int) firstPoint.getY(), (int) lastPoint.getX(), (int) lastPoint.getY(), pixelWriter, widthCanvas, heightCanvas, Config.colorOfLine);
    }
    public static void drawLine(int x1, int y1, int x2, int y2, PixelWriter pixelWriter, int widthCanvas, int heightCanvas) {
        drawLine(x1, y1, x2, y2, pixelWriter, widthCanvas, heightCanvas, Config.colorOfLine);
    }

    public static void drawLine(int x1, int y1, int x2, int y2, PixelWriter pixelWriter, int widthCanvas, int heightCanvas, Color color) {

        int deltaX = Math.abs(x1 - x2);
        int deltaY = Math.abs(y1 - y2);

        if (deltaY <= deltaX) { //Делаем основной переменной x (более плоская прямая)

           if (x2 < x1) { //Свапаем точки чтобы всегда идти слево направо
               int temp = x1; x1 = x2; x2 = temp;
               temp = y1; y1 = y2; y2 = temp;
           }

            int dirY = y2 - y1; //Смотрим направление по y
            if (dirY > 0) dirY = 1;
            if (dirY < 0) dirY = -1;

            int error = 0;
            int deltaErr = deltaY + 1;

            for (int x = x1, y = y1; x <= x2; x ++) {
                safeSetPixel(pixelWriter, widthCanvas, heightCanvas, x, y, color);
                error += deltaErr;
                if (error >= (deltaX + 1)) {
                    y += dirY;
                    error -= (deltaX + 1);
                }
            }
        }
        else { //Делаем основной переменной y (более крутая прямая)

            if (y2 < y1) { //Свапаем точки чтобы всегда идти сверху вниз
                int temp = x1; x1 = x2; x2 = temp;
                temp = y1; y1 = y2; y2 = temp;
            }

            int dirX = x2 - x1; //Смотрим направление по х
            if (dirX > 0) dirX = 1;
            if (dirX < 0) dirX = -1;

            int error = 0;
            int deltaErr = deltaX + 1;

            for (int x = x1, y = y1; y <= y2; y ++) {
                safeSetPixel(pixelWriter, widthCanvas, heightCanvas, x, y, color);
                error += deltaErr;
                if (error >= (deltaY + 1)) {
                    x += dirX;
                    error -= (deltaY + 1);
                }


            }
        }
    }

    public static void drawPoint(PixelWriter pixelWriter, int widthCanvas, int heightCanvas, Point2D point) {
        int x = (int) point.getX(), y = (int) point.getY();
        for (int dx = -Config.radiusOfPoint / 2; dx <= Config.radiusOfPoint / 2; dx ++) {
            for (int dy = -Config.radiusOfPoint / 2; dy <= Config.radiusOfPoint / 2; dy ++) {
                if (dx * dx + dy * dy <= Config.radiusOfPoint * Config.radiusOfPoint) {
                    safeSetPixel(pixelWriter, widthCanvas, heightCanvas, x + dx, y + dy, Config.colorOfPoint);
                }
            }
        }
    }

    private static void safeSetPixel(PixelWriter pixelWriter, int width, int height, int x, int y, Color color) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            pixelWriter.setColor(x, y, color);
        }
    }

}
