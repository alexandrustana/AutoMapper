package factory.dao;

/**
 * @author Alexandru Stana, alexandru.stana@busymachines.com
 * @since 15/07/2018
 */
public class EDao extends FDao {
    private String y;
    private DDao   d;

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public DDao getD() {
        return d;
    }

    public void setD(DDao d) {
        this.d = d;
    }
}
