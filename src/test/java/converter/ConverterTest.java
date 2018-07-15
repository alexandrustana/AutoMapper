package converter;

import com.convert.Converter;
import converter.dao.*;
import converter.model.*;
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
        converter.addMapping(EModel.class, EDao.class);
        converter.addMapping(FModel.class, FDao.class);
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

    @Test
    public void convertEModelToEDaoWithoutObject() {
        EModel model = new EModel();
        model.setX(1);
        model.setY("test");

        EDao dao = converter.map(model);

        assertEquals(model.getX(), dao.getX());
        assertEquals(model.getY(), dao.getY());
    }

    @Test
    public void convertEModelToEDaoWithObject() {
        DModel d = new DModel();
        d.setA(2);

        BModel b = new BModel();
        b.setZ(5);

        AModel a = new AModel();
        a.setX(3);
        a.setY(4);
        a.setB(b);

        EModel model = new EModel();
        model.setX(1);
        model.setY("test");
        model.setD(d);
        model.setA(a);

        EDao dao = converter.map(model);

        assertEquals(model.getX(), dao.getX());
        assertEquals(model.getY(), dao.getY());
        assertEquals(model.getD().getA(), dao.getD().getA());
        assertEquals(model.getA().getX(), dao.getA().getX());
        assertEquals(model.getA().getY(), dao.getA().getY());
        assertEquals(model.getA().getB().getZ(), dao.getA().getB().getZ());
    }

    @Test
    public void convertEModelToEDaoUsingSuperReferences() {
        FModel model = new EModel();
        model.setX(1);
        ((EModel) model).setY("test");

        FDao dao = converter.map(model);

        assertEquals(model.getX(), dao.getX());
        assertEquals(((EModel) model).getY(), ((EDao) dao).getY());
    }
}
