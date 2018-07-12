package dao;

/**
 * @author Alexandru Stana, alexandru.stana@busymachines.com
 * @since 12/07/2018
 */
public class BDao {
    private int z;
    private DDao d;
    private ADao a;

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public DDao getD() {
        return d;
    }

    public void setD(DDao d) {
        this.d = d;
    }

    public ADao getA() {
        return a;
    }

    public void setA(ADao a) {
        this.a = a;
    }

    @Override
    public String toString() {
        return "BDao{" +
                "z=" + z +
                ", d=" + d +
                ", a=" + a +
                '}';
    }
}
