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
import java.util.Arrays;

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

    private GraphConverter converter = new GraphConverter();

    public static void main(String[] args) {
        new MainForm();
    }

    public MainForm() {
        setTitle("GraphGenerator");
        setSize(1000, 600);
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
                        Integer.valueOf(routesNumber.getText())
                );

                if (mathRepresentation == null) {
                    JOptionPane.showMessageDialog(MainForm.this,
                            String.format("There is no graph with %s vertices and %s routes", verticesNumber.getText().trim(), routesNumber.getText()),
                            "Oops!",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                mathRepresentationView.setText("");
                mathRepresentationView.append(String.format("intcon = [1,%d];\n", mathRepresentation.getF().length));
                mathRepresentationView.append("f = " + GraphConverter.toString(mathRepresentation.getF()) + ";\n");
                mathRepresentationView.append("A = " + GraphConverter.toString(mathRepresentation.getA()) + ";\n");
                mathRepresentationView.append("b = " + GraphConverter.toString(mathRepresentation.getB()) + ";\n");
                mathRepresentationView.append("Aeq = " + GraphConverter.toString(mathRepresentation.getAeq()) + ";\n");
                mathRepresentationView.append("beq = " + GraphConverter.toString(mathRepresentation.getBeq()) + ";\n");

                mathRepresentationView.append(String.format("lb = zeros(%d,1)\n", mathRepresentation.getF().length));
                mathRepresentationView.append(String.format("ub = ones(%d,1)\n", mathRepresentation.getF().length));

                mathRepresentationView.append("x = intlinprog(f,intcon,A,b,Aeq,beq,lb,ub)");
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
        panel4.setLayout(new BorderLayout(0, 0));
        panel1.add(panel4, BorderLayout.CENTER);
        final JScrollPane scrollPane1 = new JScrollPane();
        panel4.add(scrollPane1, BorderLayout.CENTER);
        mathRepresentationView = new JTextArea();
        scrollPane1.setViewportView(mathRepresentationView);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel5, BorderLayout.SOUTH);
        copyButton = new JButton();
        copyButton.setText("Copy");
        panel5.add(copyButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
