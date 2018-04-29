package utils;

import javafx.util.Pair;
import model.*;
import view.SolverCallback;

import java.util.*;

/**
 * Created by Lobster on 26/04/18.
 */
public class GraphSolver {

    private SolverCallback callback;
    private static Random r = new Random();

    public GraphSolver(SolverCallback callback) {
        this.callback = callback;
    }

    public ShortestPaths findShortestPaths(Problem problem, List<ShortestPaths> allPaths, List<ShortestPaths> previousPaths, int k) {
        //System.out.println("Iteration: " + k);
        callback.onIteration(k);
        if (allPaths.isEmpty() && previousPaths == null) { //если функцию запустили в первый раз, то нужно найти хотя бы 1 кратчайший путь для каждого
            ShortestPaths shortestPaths = new ShortestPaths();
            for (int route[] : problem.getRoutes()) {
                int startVertex = route[0];
                int endVertex = route[1];
                Vertex shortestPath = findShortestPath(problem.getGraph(), startVertex, endVertex);
                //System.out.println(shortestPath.toString());
                shortestPaths.addPath(shortestPath);
            }

            //shortestPaths.showCommonEdges();
            allPaths.add(shortestPaths);
            return findShortestPaths(problem, allPaths, new ArrayList<ShortestPaths>(), k + 1);
        }

        if (previousPaths != null && compare(allPaths, previousPaths)) {
            return null;
        } else {
            previousPaths = new ArrayList<ShortestPaths>(allPaths);
        }

        Set<Vertex> pathsSet = new HashSet<Vertex>();
        for (ShortestPaths shortestPaths : allPaths) {
            pathsSet.addAll(shortestPaths.getPaths());
            ShortestPaths newShortestPaths = shortestPaths.copy();
            List<Pair<Vertex, Set<Edge>>> paths = new ArrayList(newShortestPaths.getShortestPaths());
            for (int i = 0; i < paths.size(); i++) {
                Pair<Vertex, Set<Edge>> path = paths.get(i);
                if (path.getValue().isEmpty()) {
                    continue;
                }

                List<Edge> restrictions = new ArrayList<Edge>(path.getValue());
                Vertex newPath = findShortestPathWithRestrictions(problem.getGraph(), restrictions, path.getKey().getHeadIndex(), path.getKey().getIndex());

                if (newPath == null) {
                    continue;
                } else {
                    //System.out.println("path: " + path.getKey().toString());
                    //System.out.println("new path: " + newPath.toString());
                    newShortestPaths.replace(path.getKey(), newPath);
                    pathsSet.add(newPath);
                }

                Graph updatedGraph = removeEdgesFromGraph(problem.getGraph().copy(), restrictions);
                for (int j = 0; j < k; j++) {
                    Vertex currentPath = findShortestPath(updatedGraph, newPath);
                    if (currentPath == null) {
                        //System.out.println("break");
                        break;
                    } else {
                        //System.out.println("not break: " + currentPath.toString());
                        pathsSet.add(currentPath);
                    }
                }
            }
        }

        //System.out.println("pathsSet size: " + pathsSet.size());
        //for (Vertex vertex : pathsSet) {
            //System.out.println(vertex.toString());
        //}

        List<List<Vertex>> all = findAll(new ArrayList<List<Vertex>>(), new ArrayList<Vertex>(), new ArrayList<Vertex>(pathsSet), problem.getRoutes(), 0, problem.getRoutes().length);

        //System.out.println("all size: " + all.size());
        /*for (List<Vertex> vertices : all) {
            //System.out.println("path set: " + vertices.size());
            for (Vertex vertex : vertices) {
                //System.out.println(vertex.toString());
            }
        }*/

        allPaths.clear();
        List<ShortestPaths> bestPaths = new ArrayList<ShortestPaths>();
        for (List<Vertex> paths : all) {
            ShortestPaths shortestPath = new ShortestPaths();
            for (Vertex path : paths) {
                shortestPath.addPath(path);
            }

            allPaths.add(shortestPath);
            if (shortestPath.commonEdgesNotExist()) {
                bestPaths.add(shortestPath);
            }
        }

        //System.out.println(allPaths.size());
        if (!bestPaths.isEmpty()) {
            return findBestPath(problem.getGraph(), bestPaths);
        } else {
            return findShortestPaths(problem, allPaths, previousPaths, k + 1);
        }
    }

