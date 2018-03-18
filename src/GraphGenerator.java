import java.util.*;

/**
 * Created by Lobster on 14.03.18.
 */
public class GraphGenerator {

    //чтобы не путаться: первый индекс строка, второй индекс столбец - new int[строка][столбец]

    private final Integer MAX_EDGE_WEIGHT = 10; // максимальный вес ребра
    private Random random = new Random();

    //генерит рандомный граф
    public Graph generate(int vertexNumber) {
        Graph graph = new Graph(vertexNumber);
        for (int i = 0; i < vertexNumber - 1; i++) {
            for (int j = i + 1; j < vertexNumber; j++) {
                if (random.nextBoolean()) {
                    graph.setEdge(i, j, random.nextInt(MAX_EDGE_WEIGHT) + 1);
                }
            }
        }

        return graph;
    }

    //вектор f - это коэффициенты при иксах нашей функции, которую мы будем минимизировать
    public int[] createVectorF(int routesNumber, Graph graph) {
        int f[] = new int[routesNumber * graph.getTotalElementsNumber()];
        for (int k = 0; k < routesNumber; k++) {
            for (int i = 0; i < graph.getVertexNumber(); i++) {
                for (int j = 0; j < graph.getVertexNumber(); j++) {
                    f[j + i * graph.getVertexNumber()] = graph.getEdgeWeight(i, j);
                }
            }
        }

        return f;
    }

    //это наше первое ограничение вида <= (меньше либо равно).
    //по сути оно говорит о том, что каждая дуга долна быть использована только 1 раз
    //допустим нам нужно найти 3 маршрута. Дуга соединяющая iю и jю вершину это х(икс) и может принимать значение 0 или 1. Она либо есть(1) либо ее нет(0).
    //т.е. сумма одной и той же дуги во всех 3х решениях должна быть <= 1
    public int[][] createMatrixA(int routesNumber, Graph graph) {
        int rowsNumber = graph.getTotalElementsNumber();
        int columnsNumber = rowsNumber * routesNumber;

        int matrixA[][] = new int[rowsNumber][columnsNumber];
        for (int k = 0; k < routesNumber; k++) {
            for (int i = 0; i < graph.getTotalElementsNumber(); i++) {
                matrixA[i][i + k * graph.getTotalElementsNumber()] = 1;
            }
        }

        return matrixA;
    }

    //этот вектор отвечает как раз за это условие <=1 для матрицы A, которая строилась выше
    //т.е. эти уравнения имеют вид х1 + х2 <= 1. Матрица A, представляет из себя коэфициенты при х(иксах),
    //а матрица b представляет результат уравнения, т.е. правую часть, т.е. просто число 1.
    public int[] createVectorB(Graph graph) {
        int b[] = new int[graph.getTotalElementsNumber()];
        for (int i = 0; i < b.length; i++) {
            b[i] = 1;
        }

        return b;
    }

    //тут жопа
    //тут 3 ограничения
    //1ое говорит о том, что кол-во дуг(для каждой вершины) в нашем маршруте(который мы найдем), должно иметь ипределенное кол-во
    //из вершины, которая является началом маршрута, обазательно должна исходить только 1 дуга.
    //из вершины, которая является концом маршрута, обазательно должна входить только 1 дуга.
    //из вершин, которые лежат внутри маршрута, кол-во входящих дуг должно равняться кол-ву исходящих

