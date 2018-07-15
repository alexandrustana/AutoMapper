package converter.dao;

/**
 * @author Alexandru Stana, alexandru.stana@busymachines.com
 * @since 12/07/2018
 */
public class CDao {
    private int  x;
    private int  y;
    private CDao c;

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

    public CDao getC() {
        return c;
    }

    public void setC(CDao c) {
        this.c = c;
    }

    @Override
    public String toString() {
        return "CDao{" +
                "x=" + x +
                ", y=" + y +
                ", c=" + c +
                '}';
    }
}
