package model;

/**
 * Created by Lobster on 21.03.18.
 */
public class Problem {

    private Graph graph;
    private int routes[][];

    public Problem(Graph graph, int[][] routes) {
        this.graph = graph;
        this.routes = routes;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public int[][] getRoutes() {
        return routes;
    }

    public void setRoutes(int[][] routes) {
        this.routes = routes;
    }
}
