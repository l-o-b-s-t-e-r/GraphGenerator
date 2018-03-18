import java.util.List;

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

    //устанавливает ребро между i-ой и j-ой вершинами и назначает вес ребру.
    public void setEdge(int i, int j, int weight) {
        edges[i][j] = edges[j][i] = 1;
        weights[i][j] = weights[j][i] = weight;
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
