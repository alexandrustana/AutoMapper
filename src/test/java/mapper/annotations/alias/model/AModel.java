package mapper.annotations.alias.model;

import com.convert.annotations.Alias;

/**
 * @author Alexandru Stana, alexandru.stana@busymachines.com
 * @since 15/07/2018
 */
public class AModel {
    @Alias(name = "y")
    private int x;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
}
