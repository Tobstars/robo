package org.models.services;

import org.models.Vector;
import org.models.Workspace;

import java.util.ArrayList;
import java.util.List;

public class MotionPlannerUtil {

    public static double getDistance(Vector vertex, Vector neighbor) {
        double y = Math.abs(neighbor.getY() - vertex.getY());
        double x = Math.abs(neighbor.getX() - vertex.getX());
        return Math.hypot(y, x);
    }

    public static boolean edgeIsValid(Vector vertex, Vector neighbor, Workspace workspace) {
        List<Vector> path = getStraightPath(vertex, neighbor);
        for (Vector vector : path) {
            if (workspace.isInCollision(vector)) {
                return false;
            }
        }
        return true;
    }

    // Outdated and should be fixed
    public static boolean binaryTreeEdgeIsValid(Vector vertex, Vector neighbor, Workspace workspace) {
        List<Vector> path = getStraightPath(vertex, neighbor);
        return binarySearchEdgeIsValid(path, 0, path.size() - 1, workspace);
    }

    public static boolean binarySearchEdgeIsValid(List<Vector> path, int left, int right, Workspace workspace) {
        if (right >= left) {
            int mid = left + (right - left) / 2;
            if (workspace.isInCollision(path.get(mid))) {
                return false;
            }
            boolean leftIsValid = binarySearchEdgeIsValid(path, left, mid - 1, workspace);
            boolean rightIsValid = binarySearchEdgeIsValid(path, mid + 1, right, workspace);
            if (leftIsValid && rightIsValid) {
                return true;
            }

        }
        return true;
    }

    public static List<Vector> getStraightPath(Vector vertex, Vector neighbor) {
        int resolution = Math.max(Math.abs(vertex.getX() - neighbor.getX()), Math.abs(vertex.getY() - neighbor.getY()));
        Vector between = new Vector(neighbor.getX() - vertex.getX(), neighbor.getY() - vertex.getY());
        List<Vector> pathPoints = calculatePathPoints(vertex, resolution, between);
        return pathPoints;
    }

    public static List<Vector> calculatePathPoints(Vector vertex, int resolution, Vector between) {
        List<Vector> pathPoints = new ArrayList<>();
        for (int i = 1; i < resolution; i++) {
            Vector pathPoint = calculatePathPoint(vertex, between, i, resolution);
            pathPoints.add(pathPoint);
        }
        return pathPoints;
    }

    public static Vector calculatePathPoint(Vector vertex, Vector between, int i, int resolution) {
        Vector round = new Vector((int) (((float) i / resolution) * between.getX()), (int) (((float) i / resolution) * between.getY()));
        return new Vector(vertex.getX() + round.getX(), vertex.getY() + round.getY());
    }

}
