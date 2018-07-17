package mapper.annotations.ignore.model;

import com.convert.annotations.Ignore;

/**
 * @author Alexandru Stana, alexandru.stana@busymachines.com
 * @since 15/07/2018
 */
public class AModel {
    @Ignore
    private int x;
    private int y;

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
}
