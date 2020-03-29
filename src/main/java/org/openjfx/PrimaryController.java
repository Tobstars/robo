package org.openjfx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class PrimaryController {

    @FXML
    ImageView robotStart;
    @FXML
    ImageView robotEnd;
    @FXML
    Label info;
    boolean placementFinished;
    boolean dragging;
    boolean selected;
    double sceneX, sceneY;
    double translateX, translateY;

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
        // only show the robot "end" if the placement is not finished
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
        placementFinished = true;
        dragging = false;
        info.setText("Great! Now you can plan the motion");
    }

    @FXML
    private void onMouseExited() {
        if (placementFinished) {
            info.setText("Great! Now you can plan the motion");
        }
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
