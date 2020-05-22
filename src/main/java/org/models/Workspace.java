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

    public static final int WIDTH = 1350;
    public static final int HEIGHT = 980;

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
                bitImage[i][j] = image.getPixelReader().getColor(i, j).equals(Color.WHITE) ? 0 : 1;
            }
        }
        return bitImage;
    }

    public boolean isInCollision(Vector config) {
        for (int x = 0; x < robot.length; x++) {
            for (int y = 0; y < robot[0].length; y++) {
                // Only check black pixels of the robot
                if (robot[x][y] == 0) {
                    continue;
                }
                Pair<Integer, Integer> coordinateOnEnvironment = new Pair<>(config.x + x, config.y + y);

                // If color is black -> there is collision
                if (env[coordinateOnEnvironment.getKey()][coordinateOnEnvironment.getValue()] == 1) {
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
