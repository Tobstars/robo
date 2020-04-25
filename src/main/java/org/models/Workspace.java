package org.models;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import org.common.PixelConverterUtil;

import java.util.List;

public class Workspace {
    // Bit representation of env and robot
    // 1 = black, 0 = white
    int[][] env;
    int[][] robot;
    private Slider pathSlider;

    public Workspace(Image env, Image robot) {
        this.env = loadBitImageFromImage(env);
        this.robot = loadBitImageFromImage(robot);
    }

    public void moveRobot(Vector config) {

    }

    public void drawRobotOnSolutionPath(List<Vector> solutionPath, StackPane pane, ImageView robot) {
        if (pathSlider != null) {
            return;
        }
        pathSlider = new Slider(0, solutionPath.size() - 1, 0);
        pathSlider.setMaxHeight(800);
        pathSlider.setPadding(new Insets(0, 100, 0, 0));
        pathSlider.setOrientation(Orientation.VERTICAL);

        pathSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            int counter = newValue.intValue();
            Vector config = solutionPath.get(counter);
            robot.setTranslateX(config.getX());
            robot.setTranslateY(config.getY());
        });

        pane.setAlignment(pathSlider, Pos.CENTER_RIGHT);
        pane.getChildren().add(pathSlider);
    }

    public int[][] loadBitImageFromImage(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        int[][] bitImage = new int[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                bitImage[i][j] = image.getPixelReader().getColor(i,j).equals(Color.BLACK) ? 1 : 0;
            }
        }
        return bitImage;
    }

    public boolean isInCollision(ImageView robot, ImageView env, Vector config) {
        for (int x = 0; x < robot.getImage().getWidth(); x++) {
            for (int y = 0; y < robot.getImage().getHeight(); y++) {
                // Only check black pixels of the robot
                if (robot.getImage().getPixelReader().getColor(x,y).equals(Color.WHITE)) {
                    continue;
                }
                Pair<Integer, Integer> coordinate = PixelConverterUtil.getCoordinateOnEnv(robot, x, y, env);

                // Check the calculated coordinate for collision
                Color color = env.getImage().getPixelReader().getColor(coordinate.getKey(), coordinate.getValue());
                // If color is black -> there is collision
                if (color.equals(Color.BLACK)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int[][] getEnv() {
        return env;
    }

    public void setEnv(int[][] env) {
        this.env = env;
    }

    public int[][] getRobot() {
        return robot;
    }

    public void setRobot(int[][] robot) {
        this.robot = robot;
    }
}
