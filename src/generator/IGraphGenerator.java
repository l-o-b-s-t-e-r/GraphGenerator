package generator;

import model.Graph;
import model.Problem;

/**
 * Created by Lobster on 21.03.18.
 */
public interface IGraphGenerator {
    Problem generate(int vertexNumber, int routesNumber);
}
