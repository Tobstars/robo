package org.common;

import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import org.models.Pixel;

import java.util.ArrayList;
import java.util.List;

public class CollisionUtil {
    List<Pixel> blackPixelsRobot;

    public CollisionUtil(Image robot) {
        this.blackPixelsRobot = getBlackPixels(robot);
    }

    // Check if the robot collides with the environment
    public boolean collidesWithEnv(ImageView robot, ImageView env) {
        // Check for all pixels of the robot if it collides with the environment
        for (int i = 0; i < blackPixelsRobot.size(); i++) {
            Pair<Double, Double> coordinate = calculateCoordinatesOnEnv(robot, env, i);

            // Check the calculated coordinate for collision
            Color color = env.getImage().getPixelReader().
                    getColor((int) coordinate.getKey().doubleValue(), (int) coordinate.getValue().doubleValue());
            // If color is black -> there is collision
            if (color.equals(Color.BLACK)) {
                return true;
            }
        }
        return false;
    }

    // Calculate global coordinates of a pixel on the environment
    private Pair<Double, Double> calculateCoordinatesOnEnv(ImageView robot, ImageView env, int i) {
        // Calculate coordinates of the robot origin
        Bounds robotBoundInParent = robot.getBoundsInParent();
        int robotGlobalOriginX = (int) robotBoundInParent.getMinX();
        int robotGlobalOriginY = (int) robotBoundInParent.getMinY();
        double robotPixelWidth = robot.getFitWidth() / robot.getImage().getWidth();
        double robotPixelHeight = robot.getFitHeight() / robot.getImage().getHeight();

        // Exact global coordinates of the pixel in the scene (not on the env) = origin of the image + pixel offset
        // The local coordinates of the pixel is used to calculate the global coordinates
        double globalPixelX = robotGlobalOriginX + blackPixelsRobot.get(i).getX() * robotPixelWidth;
        double globalPixelY = robotGlobalOriginY + blackPixelsRobot.get(i).getY() * robotPixelHeight;

        // Calculate coordinates of the env origin
        Bounds envBoundInParent = env.getBoundsInParent();
        int envGlobalOriginX = (int) envBoundInParent.getMinX();
        int envGlobalOriginY = (int) envBoundInParent.getMinY();
        double globalXOnEnv = globalPixelX - envGlobalOriginX;
        double globalYOnEnv = globalPixelY - envGlobalOriginY;

        // Calculate local coordinates of env
        double envPixelWidth = env.getFitWidth() / env.getImage().getWidth();
        // Height of env has to be calculated with getBoundsInParent() because height gets calculated during runtime
        double envPixelHeight = env.getBoundsInParent().getHeight() / env.getImage().getHeight();
        double localEnvX = globalXOnEnv / envPixelWidth;
        double localEnvY = globalYOnEnv / envPixelHeight;

        return new Pair<>(localEnvX, localEnvY);
    }

    private List<Pixel> getBlackPixels(Image robot) {
        List<Pixel> pixels = new ArrayList<>();

        // Iterate through robot
        for (int i = 0; i < (int) robot.getWidth(); i++) {
            for (int j = 0; j < (int) robot.getHeight(); j++) {
                // Only save black pixels
                Color color = robot.getPixelReader().getColor(i, j);
                if (color.equals(Color.BLACK)) {
                    Pixel pixel = new Pixel(i, j);
                    pixels.add(pixel);
                }
            }
        }
        return pixels;
    }


}
