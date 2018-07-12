package model;

/**
 * @author Alexandru Stana, alexandru.stana@busymachines.com
 * @since 12/07/2018
 */
public class CModel {
    private int    x;
    private int    y;
    private BModel b;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public BModel getB() {
        return b;
    }

    public void setB(BModel b) {
        this.b = b;
    }

    @Override
    public String toString() {
        return "CModel{" +
                "x=" + x +
                ", y=" + y +
                ", b=" + b +
                '}';
    }
}
