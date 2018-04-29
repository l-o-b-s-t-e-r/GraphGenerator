package model;

/**
 * Created by Lobster on 26/04/18.
 */
public class Edge {

    private Integer start;
    private Integer end;
    private Edge reverseEdge;

    public Edge() {

    }

    public Edge(Integer v1, Integer v2) {
        setVertices(v1, v2);
    }

    public void setVertices(Integer v1, Integer v2) {
        this.start = v1;
        this.end = v2;
    }

    public Edge getReverseEdge() {
        if (reverseEdge == null) {
            reverseEdge = new Edge(end, start);
        }

        return reverseEdge;
    }

    public Boolean equal(Vertex v1, Vertex v2) {
        //System.out.println("eq = " + this.start + "," + this.end);
        return (this.start.equals(v1.getIndex()) && this.end.equals(v2.getIndex())) || (this.start.equals(v2.getIndex()) && this.end.equals(v1.getIndex()));
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (start != null ? !start.equals(edge.start) : edge.start != null) return false;
        return end != null ? end.equals(edge.end) : edge.end == null;
    }

    @Override
    public int hashCode() {
        int result = start != null ? start.hashCode() : 0;
        result = 31 * result + (end != null ? end.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "start=" + (start + 1) +
                ", end=" + (end + 1) +
                '}';
    }
}
