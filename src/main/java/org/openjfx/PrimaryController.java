package org.openjfx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import org.common.CollisionUtil;
import org.common.PathUtil;
import org.models.Pixel;

import java.io.File;
import java.util.List;

public class PrimaryController {

    // Robot on the start position
    @FXML
    ImageView robotStart;
    // Robot on the end position
    @FXML
    ImageView robotEnd;
    @FXML
    ImageView env;
    @FXML
    Label info;
    CollisionUtil collisionUtil;
    boolean placementFinished;
    boolean dragging;
    boolean selected;
    double sceneX, sceneY;
    double translateX, translateY;

    @FXML
    public void initialize() {
        // Needed later for collision detection between robots and environment
        collisionUtil = new CollisionUtil(robotStart.getImage());
    }

    @FXML
    // Mouse click before dragging
    private void onMousePressed(MouseEvent e) {
        sceneX = e.getSceneX();
        sceneY = e.getSceneY();
        translateX = ((ImageView) (e.getSource())).getTranslateX();
        translateY = ((ImageView) (e.getSource())).getTranslateY();

        dragging = true;
    }

    @FXML
    private void dragRobot(MouseEvent e) {
        double offsetX = e.getSceneX() - sceneX;
        double offsetY = e.getSceneY() - sceneY;

        ((ImageView) (e.getSource())).setTranslateX(translateX + offsetX);
        ((ImageView) (e.getSource())).setTranslateY(translateY + offsetY);
    }

    @FXML
    private void showRobotEnd() {
        // If the robot collides -> show warning text
        if (collisionUtil.collidesWithEnv(robotStart, env)) {
            info.setText("The robot is colliding with the environment. Please reposition the robot");
            return;
        }

        // Only show the robot "end" if the placement is not finished
        if (!placementFinished) {
            robotEnd.setVisible(true);
            info.setText("Place the robot on the end position");
        }
        dragging = false;
    }

    @FXML
    private void showRobotStartName() {
        showRobotName(RobotEnum.START);
    }

    @FXML
    private void showRobotEndName() {
        showRobotName(RobotEnum.END);
    }

    private void showRobotName(RobotEnum robot) {
        if (placementFinished && !dragging) {
            selected = true;
            info.setText(robot.equals(RobotEnum.START) ?
                    "Currently selected: Start Placement" : "Currently selected: End Placement");
        }
    }

    @FXML
    private void setPlacingFinished() {
        // If the robot collides -> show warning text
        if (collisionUtil.collidesWithEnv(robotEnd, env)) {
            info.setText("The robot is colliding with the environment. Please re-position the robot");
            return;
        }

        placementFinished = true;
        dragging = false;
        info.setText("Great! Now you can plan the motion");
    }

    @FXML
    private void onMouseExited() {
        if (placementFinished && !collisionUtil.collidesWithEnv(robotStart, env)
                && !collisionUtil.collidesWithEnv(robotEnd, env)) {
            info.setText("Great! Now you can plan the motion");
        }
    }

    @FXML
    private void openFileChooser() {
        // Open File Chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load new environment");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Bitmap Files", "*.bmp")
        );
        File file = fileChooser.showOpenDialog(robotStart.getScene().getWindow());

        // Load environment
        Image image = new Image("file:///" + file.getAbsolutePath());
        env.setImage(image);
    }

    @FXML
    private void calculatePath() {
        List<Pixel> pathPixels = PathUtil.calculatePath(env);


    }

    @FXML
    private void reset() {
        info.setText("Place the robot on the start position");
        placementFinished = false;

        robotStart.setTranslateX(0);
        robotStart.setTranslateY(0);

        robotEnd.setVisible(false);
        robotEnd.setTranslateX(0);
        robotEnd.setTranslateY(0);
    }
}
