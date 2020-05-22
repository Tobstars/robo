package org.common;

import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import javafx.util.Pair;
import org.models.Pixel;
import org.models.Vector;

public class PixelConverterUtil {

    // Calculate global coordinate of a robot pixel on the environment
    public static Pair<Integer, Integer> getCoordinateOnEnv(ImageView robot, int pixelX, int pixelY) {
        // Calculate coordinates of the robot origin
        Bounds robotBoundInParent = robot.getBoundsInParent();
        int robotGlobalOriginX = (int) robotBoundInParent.getMinX();
        int robotGlobalOriginY = (int) robotBoundInParent.getMinY();

        // Exact global coordinates of the pixel in the scene = origin of the image + pixel offset
        int globalPixelX = robotGlobalOriginX + pixelX;
        int globalPixelY = robotGlobalOriginY + pixelY;

        return new Pair<>(globalPixelX, globalPixelY);
    }

    public static Vector getConfiguration(ImageView robot) {
        Pair<Integer, Integer> coordinate = getCoordinateOnEnv(robot, 0, 0);
        return new Vector(coordinate.getKey(), coordinate.getValue());
    }
}
