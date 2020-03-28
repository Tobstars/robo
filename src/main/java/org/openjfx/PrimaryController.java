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
    String infoCache = "";
    boolean placementFinished;
    boolean dragging;

    @FXML
    private void dragRobotStart(MouseEvent e) {
        dragging = true;
        robotStart.setX(e.getX());
        robotStart.setY(e.getY());
    }

    @FXML
    private void dragRobotEnd(MouseEvent e) {
        dragging = true;
        robotEnd.setX(e.getX());
        robotEnd.setY(e.getY());
    }

    @FXML
    private void showRobotEnd() {
        // only show the robot end if the placement is not finished
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
            infoCache = info.getText();
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
    private void clearInfo() {
        if (placementFinished) {
            if (infoCache.isEmpty()) {
                info.setText("");
            } else {    // Else show the text that was displayed before
                info.setText(infoCache);
            }
        }
    }

    @FXML
    private void reset() {
        info.setText("Place the robot on the start position");
        placementFinished = false;

        robotStart.setX(0);
        robotStart.setY(0);

        robotEnd.setVisible(false);
        robotEnd.setX(0);
        robotEnd.setY(0);
    }
}
