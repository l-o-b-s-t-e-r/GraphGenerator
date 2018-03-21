package generator;

import generator.IGraphGenerator;
import model.Graph;
import model.Problem;

import java.util.*;

/**
 * Created by Lobster on 14.03.18.
 */
public class GraphGenerator implements IGraphGenerator {

    //чтобы не путаться: первый индекс строка, второй индекс столбец - new int[строка][столбец]

    private final Integer MAX_EDGE_WEIGHT = 10; // максимальный вес ребра
    private Random random = new Random();

    //генерит рандомный граф и маршруты
    public Problem generate(int vertexNumber, int routesNumber) {
        Graph graph = new Graph(vertexNumber);
        for (int i = 0; i < vertexNumber - 1; i++) {
            for (int j = i + 1; j < vertexNumber; j++) {
                if (random.nextBoolean()) {
                    graph.setEdge(i, j, random.nextInt(MAX_EDGE_WEIGHT) + 1);
                }
            }
        }

        int routes[][] = new int[routesNumber][2];
        for (int i = 0; i < routesNumber; i++) {
            routes[i][0] = random.nextInt(vertexNumber);
            routes[i][1] = random.nextInt(vertexNumber);
        }

        return new Problem(graph, routes);
    }
}
