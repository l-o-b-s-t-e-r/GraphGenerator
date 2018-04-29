package model;

/**
 * Created by Lobster on 14.03.18.
 */
public class Graph {

    private int vertexNumber; //кол-во вершин
    private int edges[][]; //матрица, которая представляет наш граф из 0 и 1.
    private int weights[][]; //матрица весов

    public Graph(Integer vertexNumber) {
        this.vertexNumber = vertexNumber;
        edges = new int[vertexNumber][vertexNumber];
        weights = new int[vertexNumber][vertexNumber];
    }

    public Graph(int[][] edges, int[][] weights) {
        this.vertexNumber = edges.length;
        this.edges = new int[vertexNumber][vertexNumber];
        this.weights = new int[vertexNumber][vertexNumber];

        for (int i = 0; i < edges.length; i++) {
            for (int j = 0; j < edges[i].length; j++) {
                this.edges[i][j] = edges[i][j];
                this.weights[i][j] = weights[i][j];
            }
        }
    }

    public Graph copy() {
        return new Graph(edges, weights);
    }

    //устанавливает ребро между i-ой и j-ой вершинами и назначает вес ребру.
    public void setEdge(int i, int j, int weight) {
        edges[i][j] = edges[j][i] = 1;
        weights[i][j] = weights[j][i] = weight;
    }

    public void setEdge(Edge edge, int weight) {
        edges[edge.getStart()][edge.getEnd()] = edges[edge.getEnd()][edge.getStart()] = 1;
        weights[edge.getStart()][edge.getEnd()] = weights[edge.getEnd()][edge.getStart()] = weight;
    }

    public void removeEdge(int i, int j) {
        edges[i][j] = edges[j][i] = weights[i][j] = weights[j][i] = 0;
    }

    public int removeEdge(Edge edge) {
        int w = weights[edge.getStart()][edge.getEnd()];
        edges[edge.getStart()][edge.getEnd()] = edges[edge.getEnd()][edge.getStart()] = weights[edge.getStart()][edge.getEnd()] = weights[edge.getEnd()][edge.getStart()] = 0;
        return w;
    }

    //проверка есть ли ребро между i-ой и j-ой вершинами
    public boolean isEdgeExist(int i, int j) {
        return edges[i][j] == 1;
    }

    //общее кол-во элементов в матрице,
    //фактически это число представляем собой кол-во переменных (х - иксов) в нашем линейном уравнение, которое мы пытаемся решить
    public int getTotalElementsNumber() {
        return vertexNumber * vertexNumber;
    }

    public int getEdgeWeight(int i, int j) {
        return weights[i][j];
    }

    public int getVertexNumber() {
        return vertexNumber;
    }

    public void setVertexNumber(int vertexNumber) {
        this.vertexNumber = vertexNumber;
    }

    public int[][] getEdges() {
        return edges;
    }

    public void setEdges(int[][] edges) {
        this.edges = edges;
    }

    public int[][] getWeights() {
        return weights;
    }

    public void setWeights(int[][] weights) {
        this.weights = weights;
    }
}
