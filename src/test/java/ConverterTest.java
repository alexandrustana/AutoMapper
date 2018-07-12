import com.converter.Converter;
import dao.ADao;
import dao.BDao;
import dao.CDao;
import dao.DDao;
import model.AModel;
import model.BModel;
import model.CModel;
import model.DModel;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author Alexandru Stana, alexandru.stana@busymachines.com
 * @since 12/07/2018
 */
public class ConverterTest {
    private static Converter converter;

    @BeforeClass
    public static void setup() {
        converter = Converter.instance();
        converter.addMapping(AModel.class, ADao.class);
        converter.addMapping(BModel.class, BDao.class);
        converter.addMapping(CModel.class, CDao.class);
        converter.addMapping(DModel.class, DDao.class);
    }

    @Test
    public void convertDModelToDDao() {
        DModel model = new DModel();
        model.setA(1);

        DDao dao = converter.map(model);

        assertEquals(model.getA(), dao.getA());
        assertNotEquals(model, dao);
    }

    @Test
    public void convertAModelToADao() {
        AModel model = new AModel();
        model.setX(1);
        model.setY(2);

        ADao dao = converter.map(model);

        assertEquals(model.getX(), dao.getX());
        assertEquals(model.getY(), dao.getY());
        assertNotEquals(model, dao);
    }
}
