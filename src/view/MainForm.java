package view;

import generator.GraphGenerator;
import generator.IGraphGenerator;
import generator.MockGraphGenerator;
import model.MathRepresentation;
import model.Problem;
import utils.GraphConverter;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Lobster on 21.03.18.
 */
public class MainForm extends JFrame {
    private JPanel panel1;
    private JTextArea mathRepresentationView;
    private JButton copyButton;
    private JTextField verticesNumber;
    private JTextField routesNumber;
    private JCheckBox mockCheckBox;
    private JButton generateButton;
    private JTextArea solutionView;
    private JButton parseButton;

    private GraphConverter converter = new GraphConverter();

    public static void main(String[] args) {
        new MainForm();
    }

    public MainForm() {
        setTitle("GraphGenerator");
        setSize(1000, 700);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        setContentPane(panel1);
        setVisible(true);
        initListeners();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void initListeners() {
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mathRepresentationView.setText("");

                if (!isInputDataValid()) {
                    JOptionPane.showMessageDialog(MainForm.this,
                            "Invalid Input",
                            "Oops!",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                MathRepresentation mathRepresentation = calculate(
                        mockCheckBox.isSelected() ? new MockGraphGenerator() : new GraphGenerator(),
                        Integer.valueOf(verticesNumber.getText().trim()),
                        Integer.valueOf(routesNumber.getText().trim())
                );

                if (mathRepresentation == null) {
                    JOptionPane.showMessageDialog(MainForm.this,
                            String.format("There is no graph with %s vertices and %s routes", verticesNumber.getText().trim(), routesNumber.getText()),
                            "Oops!",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                mathRepresentationView.append(String.format("intcon = [1,%d];\n\n", mathRepresentation.getF().length));
                mathRepresentationView.append("f = " + GraphConverter.toString(mathRepresentation.getF()) + ";\n\n");
                mathRepresentationView.append("A = " + GraphConverter.toString(mathRepresentation.getA()) + ";\n\n");
                mathRepresentationView.append("b = " + GraphConverter.toString(mathRepresentation.getB()) + ";\n\n");
                mathRepresentationView.append("Aeq = " + GraphConverter.toString(mathRepresentation.getAeq()) + ";\n\n");
                mathRepresentationView.append("beq = " + GraphConverter.toString(mathRepresentation.getBeq()) + ";\n\n");

                mathRepresentationView.append(String.format("lb = zeros(%d,1)\n\n", mathRepresentation.getF().length));
                mathRepresentationView.append(String.format("ub = ones(%d,1)\n\n", mathRepresentation.getF().length));

                mathRepresentationView.append("x = intlinprog(f,intcon,A,b,Aeq,beq,lb,ub)");
            }
        });

        parseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String solution = solutionView.getText().replace(" ", "");
                solution = solution.replace("\n", "");
                solutionView.setText("");

                char solutionsAsArray[] = solution.toCharArray();
                int vertexNumber = Integer.valueOf(verticesNumber.getText().trim());
                int routeNumber = Integer.valueOf(routesNumber.getText().trim());

                int solutionAsMatrix[][] = new int[vertexNumber][vertexNumber];
                for (int k = 0; k < routeNumber; k++) {
                    for (int i = 0; i < vertexNumber; i++) {
                        for (int j = 0; j < vertexNumber; j++) {
                            solutionAsMatrix[i][j] = Character.getNumericValue(solutionsAsArray[k * vertexNumber * vertexNumber + j + i * vertexNumber]);
                        }
                    }

                    solutionView.append(parseSolution(solutionAsMatrix));
                    solutionView.append("\n");
                }
            }
        });

        copyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringSelection stringSelection = new StringSelection(mathRepresentationView.getText());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
            }
        });
    }

    private String parseSolution(int solution[][]) {
        StringBuilder parsedSolution = new StringBuilder();
        int previousVertex = findStartVertex(solution);
        int currentVertex = previousVertex;

        do {
            for (int j = 0; j < solution[currentVertex].length; j++) {
                if (solution[currentVertex][j] == 1) {
                    previousVertex = currentVertex;
                    currentVertex = j;
                    parsedSolution.append(String.valueOf(previousVertex + 1)).append("->");
                    break;
                }

                previousVertex = currentVertex;
            }
        } while (previousVertex != currentVertex);

        parsedSolution.append(String.valueOf(previousVertex + 1));

        return parsedSolution.toString();
    }

    private int findStartVertex(int solution[][]) {
        for (int i = 0; i < solution.length; i++) {
            for (int j = 0; j < solution[i].length; j++) {
                if (solution[i][j] == 1) {
                    boolean flag = true;
                    for (int k = 0; k < solution.length; k++) {
                        if (solution[k][i] == 1) {
                            flag = false;
                            break;
                        }
                    }

                    if (flag) {
                        return i;
                    }
                }
            }
        }

        return -1;
    }

    private boolean isInputDataValid() {
        if (isNotNumeric(verticesNumber.getText().trim()) || isNotNumeric(routesNumber.getText().trim())) {
            return false;
        }

        return true;
    }

    private boolean isNotNumeric(String maybeNumeric) {
        return maybeNumeric == null || maybeNumeric.isEmpty() || !maybeNumeric.matches("[0-9]+");
    }


    private MathRepresentation calculate(IGraphGenerator generator, int vertexNumber, int routesNumber) {
        Problem problem = generator.generate(vertexNumber, routesNumber);
        if (problem == null) {
            return null;
        }

        mathRepresentationView.append("Graph = " + GraphConverter.toString(problem.getGraph().getWeights()) + ";\n\n");
        mathRepresentationView.append("Graph = " + GraphConverter.graphToString(problem.getGraph()) + ";\n\n");
        mathRepresentationView.append("Routes = " + GraphConverter.routesToString(problem.getRoutes()) + ";\n\n");
        mathRepresentationView.append("==================================================================\n\n");

        int f[] = converter.createVectorF(routesNumber, problem.getGraph());
        int A[][] = converter.createMatrixA(routesNumber, problem.getGraph());
        int b[] = converter.createVectorB(problem.getGraph());
        int beq[] = converter.createVectorBeq(routesNumber, problem.getGraph());
        int Aeq[][] = converter.createMatrixAeq(problem.getRoutes(), beq, problem.getGraph());


        return new MathRepresentation(f, A, b, beq, Aeq);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel1.add(panel2, BorderLayout.NORTH);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3);
        final JLabel label1 = new JLabel();
        label1.setText("Vertices Number");
        panel3.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        verticesNumber = new JTextField();
        panel3.add(verticesNumber, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Routes Number");
        panel3.add(label2, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        routesNumber = new JTextField();
        panel3.add(routesNumber, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        mockCheckBox = new JCheckBox();
        mockCheckBox.setText("Mock");
        panel3.add(mockCheckBox, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        generateButton = new JButton();
        generateButton.setText("Generate!");
        panel3.add(generateButton, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel4, BorderLayout.CENTER);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel5, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel5.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        mathRepresentationView = new JTextArea();
        scrollPane1.setViewportView(mathRepresentationView);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel6, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        panel6.add(scrollPane2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        solutionView = new JTextArea();
        scrollPane2.setViewportView(solutionView);
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel7, BorderLayout.SOUTH);
        copyButton = new JButton();
        copyButton.setText("Copy");
        panel7.add(copyButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        parseButton = new JButton();
        parseButton.setText("Parse");
        panel7.add(parseButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
