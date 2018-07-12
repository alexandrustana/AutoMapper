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

import static org.junit.Assert.*;

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
    }

    @Test
    public void convertAModelToADaoIgnoreB() {
        AModel model = new AModel();
        model.setX(1);
        model.setY(2);

        ADao dao = converter.map(model);

        assertEquals(model.getX(), dao.getX());
        assertEquals(model.getY(), dao.getY());
    }

    @Test
    public void convertAModelToADaoWithB() {
        BModel b = new BModel();
        b.setZ(3);

        AModel model = new AModel();
        model.setX(1);
        model.setY(2);
        model.setB(b);

        ADao dao = converter.map(model);

        assertEquals(model.getX(), dao.getX());
        assertEquals(model.getY(), dao.getY());
        assertEquals(model.getB().getZ(), dao.getB().getZ());
    }

    @Test
    public void convertInfiniteRecursion() {
        AModel model = new AModel();
        model.setX(0);

        BModel b = new BModel();

        b.setA(model);
        model.setB(b);

        ADao dao = converter.map(model);

        assertEquals(model.getX(), dao.getX());
        assertEquals(model.getB().getA().getB().getA().getX(), dao.getB().getA().getB().getA().getX());
    }

    @Test
    public void convertAModelToADao() {
        DModel d = new DModel();
        d.setA(6);

        AModel a = new AModel();
        a.setY(4);
        a.setX(5);

        BModel b = new BModel();
        b.setZ(3);
        b.setA(a);
        b.setD(d);

        AModel model = new AModel();
        model.setX(1);
        model.setY(2);
        model.setB(b);

        ADao dao = converter.map(model);

        assertEquals(model.getX(), dao.getX());
        assertEquals(model.getY(), dao.getY());
        assertEquals(model.getB().getZ(), dao.getB().getZ());
        assertEquals(model.getB().getA().getX(), dao.getB().getA().getX());
        assertEquals(model.getB().getA().getY(), dao.getB().getA().getY());
        assertEquals(model.getB().getD().getA(), dao.getB().getD().getA());

        assertNull(dao.getB().getA().getB());
    }

    @Test
    public void convertCModelToCDao() {
        CModel model = new CModel();
        model.setX(1);
        model.setY(2);

        CDao dao = converter.map(model);

        assertEquals(model.getX(), dao.getX());
        assertEquals(model.getY(), dao.getY());
    }

    @Test
    public void convertCModelToCDaoRecursive() {
        CModel c = new CModel();
        c.setY(3);
        c.setX(4);

        CModel model = new CModel();
        model.setX(1);
        model.setY(2);
        model.setC(c);

        CDao dao = converter.map(model);

        assertEquals(model.getX(), dao.getX());
        assertEquals(model.getY(), dao.getY());
        assertEquals(model.getC().getX(), dao.getC().getX());
        assertEquals(model.getC().getY(), dao.getC().getY());
    }
}
