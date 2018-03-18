import java.io.*;

public class Main {

    //если используешь тестовые
    private static final int VERTEX_NUMBER = 5; //кол-во вершин в графе
    private static final int ROUTES_NUMBER = 1; //кол-во маршрутов, которые надо будет найти

    public static void main(String[] args) {
        //генератор, который генерит все: граф, маршруты, матрицы для матлаба
        GraphGenerator generator = new GraphGenerator();

        //Graph graph = generator.generate(VERTEX_NUMBER); //генерация рандомного граф
        Graph graph = generator.getMockGraph5(); //берет заранее созданный граф, удобно для тестирования

        try {
            //та след. 3 строчки забей
            File file = new File("graph.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(String.format("intcon = [1,%d];\n", graph.getTotalElementsNumber()));

            //int pairs[][] = generator.createRoutes(ROUTES_NUMBER, graph); //генерация рандомных маршрутов
            int pairs[][] = generator.getMockRoutes5(); //берет заранее созданные маршруты , удобно для тестирования

            //чтобы понять примерно что такое f,A,b,Aeq,beq смотри примеры отсюда https://www.mathworks.com/help/optim/ug/linprog.html
            int f[] = generator.createVectorF(pairs.length, graph);
            int A[][] = generator.createMatrixA(pairs.length, graph);
            int b[] = generator.createVectorB(graph);
            int beq[] = generator.createVectorBeq(pairs.length, graph);
            int Aeq[][] = generator.createMatrixAeq(pairs, beq, graph);

            //дальше забей
            writer.write("f = " + StringConverter.toString(f) + ";\n");
            writer.write("A = " + StringConverter.toString(A) + ";\n");
            writer.write("b = " + StringConverter.toString(b) + ";\n");
            writer.write("Aeq = " + StringConverter.toString(Aeq) + ";\n");
            writer.write("beq = " + StringConverter.toString(beq) + ";\n");

            writer.write(String.format("lb = zeros(%d,1)\n", graph.getTotalElementsNumber()));
            writer.write(String.format("ub = ones(%d,1)\n", graph.getTotalElementsNumber()));

            writer.write("x = intlinprog(f,intcon,A,b,Aeq,beq,lb,ub)");

            writer.close();
            System.out.println("Success!");
        } catch (IOException ex) {
            ex.fillInStackTrace();
        }
    }
}
