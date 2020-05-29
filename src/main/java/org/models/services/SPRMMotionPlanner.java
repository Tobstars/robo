package org.models.services;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.models.ConfigSpace;
import org.models.Edge;
import org.models.Vector;
import org.models.Workspace;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class SPRMMotionPlanner {
    Workspace workspace;
    List<Vector> vertex = new ArrayList<>();
    List<Edge> edges = new LinkedList<>();

    private SPRMMotionPlanner(Workspace workspace) {
        this.workspace = workspace;
    }

    public static List<Vector> planMotion(Vector cInit, Vector cGoal, double radius, int samples, Workspace workspace) {
        return new SPRMMotionPlanner(workspace).executeSPRM(cInit, cGoal, radius, samples);
    }

    private List<Vector> executeSPRM(Vector cInit, Vector cGoal, double radius, int samples) {
        addConfigurations(cInit, cGoal);
        createSamples(samples);
        buildRoadMap(radius);
        return findShortestPath(cInit, cGoal);
    }

    private List<Vector> findShortestPath(Vector cInit, Vector cGoal) {
        Graph<Vector, DefaultWeightedEdge> graph = createGraph();
        GraphPath<Vector, DefaultWeightedEdge> path = DijkstraShortestPath.findPathBetween(graph, cInit, cGoal);
        if (path == null) {
            System.out.println("ERROR: No solution path was found");
            return null;
        } else {
            System.out.println("SUCCESS: A solution path was found");
            return path.getVertexList();
        }
    }

    private Graph<Vector, DefaultWeightedEdge> createGraph() {
        Graph<Vector, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        for (Vector vertex : this.vertex) {
            graph.addVertex(vertex);
        }
        for (Edge edge : this.edges) {
            DefaultWeightedEdge weightedEdge = graph.addEdge(edge.getStart(), edge.getEnd());
            graph.setEdgeWeight(weightedEdge, edge.getDistance());
        }
        return graph;
    }

    private void addConfigurations(Vector cInit, Vector cGoal) {
        vertex.add(cInit);
        vertex.add(cGoal);
    }

    private void buildRoadMap(double radius) {
        for (Vector vertex : this.vertex) {
            List<Vector> neighbors = getNeighbors(vertex, radius);
            getValidEdges(vertex, neighbors);
        }
    }

    private void getValidEdges(Vector vertex, List<Vector> neighbors) {
        for (Vector neighbor : neighbors) {
            if (edgeIsValid(vertex, neighbor)) {
                Edge edge = new Edge(vertex, neighbor);
                if (!edges.contains(edge)) {
                    edges.add(edge);
                }
            }
        }
    }

    private boolean edgeIsValid(Vector vertex, Vector neighbor) {
        List<Vector> path = getStraightPath(vertex, neighbor);
        return binarySearchEdgeIsValid(path, 0, path.size() - 1);
    }

    private boolean binarySearchEdgeIsValid(List<Vector> path, int left, int right) {
        if (right >= left) {
            int mid = left + (right - left) / 2;
            if (workspace.isInCollision(path.get(mid))) {
                return false;
            }
            boolean leftIsValid = binarySearchEdgeIsValid(path, left, mid - 1);
            boolean rightIsValid = binarySearchEdgeIsValid(path, mid + 1, right);
            if (leftIsValid && rightIsValid) {
                return true;
            }

        }
        return true;
    }

    private List<Vector> getStraightPath(Vector vertex, Vector neighbor) {
        int resolution = Math.max(Math.abs(vertex.getX() - neighbor.getX()), Math.abs(vertex.getY() - neighbor.getY()));
        Vector between = new Vector(neighbor.getX() - vertex.getX(), neighbor.getY() - vertex.getY());
        List<Vector> pathPoints = calculatePathPoints(vertex, resolution, between);
        return pathPoints;
    }

    private List<Vector> calculatePathPoints(Vector vertex, int resolution, Vector between) {
        List<Vector> pathPoints = new ArrayList<>();
        for (int i = 1; i < resolution; i++) {
            Vector pathPoint = calculatePathPoint(vertex, between, i, resolution);
            pathPoints.add(pathPoint);
        }
        return pathPoints;
    }

    private Vector calculatePathPoint(Vector vertex, Vector between, int i, int resolution) {
        Vector round = new Vector((int) (((float) i / resolution) * between.getX()), (int) (((float) i / resolution) * between.getY()));
        return new Vector(vertex.getX() + round.getX(), vertex.getY() + round.getY());
    }

    private List<Vector> getNeighbors(Vector vertex, double radius) {
        List<Vector> neighbors = new ArrayList<>();
        for (Vector neighbor : this.vertex) {
            if (neighbor.equals(vertex)) {
                continue;
            }
            double distance = getDistance(vertex, neighbor);
            if (distance < radius) {
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }

    private double getDistance(Vector vertex, Vector neighbor) {
        double y = Math.abs(neighbor.getY() - vertex.getY());
        double x = Math.abs(neighbor.getX() - vertex.getX());
        return Math.hypot(y, x);
    }

    private void createSamples(int samples) {
        for (int i = 0; i < samples; i++) {
            Random r = new Random();
            int randomX = r.nextInt(ConfigSpace.WIDTH + 1);
            int randomY = r.nextInt(ConfigSpace.HEIGHT + 1);
            Vector random = new Vector();
            random.setX(randomX);
            random.setY(randomY);
            if (!vertex.contains(random)) {
                vertex.add(random);
            }
        }
    }
}
