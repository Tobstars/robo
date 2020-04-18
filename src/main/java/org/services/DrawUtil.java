package org.services;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.models.Pixel;

import java.util.List;

public class DrawUtil {
    private Slider pathSlider;

    public void drawPath(Canvas pathCanvas, StackPane pane, List<Pixel<Integer>> pathPixels, ImageView robot) {
        if (pathSlider == null) {
            getSlider(pathPixels, pane, robot);
        }

        // Draw path
        clearCanvas(pathCanvas);
        pathCanvas.getGraphicsContext2D().setGlobalAlpha(0);
        for (Pixel<Integer> pixel : pathPixels) {
            pathCanvas.getGraphicsContext2D().getPixelWriter().setColor(pixel.getX(), pixel.getY(), Color.GREEN);
        }
    }

    private static void clearCanvas(Canvas pathCanvas) {
        pathCanvas.getGraphicsContext2D().clearRect(0, 0, pathCanvas.getWidth(), pathCanvas.getHeight());
    }

    private Slider getSlider(List<Pixel<Integer>> pathPixels, StackPane pane, ImageView robot) {
        pathSlider = new Slider(0, pathPixels.size() - 1, 0);
        pathSlider.setMaxHeight(800);
        pathSlider.setPadding(new Insets(0, 100, 0, 0));
        pathSlider.setOrientation(Orientation.VERTICAL);

        pathSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            int counter = (int) newValue.doubleValue();
            Pixel<Integer> pathPixel = pathPixels.get(counter);
            Pixel<Double> centerPixel = PixelConverterUtil.getGlobalRobotCenterPixel(pathPixel.getX(), pathPixel.getY());
            robot.setTranslateX(centerPixel.getX());
            robot.setTranslateY(centerPixel.getY());
        });

        pane.setAlignment(pathSlider, Pos.CENTER_RIGHT);
        pane.getChildren().add(pathSlider);
        return pathSlider;
    }

}
