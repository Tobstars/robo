package org.services;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import org.models.Pixel;

import java.util.ArrayList;
import java.util.List;

public class CollisionUtil {
    List<Pixel<Integer>> blackPixelsRobot;

    public CollisionUtil(Image robot) {
        this.blackPixelsRobot = getBlackPixels(robot);
    }

    // Check if the robot collides with the environment
    public boolean collidesWithEnv(ImageView robot, ImageView env) {
        // Check for all pixels of the robot if it collides with the environment
        for (Pixel<Integer> pixel : blackPixelsRobot) {
            Pair<Integer, Integer> coordinate =
                    PixelConverterUtil.getCoordinateOnEnv(robot, pixel.getX(), pixel.getY(), env);

            // Check the calculated coordinate for collision
            Color color = env.getImage().getPixelReader().getColor(coordinate.getKey(), coordinate.getValue());
            // If color is black -> there is collision
            if (color.equals(Color.BLACK)) {
                return true;
            }
        }
        return false;
    }

    private List<Pixel<Integer>> getBlackPixels(Image robot) {
        List<Pixel<Integer>> pixels = new ArrayList<>();

        // Iterate through robot
        for (int i = 0; i < (int) robot.getWidth(); i++) {
            for (int j = 0; j < (int) robot.getHeight(); j++) {
                // Only save black pixels
                Color color = robot.getPixelReader().getColor(i, j);
                if (color.equals(Color.BLACK)) {
                    Pixel<Integer> pixel = new Pixel<>(i, j);
                    pixels.add(pixel);
                }
            }
        }
        return pixels;
    }


}
