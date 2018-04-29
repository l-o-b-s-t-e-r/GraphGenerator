package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Lobster on 26/04/18.
 */
public class Vertex {

    private Vertex parent;
    private Integer headIndex;
    private Integer index = 0;
    private Integer distance = Integer.MAX_VALUE;
    private Boolean used = false;
    private Set<Edge> edges;

    public Vertex(Integer index, Integer headIndex) {
        this.index = index;
        this.headIndex = headIndex;
    }

    public Integer getHeadIndex() {
        return headIndex;
    }

    public void setDistance(Vertex parent, Integer weight) {
        if (parent.getDistance() + weight < distance) {
            distance = parent.getDistance() + weight;
            this.parent = parent;
        }
    }

    public Boolean isNotInfinity() {
        return distance != Integer.MAX_VALUE;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Boolean isUsed() {
        return used;
    }

    public Boolean isNotUsed() {
        return !used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    public Vertex getParent() {
        return parent;
    }

    public void setParent(Vertex parent) {
        this.parent = parent;
    }

    public int getDistance() {
        return distance;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Boolean hasParent() {
        return parent != null;
    }

    public Set<Edge> getEdges() {
        if (edges == null) {
            edges = new HashSet<Edge>();
            Vertex currentVertex = this;

            do {
                //edges.add(new Edge(currentVertex.getIndex(), currentVertex.getParent().getIndex()));
                edges.add(new Edge(currentVertex.getParent().getIndex(), currentVertex.getIndex()));
                currentVertex = currentVertex.getParent();
            } while (currentVertex.hasParent());
        }

        return edges;
    }

    public Set<Edge> getOneDirectedEdges() {
        Vertex currentVertex = this;
        Set<Edge> edges = new HashSet<Edge>();

        do {
            edges.add(new Edge(currentVertex.getIndex(), currentVertex.getParent().getIndex()));
            currentVertex = currentVertex.getParent();
        } while (currentVertex.hasParent());

        return edges;
    }

    @Override
    public String toString() {
        List<Integer> indexes = new ArrayList<Integer>();
        Vertex currentVertex = this;
        while (currentVertex.hasParent()) {
            indexes.add(currentVertex.getIndex() + 1);
            currentVertex = currentVertex.getParent();
        }
        indexes.add(currentVertex.getIndex() + 1);

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = indexes.size() - 1; i > 0; i--) {
            stringBuilder.append(indexes.get(i) + "->");
        }
        stringBuilder.append(indexes.get(0));

        return stringBuilder.toString();
    }

    public int length() {
        Vertex currentVertex = this;
        int length = 0;
        while (currentVertex.hasParent()) {
            length++;
            currentVertex = currentVertex.getParent();
        }

        return length + 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vertex vertex = (Vertex) o;

        if (parent != null ? !parent.equals(vertex.parent) : vertex.parent != null) return false;
        if (headIndex != null ? !headIndex.equals(vertex.headIndex) : vertex.headIndex != null) return false;
        return index != null ? index.equals(vertex.index) : vertex.index == null;
    }

    @Override
    public int hashCode() {
        int result = parent != null ? parent.hashCode() : 0;
        result = 31 * result + (headIndex != null ? headIndex.hashCode() : 0);
        result = 31 * result + (index != null ? index.hashCode() : 0);
        return result;
    }
}
