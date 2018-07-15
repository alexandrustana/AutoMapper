package model;

/**
 * @author Alexandru Stana, alexandru.stana@busymachines.com
 * @since 15/07/2018
 */
public class EModel extends FModel {
    private String y;
    private DModel d;

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public DModel getD() {
        return d;
    }

    public void setD(DModel d) {
        this.d = d;
    }
}
