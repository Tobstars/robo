package org.models;

import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.models.services.RRTMotionPlanner;
import org.models.services.SPRMMotionPlanner;

import java.util.ArrayList;
import java.util.List;

public class ConfigSpace {
    // Bit representation of the configuration space
    int[][] configSpace;
    Vector initialConfig = new Vector(0, 0);
    Vector goalConfig = new Vector(0, 0);
    List<Vector> solutionPath = new ArrayList<>();
    final int configRadius = 5;
    public static int WIDTH = 3;
    public static int HEIGHT = 3;

    public void setAndDrawInitialConfig(Vector config, Canvas canvas) {
        this.initialConfig = config;
        canvas.getGraphicsContext2D().setFill(Color.GREEN);
        canvas.getGraphicsContext2D().fillOval(this.initialConfig.x - configRadius,
                this.initialConfig.y - configRadius, configRadius * 2, configRadius * 2);
    }

    public void setAndDrawGoalConfig(Vector config, Canvas canvas) {
        this.goalConfig = config;
        canvas.getGraphicsContext2D().setFill(Color.RED);
        canvas.getGraphicsContext2D().fillOval(this.goalConfig.x - configRadius,
                this.goalConfig.y - configRadius, configRadius * 2, configRadius * 2);
    }

    public Canvas createConfigSpaceCanvas(StackPane pane) {
        Canvas canvas = new Canvas();
        int width = configSpace.length;
        canvas.setWidth(width);
        int height = configSpace[0].length;
        canvas.setHeight(height);
        canvas.getGraphicsContext2D().setFill(Color.WHITE);
        canvas.getGraphicsContext2D().fillRect(0, 0, width, height);

        pane.setAlignment(canvas, Pos.TOP_LEFT);
        pane.getChildren().add(canvas);
        return canvas;
    }

    public void createConfigSpace(Workspace workspace) {
        int width = workspace.getEnv().length - workspace.robot.length;
        int height = workspace.getEnv()[0].length - workspace.robot[0].length;
        WIDTH = width;
        HEIGHT = height;
        this.configSpace = new int[width][height];
    }

    public void drawConfigSpace(Canvas canvas) {

    }

    public void calculateSolutionPath(Vector initialConfig, Vector goalConfig, Workspace workspace, int samples, int radius, int iterations, String algo) {
        System.out.println(algo);
        for (int i = 0; i < iterations; i++) {
            List<Vector> path;
            if (algo.equals("SPRM Motion Planner")) {
                path = SPRMMotionPlanner.planMotion(initialConfig, goalConfig, radius, samples, workspace);
            } else {
                path = RRTMotionPlanner.planMotion(initialConfig, goalConfig, radius, 80, workspace);
            }
            if (path != null) {
                solutionPath = path;
                System.out.println("Solution Path: ");
                path.forEach(v -> System.out.println("[" + v.getX() + "," + v.getY() + "]"));
                break;
            }
        }
    }

    public void drawSolutionPath(Canvas csCanvas) {
        for (Vector vector : solutionPath) {
            csCanvas.getGraphicsContext2D().getPixelWriter().setColor(vector.getX(), vector.getY(), Color.BLUE);
        }
    }

    public int[][] getConfigSpace() {
        return configSpace;
    }

    public void setConfigSpace(int[][] configSpace) {
        this.configSpace = configSpace;
    }

    public Vector getInitialConfig() {
        return initialConfig;
    }

    public void setInitialConfig(Vector initialConfig) {
        this.initialConfig = initialConfig;
    }

    public Vector getGoalConfig() {
        return goalConfig;
    }

    public void setGoalConfig(Vector goalConfig) {
        this.goalConfig = goalConfig;
    }

    public List<Vector> getSolutionPath() {
        return solutionPath;
    }

    public void setSolutionPath(List<Vector> solutionPath) {
        this.solutionPath = solutionPath;
    }
}
