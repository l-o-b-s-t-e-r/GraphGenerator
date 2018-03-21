package model;

/**
 * Created by Lobster on 21.03.18.
 */
public class MathRepresentation {

    private int f[];
    private int A[][];
    private int b[];
    private int beq[];
    private int Aeq[][];

    public MathRepresentation(int[] f, int[][] a, int[] b, int[] beq, int[][] aeq) {
        this.f = f;
        A = a;
        this.b = b;
        this.beq = beq;
        Aeq = aeq;
    }

    public int[] getF() {
        return f;
    }

    public void setF(int[] f) {
        this.f = f;
    }

    public int[][] getA() {
        return A;
    }

    public void setA(int[][] a) {
        A = a;
    }

    public int[] getB() {
        return b;
    }

    public void setB(int[] b) {
        this.b = b;
    }

    public int[] getBeq() {
        return beq;
    }

    public void setBeq(int[] beq) {
        this.beq = beq;
    }

    public int[][] getAeq() {
        return Aeq;
    }

    public void setAeq(int[][] aeq) {
        Aeq = aeq;
    }
}
