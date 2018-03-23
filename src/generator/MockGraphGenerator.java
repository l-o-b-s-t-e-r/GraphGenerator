package generator;

import model.Graph;
import model.Problem;

/**
 * Created by Lobster on 21.03.18.
 */
public class MockGraphGenerator implements IGraphGenerator {

    //Матрица с тестовыми данными
    //в i-ой строке содержаться все модельки у которых количество вершин = i
    //в j-ом столбце хранятся все такие модельки для которых нужно найти j маршрутов
    //поэтому удобно, если нужно взять тестовый пример для графа с 5ю вершинами и 3мя маршрутами, то просто problems[5][3].
    private Problem problems[][];

    public MockGraphGenerator() {
        problems = new Problem[10][10];

        //Тестовый пример №1
        //Граф с 2мя вершинами и нужно найти 1 путь
        int graph[][] = new int[][]{
                {0, 1},
                {1, 0},
        };

        int weights[][] = new int[][]{
                {0, 5},
                {5, 0},
        };

        int routes[][] = new int[][]{
                {0, 1}
        };
        problems[graph.length][routes.length] = new Problem(new Graph(graph, weights), routes);

        //Тестовый пример №3
        //Граф с 3мя вершинами и нужно найти 1 путь
        graph = new int[][]{
                {0, 1, 0},
                {1, 0, 1},
                {0, 1, 0}
        };

        weights = new int[][]{
                {0, 5, 0},
                {5, 0, 6},
                {5, 7, 0}
        };

        routes = new int[][]{
                {0, 2}
        };
        problems[graph.length][routes.length] = new Problem(new Graph(graph, weights), routes);

        //Тестовый пример №3
        //Граф с 3мя вершинами и нужно найти 2 пути
        graph = new int[][]{
                {0, 1, 1},
                {1, 0, 1},
                {1, 1, 0}
        };

        weights = new int[][]{
                {0, 6, 5},
                {6, 0, 7},
                {5, 7, 0}
        };

        routes = new int[][]{
                {0, 2},
                {0, 2}
        };
        problems[graph.length][routes.length] = new Problem(new Graph(graph, weights), routes);

        //Тестовый пример №4
        //Граф с 3мя вершинами и нужно найти 3 пути
        graph = new int[][]{
                {0, 1, 1},
                {1, 0, 1},
                {1, 1, 0}
        };

        weights = new int[][]{
                {0, 6, 5},
                {6, 0, 7},
                {5, 7, 0}
        };

        routes = new int[][]{
                {0, 1},
                {1, 2},
                {2, 0}
        };
        problems[graph.length][routes.length] = new Problem(new Graph(graph, weights), routes);

        //Тестовый пример №5
        //Граф с 4мя вершинами и нужно найти 1 путь
        graph = new int[][]{
                {0, 1, 0, 1},
                {1, 0, 1, 0},
                {0, 1, 0, 1},
                {1, 0, 1, 0}
        };

        weights = new int[][]{
                {0, 2, 0, 9},
                {2, 0, 2, 0},
                {0, 2, 0, 2},
                {9, 0, 2, 0}
        };

        routes = new int[][]{
                {0, 3}
        };
        problems[graph.length][routes.length] = new Problem(new Graph(graph, weights), routes);

        //Тестовый пример №6
        //Граф с 4мя вершинами и нужно найти 2 путь
        graph = new int[][]{
                {0, 1, 1, 1},
                {1, 0, 0, 1},
                {1, 0, 0, 1},
                {1, 1, 1, 0}
        };

        weights = new int[][]{
                {0, 2, 3, 7},
                {2, 0, 0, 2},
                {3, 0, 0, 3},
                {7, 2, 3, 0}
        };

        routes = new int[][]{
                {0, 3},
                {0, 3}
        };
        problems[graph.length][routes.length] = new Problem(new Graph(graph, weights), routes);

        //Тестовый пример №7
        //Граф с 4мя вершинами и нужно найти 2 пути
        graph = new int[][]{
                {0, 1, 1, 1},
                {1, 0, 0, 1},
                {1, 0, 0, 1},
                {1, 1, 1, 0}
        };

        weights = new int[][]{
                {0, 2, 3, 7},
                {2, 0, 0, 2},
                {3, 0, 0, 3},
                {7, 2, 3, 0}
        };

        routes = new int[][]{
                {0, 3},
                {0, 3}
        };
        problems[graph.length][routes.length] = new Problem(new Graph(graph, weights), routes);

        //Тестовый пример №8
        //Граф с 4мя вершинами и нужно найти 3 пути
        graph = new int[][]{
                {0, 1, 1, 1},
                {1, 0, 0, 1},
                {1, 0, 0, 1},
                {1, 1, 1, 0}
        };

        weights = new int[][]{
                {0, 2, 3, 7},
                {2, 0, 0, 2},
                {3, 0, 0, 3},
                {7, 2, 3, 0}
        };

        routes = new int[][]{
                {0, 3},
                {0, 3},
                {0, 3}
        };
        problems[graph.length][routes.length] = new Problem(new Graph(graph, weights), routes);

        //Тестовый пример №9
        //Граф с 4мя вершинами и нужно найти 4 пути
        graph = new int[][]{
                {0, 1, 1, 1},
                {1, 0, 1, 1},
                {1, 1, 0, 1},
                {1, 1, 1, 0}
        };

        weights = new int[][]{
                {0, 2, 3, 4},
                {2, 0, 5, 6},
                {3, 5, 0, 7},
                {4, 6, 7, 0}
        };

        routes = new int[][]{
                {0, 3},
                {0, 3},
                {0, 3},
                {1, 2}
        };
        problems[graph.length][routes.length] = new Problem(new Graph(graph, weights), routes);

        //Тестовый пример №10
        //Граф с 5ю вершинами и нужно найти 1 путь
        graph = new int[][]{
                {0, 1, 1, 0, 0},
                {1, 0, 1, 1, 0},
                {1, 1, 0, 1, 1},
                {0, 1, 1, 0, 1},
                {0, 0, 1, 1, 0},
        };

        weights = new int[][]{
                {0, 9, 1, 0, 0},
                {9, 0, 1, 8, 0},
                {1, 1, 0, 1, 1},
                {0, 8, 1, 0, 1},
                {0, 0, 1, 1, 0},
        };

        routes = new int[][]{
                {0, 1}
        };
        problems[graph.length][routes.length] = new Problem(new Graph(graph, weights), routes);

        //Тестовый пример №11
        //Граф с 5ю вершинами и нужно найти 2 пути
        graph = new int[][]{
                {0, 1, 1, 0, 0},
                {1, 0, 1, 1, 0},
                {1, 1, 0, 1, 1},
                {0, 1, 1, 0, 1},
                {0, 0, 1, 1, 0},
        };

        weights = new int[][]{
                {0, 9, 1, 0, 0},
                {9, 0, 1, 8, 0},
                {1, 1, 0, 1, 1},
                {0, 8, 1, 0, 1},
                {0, 0, 1, 1, 0},
        };

        routes = new int[][]{
                {0, 1},
                {0, 3}
        };
        problems[graph.length][routes.length] = new Problem(new Graph(graph, weights), routes);

        //Тестовый пример №12
        //Граф с 5ю вершинами и нужно найти 3 пути
        graph = new int[][]{
                {0, 1, 1, 0, 0},
                {1, 0, 1, 1, 0},
                {1, 1, 0, 1, 1},
                {0, 1, 1, 0, 1},
                {0, 0, 1, 1, 0},
        };

        weights = new int[][]{
                {0, 3, 2, 0, 0},
                {3, 0, 4, 5, 0},
                {2, 4, 0, 6, 7},
                {0, 5, 6, 0, 1},
                {0, 0, 7, 1, 0},
        };

        routes = new int[][]{
                {0, 4},
                {0, 3},
                {1, 4}
        };
        problems[graph.length][routes.length] = new Problem(new Graph(graph, weights), routes);

        //Тестовый пример №13
        //Граф с 5ю вершинами и нужно найти 4 пути
        graph = new int[][]{
                {0, 1, 1, 0, 0},
                {1, 0, 1, 1, 0},
                {1, 1, 0, 1, 1},
                {0, 1, 1, 0, 1},
                {0, 0, 1, 1, 0},
        };

        weights = new int[][]{
                {0, 3, 2, 0, 0},
                {3, 0, 4, 5, 0},
                {2, 4, 0, 6, 7},
                {0, 5, 6, 0, 1},
                {0, 0, 7, 1, 0},
        };

        routes = new int[][]{
                {0, 3},
                {0, 3},
                {1, 4},
                {3, 4}
        };
        problems[graph.length][routes.length] = new Problem(new Graph(graph, weights), routes);


        //Тестовый пример №14
        //Граф с 5ю вершинами и нужно найти 5 путей
        graph = new int[][]{
                {0, 1, 1, 0, 0},
                {1, 0, 1, 1, 0},
                {1, 1, 0, 1, 1},
                {0, 1, 1, 0, 1},
                {0, 0, 1, 1, 0},
        };

        weights = new int[][]{
                {0, 3, 2, 0, 0},
                {3, 0, 4, 5, 0},
                {2, 4, 0, 6, 7},
                {0, 5, 6, 0, 1},
                {0, 0, 7, 1, 0},
        };

        routes = new int[][]{
                {0, 3},
                {1, 3},
                {0, 2},
                {2, 4},
                {3, 4}
        };
        problems[graph.length][routes.length] = new Problem(new Graph(graph, weights), routes);

        //Тестовый пример №15
        //Граф с 5ю вершинами и нужно найти 6 путей
        graph = new int[][]{
                {0, 1, 1, 0, 0},
                {1, 0, 1, 1, 0},
                {1, 1, 0, 1, 1},
                {0, 1, 1, 0, 1},
                {0, 0, 1, 1, 0},
        };

        weights = new int[][]{
                {0, 3, 2, 0, 0},
                {3, 0, 4, 5, 0},
                {2, 4, 0, 6, 7},
                {0, 5, 6, 0, 1},
                {0, 0, 7, 1, 0},
        };

        routes = new int[][]{
                {0, 4},
                {0, 1},
                {4, 3},
                {1, 2},
                {1, 3},
                {2, 3}
        };
        problems[graph.length][routes.length] = new Problem(new Graph(graph, weights), routes);

        //Тестовый пример №16
        //Граф с 5ю вершинами и нужно найти 7 путей
        graph = new int[][]{
                {0, 1, 1, 0, 0},
                {1, 0, 1, 1, 0},
                {1, 1, 0, 1, 1},
                {0, 1, 1, 0, 1},
                {0, 0, 1, 1, 0},
        };

        weights = new int[][]{
                {0, 3, 2, 0, 0},
                {3, 0, 4, 5, 0},
                {2, 4, 0, 6, 7},
                {0, 5, 6, 0, 1},
                {0, 0, 7, 1, 0},
        };

        routes = new int[][]{
                {0, 1},
                {1, 3},
                {3, 4},
                {4, 2},
                {2, 0},
                {1, 2},
                {2, 3}
        };
        problems[graph.length][routes.length] = new Problem(new Graph(graph, weights), routes);
    }

    @Override
    public Problem generate(int vertexNumber, int routesNumber) {
        if (problems.length > vertexNumber && problems[vertexNumber].length > routesNumber) {
            return problems[vertexNumber][routesNumber];
        } else {
            return null;
        }
    }
}
