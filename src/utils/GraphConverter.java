package utils;

import model.Graph;

/**
 * Created by Lobster on 15.03.18.
 */
public class GraphConverter {

    //чтобы не путаться: первый индекс строка, второй индекс столбец - new int[строка][столбец]

    //вектор f - это коэффициенты при иксах нашей функции, которую мы будем минимизировать
    public int[] createVectorF(int routesNumber, Graph graph) {
        int f[] = new int[routesNumber * graph.getTotalElementsNumber()];
        for (int k = 0; k < routesNumber; k++) {
            for (int i = 0; i < graph.getVertexNumber(); i++) {
                for (int j = 0; j < graph.getVertexNumber(); j++) {
                    f[k * graph.getTotalElementsNumber() + j + i * graph.getVertexNumber()] = graph.getEdgeWeight(i, j);
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
                    beq[i + k * kMatrixAeq.length] = 1;
                } else if (pairs[k][1] == i) {
                    beq[i + k * kMatrixAeq.length] = -1;
                }
            }

            /*int secondRestrictionAeq[][] = new int[1][graph.getTotalElementsNumber()];
            for (int i = 0; i < secondRestrictionAeq[0].length; i += graph.getVertexNumber() + 1) {
                secondRestrictionAeq[0][i] = 1;
            }*/

            int thirdRestrictionAeq[][] = new int[1][graph.getTotalElementsNumber()];
            for (int i = 0; i < graph.getEdges().length; i++) {
                for (int j = 0; j < graph.getEdges()[i].length; j++) {
                    thirdRestrictionAeq[0][j + i * graph.getVertexNumber()] = graph.isEdgeExist(i, j) ? 0 : 1;
                }
            }

            System.arraycopy(firstRestrictionAeq, 0, kMatrixAeq, 0, firstRestrictionAeq.length);
            //System.arraycopy(secondRestrictionAeq, 0, kMatrixAeq, firstRestrictionAeq.length, secondRestrictionAeq.length);
            System.arraycopy(thirdRestrictionAeq, 0, kMatrixAeq, firstRestrictionAeq.length, thirdRestrictionAeq.length);

            for (int i = 0; i < kMatrixAeq.length; i++) {
                for (int j = 0; j < kMatrixAeq[i].length; j++) {
                    matrixAeq[k * kMatrixAeq.length + i][k * kMatrixAeq[i].length + j] = kMatrixAeq[i][j];
                }
            }
        }

        return matrixAeq;
    }

    //этот вектор отвечает как раз за это условие = для матрицы Aeq, которая строилась выше
    //т.е. эти уравнения имеют вид х1 + х2 = число. Матрица Aeq, представляет из себя коэфициенты при х(иксах),
    //а матрица Beq представляет результат уравнения, т.е. правую часть.
    public int[] createVectorBeq(int routesNumber, Graph graph) {
        return new int[routesNumber * (graph.getVertexNumber() + 1)];
    }

    public static String toString(int[][] matrix) {
        StringBuilder matrixStr = new StringBuilder("[\n");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length - 1; j++) {
                matrixStr.append(matrix[i][j]).append(",");
            }

            matrixStr.append(matrix[i][matrix[i].length - 1]);
            if (i != matrix.length - 1) {
                matrixStr.append(";\n");
            }
        }

        matrixStr.append("\n]");
        return matrixStr.toString();
    }

    public static String toString(Integer[][] matrix) {
        StringBuilder matrixStr = new StringBuilder("[\n");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length - 1; j++) {
                matrixStr.append(matrix[i][j]).append(",");
            }

            matrixStr.append(matrix[i][matrix[i].length - 1]);
            if (i != matrix.length - 1) {
                matrixStr.append(";\n");
            }
        }

        matrixStr.append("\n]");
        return matrixStr.toString();
    }

    public static String toString(int[] vector) {
        StringBuilder vectorStr = new StringBuilder("[");
        for (int i = 0; i < vector.length - 1; i++) {
            vectorStr.append(vector[i]).append(";");
        }

        vectorStr.append(vector[vector.length - 1]).append("]");
        return vectorStr.toString();
    }

    public static String routesToString(int[][] matrix) {
        StringBuilder matrixStr = new StringBuilder("[\n");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length - 1; j++) {
                matrixStr.append(matrix[i][j] + 1).append("->");
            }

            matrixStr.append(matrix[i][matrix[i].length - 1] + 1);
            if (i != matrix.length - 1) {
                matrixStr.append(";\n");
            }
        }

        matrixStr.append("\n]");
        return matrixStr.toString();
    }

    public static String routesToString(Integer[][] matrix) {
        StringBuilder matrixStr = new StringBuilder("[\n");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length - 1; j++) {
                matrixStr.append(matrix[i][j] + 1).append("->");
            }

            matrixStr.append(matrix[i][matrix[i].length - 1] + 1);
            if (i != matrix.length - 1) {
                matrixStr.append(";\n");
            }
        }

        matrixStr.append("\n]");
        return matrixStr.toString();
    }

    public static String graphToString(Graph graph) {
        StringBuilder matrixStr = new StringBuilder("[\n");
        for (int i = 0; i < graph.getVertexNumber(); i++) {
            for (int j = i; j < graph.getVertexNumber(); j++) {
                if (graph.isEdgeExist(i, j)) {
                    matrixStr.append(i + 1).append("->").append(j + 1).append(" = ").append(graph.getWeights()[i][j]).append(";\n");
                }
            }
        }

        matrixStr.append("]");
        return matrixStr.toString();
    }
}
