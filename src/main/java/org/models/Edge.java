package org.models;

public class Edge {
    private Vector start;
    private Vector end;

    public Edge(Vector start, Vector end) {
        this.start = start;
        this.end = end;
    }

    public double getDistance() {
        double sum = Math.pow(start.getX() - end.getX(), 2) + Math.pow(start.getY() - end.getY(), 2);
        return Math.sqrt(sum);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (start.equals(edge.start)) {
            if (end.equals(edge.end)){
                return true;
            } else {
                return false;
            }
        } else if (start.equals(edge.end)) {
            if (end.equals(edge.start)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = start != null ? start.hashCode() : 0;
        result = 31 * result + (end != null ? end.hashCode() : 0);
        return result;
    }

    public Vector getStart() {
        return start;
    }

    public Vector getEnd() {
        return end;
    }
}
