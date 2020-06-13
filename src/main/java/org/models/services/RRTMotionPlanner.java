package org.models.services;

import com.scalified.tree.TreeNode;
import com.scalified.tree.multinode.ArrayMultiTreeNode;
import org.models.*;
import org.models.Vector;

import java.util.*;

public class RRTMotionPlanner {
    TreeNode<Vector> root = new ArrayMultiTreeNode<>(null);
    private int range;
    private int maxTimeInSeconds;
    private Workspace workspace;
    private long end;

    public static List<Vector> planMotion(Vector cInit, Vector cGoal, int range, int maxTimeInSeconds, Workspace workspace) {
        return new RRTMotionPlanner(range, maxTimeInSeconds, workspace).execute(cInit, cGoal);
    }

    private RRTMotionPlanner(int range, int maxTimeInSeconds, Workspace workspace) {
        this.range = range;
        this.maxTimeInSeconds = maxTimeInSeconds;
        this.workspace = workspace;
    }

    private List<Vector> execute(Vector cInit, Vector cGoal) {
        startTimer();
        addCInit(cInit);
        while (!timeIsOver()) {
            Vector randomConf = getRandomConfig();
            TreeNode<Vector> nearestNeighbourNode = getNearestNeighbour(randomConf);
            Vector newConfig = getValidEdge(nearestNeighbourNode.data(), randomConf);

            if (!newConfigIsTrapped(newConfig, nearestNeighbourNode.data())) {
                TreeNode<Vector> newConfigNode = new ArrayMultiTreeNode<>(newConfig);
                nearestNeighbourNode.add(newConfigNode);
                if (goalReached(newConfig, cGoal)) {
                    TreeNode<Vector> cGoalNode = new ArrayMultiTreeNode<>(cGoal);
                    newConfigNode.add(cGoalNode);
                    return getPath(cGoalNode);
                }
            }
        }
        return null;
    }

    private void startTimer() {
        long start = System.currentTimeMillis();
        end = start + maxTimeInSeconds * 1000;
    }

    private List<Vector> getPath(TreeNode<Vector> cGoalNode) {
        List<Vector> reversePath = new ArrayList<>();
        reversePath.add(cGoalNode.data());

        TreeNode<Vector> ancestor = cGoalNode;
        while (!ancestor.isRoot()) {
            reversePath.add(ancestor.parent().data());
            ancestor = ancestor.parent();
        }

        Collections.reverse(reversePath);
        return reversePath;
    }

    private boolean goalReached(Vector newConfig, Vector cGoal) {
        return MotionPlannerUtil.edgeIsValid(newConfig, cGoal, workspace);
    }

    private boolean newConfigIsTrapped(Vector newConfig, Vector nearestNeighbour) {
        return !MotionPlannerUtil.edgeIsValid(newConfig, nearestNeighbour, workspace);
    }

    private Vector getValidEdge(Vector nearestNeighbour, Vector randomConf) {
        if (MotionPlannerUtil.getDistance(randomConf, nearestNeighbour) <= range) {
            return randomConf;
        } else {
            Vector vectorBetween = new Vector(randomConf.getX() - nearestNeighbour.getX(), randomConf.getY() - nearestNeighbour.getY());
            return calculateNewConfig(vectorBetween, nearestNeighbour);
        }

    }

    private Vector calculateNewConfig(Vector vectorBetween, Vector nearestNeighbour) {
        double length = (double) range / vectorBetween.getLength();
        Vector direction = new Vector((int) (length * vectorBetween.getX()), (int) (length * vectorBetween.getY()));
        return new Vector(nearestNeighbour.getX() + direction.getX(), nearestNeighbour.getY() + direction.getY());
    }

    private TreeNode<Vector> getNearestNeighbour(Vector conf) {
        double smallestDistance = Integer.MAX_VALUE;
        TreeNode<Vector> nearestNeighbourNode = null;
        for (TreeNode<Vector> node : root) {
            Vector neighbour = node.data();
            // should not happen :/
            assert !neighbour.equals(conf);
            double distance = MotionPlannerUtil.getDistance(conf, neighbour);
            if (distance < smallestDistance) {
                smallestDistance = distance;
                nearestNeighbourNode = node;
            }
        }
        return nearestNeighbourNode;
    }

    private void addCInit(Vector cInit) {
        root.setData(cInit);
    }

    private Vector getRandomConfig() {
        Random r = new Random();
        int randomX = r.nextInt(ConfigSpace.WIDTH + 1);
        int randomY = r.nextInt(ConfigSpace.HEIGHT + 1);
        Vector random = new Vector();
        random.setX(randomX);
        random.setY(randomY);
        if (!workspace.isInCollision(random)) {
            return random;
        }
        return getRandomConfig();
    }

    //TODO
    private boolean timeIsOver() {
        return System.currentTimeMillis() > end;
    }


}
