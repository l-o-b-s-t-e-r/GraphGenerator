package model;

import javafx.util.Pair;

import java.util.*;

/**
 * Created by Lobster on 28/04/18.
 */
public class ShortestPaths {

    private List<Pair<Vertex, Set<Edge>>> shortestPaths;

    public ShortestPaths() {
        shortestPaths = new ArrayList<Pair<Vertex, Set<Edge>>>();
    }

    public ShortestPaths(List<Pair<Vertex, Set<Edge>>> shortestPaths) {
        this.shortestPaths = new ArrayList<Pair<Vertex, Set<Edge>>>(shortestPaths);
    }

    public List<Pair<Vertex, Set<Edge>>> getShortestPaths() {
        return shortestPaths;
    }

    public void addPath(Vertex newPath) {
        Pair<Vertex, Set<Edge>> newPathEntry = new Pair<Vertex, Set<Edge>>(newPath, new LinkedHashSet<Edge>());
        shortestPaths.add(newPathEntry);
        for (Pair<Vertex, Set<Edge>> path : shortestPaths) {
            if (path.getKey() == newPath) {
                continue;
            }

            Pair<Set<Edge>, Set<Edge>> commonEdges = findCommonEdges(newPath.getEdges(), path.getKey().getEdges());
            //System.out.println("common edges1: " + commonEdges.getKey().size());
            //System.out.println("common edges2: " + commonEdges.getValue().size());
            newPathEntry.getValue().addAll(commonEdges.getKey());
            path.getValue().addAll(commonEdges.getValue());
        }
    }

    private Pair<Set<Edge>, Set<Edge>> findCommonEdges(Set<Edge> e1, Set<Edge> e2) {
        //System.out.println("e1 " + e1.toString());
        //System.out.println("e2" + e2.toString());
        Set<Edge> commonEdgesE1 = new LinkedHashSet<Edge>();
        Set<Edge> commonEdgesE2 = new LinkedHashSet<Edge>();

        for (Edge edge : e1) {
            if (e2.contains(edge)) {
                commonEdgesE1.add(edge);
                commonEdgesE2.add(edge);
            } else if (e2.contains(edge.getReverseEdge())) {
                commonEdgesE1.add(edge);
                commonEdgesE2.add(edge.getReverseEdge());
            }
        }

        return new Pair<Set<Edge>, Set<Edge>>(commonEdgesE1, commonEdgesE2);
    }

    public Set<Vertex> getPaths() {
        Set<Vertex> vertices = new HashSet<Vertex>();
        for (Pair<Vertex, Set<Edge>> path : shortestPaths) {
            vertices.add(path.getKey());
        }

        return vertices;
    }

    public ShortestPaths copy() {
        return new ShortestPaths(shortestPaths);
    }

    public Boolean commonEdgesNotExist() {
        for (Pair<Vertex, Set<Edge>> path : shortestPaths) {
            if (!path.getValue().isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public void replace(Vertex oldPath, Vertex newPath) {
        List<Edge> commonEdgesOfOldPath = null;
        for (int i = 0; i < shortestPaths.size(); i++) {
            if (shortestPaths.get(i).getKey().equals(oldPath)) {
                commonEdgesOfOldPath = new ArrayList<Edge>(shortestPaths.get(i).getValue());
                break;
            }
        }

        for (int i = 0; i < commonEdgesOfOldPath.size(); i++) {
            Edge edge = commonEdgesOfOldPath.get(i);
            boolean isFound = false;
            Set<Edge> commonEdges = null;
            for (Pair<Vertex, Set<Edge>> path : shortestPaths) {
                Set<Edge> edges = path.getValue();
                if (edges.contains(edge)) {
                    if (isFound) {
                        isFound = false;
                        break;
                    } else {
                        isFound = true;
                        commonEdges = edges;
                    }
                }
            }

            if (isFound) {
                commonEdges.remove(edge);
            }
        }

        addPath(newPath);
    }

    public void showCommonEdges() {
        for (Pair<Vertex, Set<Edge>> path : shortestPaths) {
            StringBuilder str = new StringBuilder(path.getKey().toString() + ": ");
            for (Edge edge : path.getValue()) {
                str.append((edge.getStart() + 1)).append("-").append((edge.getEnd() + 1)).append("; ");
            }

            System.out.println(str.toString());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShortestPaths that = (ShortestPaths) o;

        return shortestPaths != null ? shortestPaths.equals(that.shortestPaths) : that.shortestPaths == null;
    }

    @Override
    public int hashCode() {
        return shortestPaths != null ? shortestPaths.hashCode() : 0;
    }
}
