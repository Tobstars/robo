package org.common;

import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import javafx.util.Pair;
import org.models.Pixel;

public class PixelConverterUtil {
    // Robot is 48*48 pixels
    public static final double robotCenterX = 23.5;
    public static final double robotCenterY = 23.5;

    // Calculate global coordinate of a robot pixel on the environment
    public static Pair<Integer, Integer> getCoordinateOnEnv(ImageView robot, int pixelX, int pixelY, ImageView env) {
        // Calculate coordinates of the robot origin
        Bounds robotBoundInParent = robot.getBoundsInParent();
        int robotGlobalOriginX = (int) robotBoundInParent.getMinX();
        int robotGlobalOriginY = (int) robotBoundInParent.getMinY();

        // Exact global coordinates of the pixel in the scene = origin of the image + pixel offset
        int globalPixelX = robotGlobalOriginX + pixelX;
        int globalPixelY = robotGlobalOriginY + pixelY;

        return new Pair<>(globalPixelX, globalPixelY);
    }
}
