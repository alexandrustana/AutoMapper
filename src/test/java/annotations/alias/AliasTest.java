package annotations.alias;

import annotations.alias.dao.ADao;
import annotations.alias.model.AModel;
import com.convert.Mapper;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Alexandru Stana, alexandru.stana@busymachines.com
 * @since 15/07/2018
 */
public class AliasTest {
    private static Mapper converter;

    @BeforeClass
    public static void setup() {
        converter = Mapper.instance();
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
