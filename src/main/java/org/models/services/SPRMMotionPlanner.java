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
        return new SPRMMotionPlanner(workspace).execute(cInit, cGoal, radius, samples);
    }

    private List<Vector> execute(Vector cInit, Vector cGoal, double radius, int samples) {
        addConfigurations(cInit, cGoal);
        // CONFIGURE SAMPLING METHOD!!!
        createSamples(samples, false);
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
            if (MotionPlannerUtil.edgeIsValid(vertex, neighbor, workspace)) {
                Edge edge = new Edge(vertex, neighbor);
                if (!edges.contains(edge)) {
                    edges.add(edge);
                }
            }
        }
    }

    private List<Vector> getNeighbors(Vector vertex, double radius) {
        List<Vector> neighbors = new ArrayList<>();
        for (Vector neighbor : this.vertex) {
            if (neighbor.equals(vertex)) {
                continue;
            }
            double distance = MotionPlannerUtil.getDistance(vertex, neighbor);
            if (distance < radius) {
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }

    private void createSamples(int samples, boolean bridgeDist) {
        if (bridgeDist) {
            //TODO: check if mean can be deleted
            //Generate 10% of samples with bridge and the others with normal distribution
            int bridgeSamples = (int) (0.1 * samples);
            createSamplesWithBridgeDist(bridgeSamples, 0, 1);
            createSamplesWithNormalDist(samples - bridgeSamples);
        } else {
            createSamplesWithNormalDist(samples);
        }
    }

    private void createSamplesWithBridgeDist(int samples, int mean, int derivation) {
        int i = 0;
        while (i < samples) {
            Random r = new Random();
            Vector bridgeEdge1 = getRandomConfig(r);
            if (workspace.isInCollision(bridgeEdge1)) {
                Vector bridgeEdge2 = calculateSecondBridgeEdge(bridgeEdge1, r, derivation);
                if (workspace.isInCollision(bridgeEdge2)) {
                    Vector midPoint = calculateMidPoint(bridgeEdge1, bridgeEdge2);
                    if (!workspace.isInCollision(midPoint)) {
                        vertex.add(midPoint);
                        i++;
                    }
                }
            }
        }
    }

    private Vector calculateMidPoint(Vector bridgeEdge1, Vector bridgeEdge2) {
        int midPointX = (bridgeEdge1.getX() + bridgeEdge2.getX()) / 2;
        int midPointY = (bridgeEdge1.getY() + bridgeEdge2.getY()) / 2;
        return new Vector(midPointX, midPointY);
    }

    private Vector calculateSecondBridgeEdge(Vector bridgeEdge1, Random r, int derivation) {
        int bridgeEdgeX = calculateValidBridgeValue(bridgeEdge1.getX(), derivation, r, ConfigSpace.WIDTH);
        int bridgeEdgeY = calculateValidBridgeValue(bridgeEdge1.getY(), derivation, r, ConfigSpace.HEIGHT);
        return new Vector(bridgeEdgeX, bridgeEdgeY);
    }

    private int calculateValidBridgeValue(int value, int derivation, Random r, int max) {
        while (true) {
            int random = (int) (r.nextGaussian() * derivation + value);
            if (random >= 0 && random <= max) {
                return random;
            }
        }
    }

    private void createSamplesWithNormalDist(int samples) {
        int i = 0;
        while (i < samples) {
            Random r = new Random();
            Vector random = getRandomConfig(r);
            if (!vertex.contains(random) && !workspace.isInCollision(random)) {
                vertex.add(random);
                i++;
            }
        }
    }

    private Vector getRandomConfig(Random r) {
        int randomX = r.nextInt(ConfigSpace.WIDTH + 1);
        int randomY = r.nextInt(ConfigSpace.HEIGHT + 1);
        Vector random = new Vector();
        random.setX(randomX);
        random.setY(randomY);
        return random;
    }
}