    //2ое говорит о том, что не может быть дуги из i-й вершины в i-ю, т.е. дуги из себя в себя же как бы :D
    //3е говорит о том, что если изначально в графе не было дуги из i в j, то и в конечном решении не должно быть таких дуг.
    public int[][] createMatrixAeq(int pairs[][], int beq[], Graph graph) {
        int rowsNumber = beq.length;
        int columnsNumber = pairs.length * graph.getTotalElementsNumber();

        int matrixAeq[][] = new int[rowsNumber][columnsNumber];

        for (int k = 0; k < pairs.length; k++) {
            int kMatrixAeq[][] = new int[beq.length / pairs.length][graph.getTotalElementsNumber()];

            int firstRestrictionAeq[][] = new int[graph.getVertexNumber()][graph.getTotalElementsNumber()];
            for (int i = 0; i < firstRestrictionAeq.length; i++) {
                int row[][] = new int[graph.getVertexNumber()][graph.getVertexNumber()];
                for (int j = 0; j < row.length; j++) {
                    if (i != j) {
                        row[i][j] = 1;
                        row[j][i] = -1;
                    }
                }

                for (int ii = 0; ii < row.length; ii++) {
                    for (int jj = 0; jj < row[ii].length; jj++) {
                        firstRestrictionAeq[i][jj + ii * row[ii].length] = row[ii][jj];
                    }
                }

                if (pairs[k][0] == i) {
                    beq[i + k * graph.getVertexNumber()] = 1;
                } else if (pairs[k][1] == i) {
                    beq[i + k * graph.getVertexNumber()] = -1;
                }
            }

            int secondRestrictionAeq[][] = new int[1][graph.getTotalElementsNumber()];
            for (int i = 0; i < secondRestrictionAeq[0].length; i += graph.getVertexNumber() + 1) {
                secondRestrictionAeq[0][i] = 1;
            }

            int thirdRestrictionAeq[][] = new int[1][graph.getTotalElementsNumber()];
            for (int i = 0; i < graph.getEdges().length; i++) {
                for (int j = 0; j < graph.getEdges()[i].length; j++) {
                    thirdRestrictionAeq[0][j + i * graph.getVertexNumber()] = graph.isEdgeExist(i, j) ? 0 : 1;
                }
            }

            System.arraycopy(firstRestrictionAeq, 0, kMatrixAeq, 0, firstRestrictionAeq.length);
            System.arraycopy(secondRestrictionAeq, 0, kMatrixAeq, firstRestrictionAeq.length, secondRestrictionAeq.length);
            System.arraycopy(thirdRestrictionAeq, 0, kMatrixAeq, firstRestrictionAeq.length + secondRestrictionAeq.length, thirdRestrictionAeq.length);

            System.arraycopy(kMatrixAeq, 0, matrixAeq, k * kMatrixAeq.length, kMatrixAeq.length);
        }

        return matrixAeq;
    }

    //этот вектор отвечает как раз за это условие = для матрицы Aeq, которая строилась выше
    //т.е. эти уравнения имеют вид х1 + х2 = число. Матрица Aeq, представляет из себя коэфициенты при х(иксах),
    //а матрица Beq представляет результат уравнения, т.е. правую часть.
    public int[] createVectorBeq(int routesNumber, Graph graph) {
        return new int[routesNumber * (graph.getVertexNumber() + 2)];
    }

    //генерит маршруты
    public int[][] createRoutes(int routesNumber, Graph graph) {
        int pairs[][] = new int[routesNumber][2];
        for (int i = 0; i < routesNumber; i++) {
            pairs[i][0] = random.nextInt(graph.getVertexNumber());
            pairs[i][1] = random.nextInt(graph.getVertexNumber());
        }

        return pairs;
    }

    public Graph getMockGraph5() {
        Graph graph = new Graph(5);

        graph.setEdges(new int[][]{
                {0, 1, 1, 0, 0},
                {1, 0, 1, 1, 0},
                {1, 1, 0, 1, 1},
                {0, 1, 1, 0, 1},
                {0, 0, 1, 1, 0},
        });

        graph.setWeights(new int[][]{
                {0, 3, 2, 0, 0},
                {3, 0, 4, 5, 0},
                {2, 4, 0, 6, 7},
                {0, 5, 6, 0, 1},
                {0, 0, 7, 1, 0},
        });

        return graph;
    }

    /*public Graph getMockGraph3() {
        Graph graph = new Graph(3);

        graph.setEdges(new int[][]{
                {0, 1, 1},
                {1, 0, 1},
                {1, 1, 0}
        });

        graph.setWeights(new int[][]{
                {0, 5, 10},
                {5, 0, 6},
                {10, 6, 0}
        });

        return graph;
    }

    public int[][] getMockPairs3() {
        return new int[][]{
                {0, 2}
        };
    }*/

    public Graph getMockGraph3() {
        Graph graph = new Graph(3);

        graph.setEdges(new int[][]{
                {0, 1, 0},
                {1, 0, 1},
                {0, 1, 0}
        });

        graph.setWeights(new int[][]{
                {0, 1, 0},
                {1, 0, 1},
                {0, 1, 0}
        });

        return graph;
    }

    public int[][] getMockPairs3() {
        return new int[][]{
                {0, 2}
        };
    }

    public int[][] getMockRoutes5() {
        return new int[][]{
                {0, 4}//,
                //{0, 3},
                //{1, 4}
        };
    }
}
