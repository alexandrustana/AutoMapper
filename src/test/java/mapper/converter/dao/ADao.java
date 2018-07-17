package mapper.converter.dao;

/**
 * @author Alexandru Stana, alexandru.stana@busymachines.com
 * @since 12/07/2018
 */
public class ADao {
    private int  x;
    private int  y;
    private BDao b;

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

    public BDao getB() {
        return b;
    }

    public void setB(BDao b) {
        this.b = b;
    }

    @Override
    public String toString() {
        return "ADao{" +
                "x=" + x +
                ", y=" + y +
                ", b=" + b +
                '}';
    }
}
