package mapper.converter.model;

/**
 * @author Alexandru Stana, alexandru.stana@busymachines.com
 * @since 12/07/2018
 */
public class BModel {
    private int    z;
    private DModel d;
    private AModel a;

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public DModel getD() {
        return d;
    }

    public void setD(DModel d) {
        this.d = d;
    }

    public AModel getA() {
        return a;
    }

    public void setA(AModel a) {
        this.a = a;
    }

    @Override
    public String toString() {
        return "BModel{" +
                "z=" + z +
                ", d=" + d +
                ", a=" + a +
                '}';
    }
}
