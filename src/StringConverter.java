/**
 * Created by Lobster on 15.03.18.
 */
public class StringConverter {

    public static String toString(int[][] matrix) {
        StringBuilder matrixStr = new StringBuilder("[");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length - 1; j++) {
                matrixStr.append(matrix[i][j]).append(",");
            }

            matrixStr.append(matrix[i][matrix[i].length - 1]);
            if (i != matrix.length - 1) {
                matrixStr.append(";");
            }
        }

        matrixStr.append("]");
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

}
