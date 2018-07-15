package annotations.alias;

import annotations.alias.dao.ADao;
import annotations.alias.model.AModel;
import com.convert.Converter;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author Alexandru Stana, alexandru.stana@busymachines.com
 * @since 15/07/2018
 */
public class AliasTest {
    private static Converter converter;

    @BeforeClass
    public static void setup() {
        converter = Converter.instance();
        converter.addMapping(AModel.class, ADao.class);
    }

    @Test
    public void testAlias() {
        AModel model = new AModel();
        model.setX(1);

        ADao dao = converter.map(model);

        assertEquals(model.getX(), dao.getY());
    }
}