    public ShortestPaths findBestPath(Graph graph, List<ShortestPaths> shortestPaths) {
        ShortestPaths bestPath = shortestPaths.get(0);
        int bestF = findFunctionValueForPaths(graph, shortestPaths.get(0));
        for (int i = 1; i < shortestPaths.size(); i++) {
            int f = findFunctionValueForPaths(graph, shortestPaths.get(i));
            if (f < bestF) {
                bestF = f;
                bestPath = shortestPaths.get(i);
            }
        }

        return bestPath;
    }

    public Vertex findShortestPathWithRestrictions(Graph g, List<Edge> edges, int startVertex, int endVertex) {
        //System.out.println(edges.toString());
        if (edges.isEmpty()) {
            return null;
        }

        Graph graph = removeEdgesFromGraph(g.copy(), edges);
        Vertex shortestPath = findShortestPath(graph, startVertex, endVertex);
        //System.out.println("path after remove: " + shortestPath.toString());
        if (shortestPath.length() > 1) {
            return shortestPath;
        } else {
            edges.remove(r.nextInt(edges.size()));
            return findShortestPathWithRestrictions(g, edges, startVertex, endVertex);
        }
    }

    public Vertex findShortestPath(Graph g, Vertex path) {
        Vertex shortestPath = null;
        Edge removedEdge = null;
        int minF = 0;
        for (Edge edge : path.getEdges()) {
            int w = g.removeEdge(edge);
            Vertex currentPath = findShortestPath(g, path.getHeadIndex(), path.getIndex());
            if (currentPath.length() > 1) {
                int currentF = findFunctionValue(g, currentPath);
                if (shortestPath == null || currentF < minF) {
                    shortestPath = currentPath;
                    removedEdge = edge;
                    minF = currentF;
                }
            }

            g.setEdge(edge, w);
        }

        if (removedEdge != null) {
            g.removeEdge(removedEdge);
        }

        return shortestPath;
    }

    public Graph removeEdgesFromGraph(Graph graph, List<Edge> edges) {
        //System.out.println(GraphConverter.toString(graph.getWeights()));
        for (Edge edge : edges) {
            //System.out.println("remove: " + edge.toString());
            graph.removeEdge(edge.getStart(), edge.getEnd());
        }
        //System.out.println(GraphConverter.toString(graph.getWeights()));
        return graph;
    }

    public Boolean intersectEdgesExists(List<Vertex> vertices) {
        Set<Edge> edges = new HashSet<Edge>();
        int edgesCount = 0;
        for (Vertex vertex : vertices) {
            Set<Edge> vertexEdges = vertex.getEdges();
            edgesCount += vertexEdges.size();
            edges.addAll(vertexEdges);
        }

        return edges.size() != edgesCount;
    }

    public Integer findFunctionValueForPaths(Graph graph, ShortestPaths shortestPaths) {
        Integer f = 0;
        for (Vertex path : shortestPaths.getPaths()) {
            f += findFunctionValue(graph, path);
        }

        return f;
    }

    public Integer findFunctionValue(Graph graph, Vertex path) {
        Integer f = 0;
        for (Edge edge : path.getOneDirectedEdges()) {
            f += graph.getWeights()[edge.getStart()][edge.getEnd()];
        }

        return f;
    }

