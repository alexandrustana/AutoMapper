package annotations.ignore;

import annotations.ignore.dao.ADao;
import annotations.ignore.model.AModel;
import com.convert.Converter;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author Alexandru Stana, alexandru.stana@busymachines.com
 * @since 15/07/2018
 */
public class IgnoreTest {
    private static Converter converter;

    @BeforeClass
    public static void setup() {
        converter = Converter.instance();
        converter.addMapping(AModel.class, ADao.class);
    }

    @Test
    public void testIgnore() {
        AModel model = new AModel();
        model.setX(1);
        model.setY(2);

        ADao dao = converter.map(model);

        assertEquals(model.getY(), dao.getY());
        assertNotEquals(model.getX(), dao.getX());
    }
}
