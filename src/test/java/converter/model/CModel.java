package converter.model;

/**
 * @author Alexandru Stana, alexandru.stana@busymachines.com
 * @since 12/07/2018
 */
public class CModel {
    private int    x;
    private int    y;
    private CModel c;

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

    public CModel getC() {
        return c;
    }

    public void setC(CModel c) {
        this.c = c;
    }

    @Override
    public String toString() {
        return "CModel{" +
                "x=" + x +
                ", y=" + y +
                ", c=" + c +
                '}';
    }
}