    private List<List<Vertex>> findAll(List<List<Vertex>> all, List<Vertex> currentList, List<Vertex> restList, int routes[][], int k, int n) {
        if (n == 0) {
            int routesCopy[][] = new int[routes.length][2];
            for (int i = 0; i < routes.length; i++) {
                for (int j = 0; j < routes[i].length; j++) {
                    routesCopy[i][j] = routes[i][j];
                }
            }

            //System.out.println("currentList");
            for (Vertex path : currentList) {
                //System.out.println(path.toString());
                for (int route[] : routesCopy) {
                    if (route[0] == path.getHeadIndex() && route[1] == path.getIndex()) {
                        route[0] = route[1] = 0;
                        break;
                    }
                }
            }

            for (int route[] : routesCopy) {
                if (route[0] != 0 || route[1] != 0) {
                    return all;
                }
            }

            //System.out.println("currentList = OP" + currentList.size());

            /*Set<Integer> indexes = new HashSet<Integer>();
            for (Vertex vertex : currentList) {
                indexes.add(vertex.getIndex());
            }

            if (indexes.size() == currentList.size()) {
                all.add(currentList);
            }*/

            if (currentList.size() == routes.length) {
                all.add(currentList);
            }

            return all;
        }

        boolean flag = false;
        //System.out.println("k = " + k);
        //System.out.println("n = " + n);
        //System.out.println("restList = " + restList.size());
        for (int i = k; i <= restList.size() - n; i++) {
            //System.out.println(i);
            flag = true;
            List<Vertex> newCurrentList = new ArrayList<Vertex>(currentList);
            newCurrentList.add(restList.get(i));
            findAll(all, newCurrentList, restList, routes, i + 1, n - 1);
        }

        if (!flag && restList.size() == routes.length) {
            all.add(restList);
        }

        return all;
    }

    public Integer findAndRemoveNextVertex(Graph graph, int vertex) {
        for (int i = 0; i < graph.getVertexNumber(); i++) {
            if (i != vertex && graph.getEdges()[vertex][i] != 0) {
                graph.getEdges()[vertex][i] = 0;
                graph.getEdges()[i][vertex] = 0;
                return i;
            }
        }

        throw new NullPointerException();
    }

    public Vertex findShortestPath(Graph graph, int startVertex, int endVertex) {
        /*if (tries.containsKey(startVertex)) {
            return tries.get(startVertex)[endVertex];
        }*/
        //System.out.println("findShortestPath");
        //System.out.println(GraphConverter.toString(graph.getWeights()));

        int weights[][] = graph.getWeights();
        Vertex vertices[] = new Vertex[graph.getVertexNumber()];
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = new Vertex(i, startVertex);
        }

        vertices[startVertex].setDistance(0);

        /*for (int it = 1; it < vertices.length; it++) { // делаем vNum - 1 итерацию
            for (int v = 0; v < vertices.length; v++) {// перебираем вершины
                if (vertices[v].isNotInfinity()) { // если до v уже дошли
                    for (int i = 0; i < graph.getVertexNumber(); i++) { // перебираем смежные вершины
                        if (weights[v][i] != 0) {
                            vertices[i].setDistance(vertices[v], weights[v][i]); // улучшаем оценку расстояния (релаксация)
                        }
                    }
                }
            }
        }*/

        while (true) {
            int currentVertex = -1;
            for (int i = 0; i < graph.getVertexNumber(); i++) { // перебираем вершины
                if (vertices[i].isNotUsed() && vertices[i].isNotInfinity() &&
                        (currentVertex == -1 || vertices[i].getDistance() < vertices[currentVertex].getDistance())) { // выбираем самую близкую непомеченную вершину
                    currentVertex = i;
                }
            }

            if (currentVertex == -1) // ближайшая вершина не найдена
                break;

            vertices[currentVertex].setUsed(true); // помечаем ее
            for (int i = 0; i < graph.getVertexNumber(); i++) {
                if (vertices[i].isNotUsed() && weights[currentVertex][i] != 0) { // для всех непомеченных смежных
                    vertices[i].setDistance(vertices[currentVertex], weights[currentVertex][i]); // улучшаем оценку расстояния (релаксация)
                }
            }
        }

        //for (Vertex vertex : vertices) {
            //System.out.println(vertex.toString());
        //}
        //System.out.println("======================");

        return vertices[endVertex];
    }

    public boolean compare(List<ShortestPaths> newPaths, List<ShortestPaths> oldPaths) {
        Set<Vertex> newPathsSet = new HashSet<Vertex>();
        Set<Vertex> oldPathsSet = new HashSet<Vertex>();

        for (ShortestPaths paths : newPaths) {
            newPathsSet.addAll(paths.getPaths());
        }

        for (ShortestPaths paths : oldPaths) {
            oldPathsSet.addAll(paths.getPaths());
        }

        return newPathsSet.equals(oldPathsSet);
    }
}
