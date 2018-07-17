package mapper.converter;

import com.convert.Mapper;
import mapper.converter.dao.*;
import mapper.converter.model.*;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Alexandru Stana, alexandru.stana@busymachines.com
 * @since 12/07/2018
 */
public class MapperTest {
    private static Mapper mapper;

    @BeforeClass
    public static void setup() {
        mapper = Mapper.instance();
        mapper.addMapping(AModel.class, ADao.class);
        mapper.addMapping(BModel.class, BDao.class);
        mapper.addMapping(CModel.class, CDao.class);
        mapper.addMapping(DModel.class, DDao.class);
        mapper.addMapping(EModel.class, EDao.class);
        mapper.addMapping(FModel.class, FDao.class);
    }

    @Test
    public void mapDModelToDDao() {
        DModel model = new DModel();
        model.setA(1);

        DDao dao = mapper.map(model);

        assertEquals(model.getA(), dao.getA());
    }

    @Test
    public void mapAModelToADaoIgnoreB() {
        AModel model = new AModel();
        model.setX(1);
        model.setY(2);

        ADao dao = mapper.map(model);

        assertEquals(model.getX(), dao.getX());
        assertEquals(model.getY(), dao.getY());
    }

    @Test
    public void mapAModelToADaoWithB() {
        BModel b = new BModel();
        b.setZ(3);

        AModel model = new AModel();
        model.setX(1);
        model.setY(2);
        model.setB(b);

        ADao dao = mapper.map(model);

        assertEquals(model.getX(), dao.getX());
        assertEquals(model.getY(), dao.getY());
        assertEquals(model.getB().getZ(), dao.getB().getZ());
    }

    @Test
    public void mapInfiniteRecursion() {
        AModel model = new AModel();
        model.setX(0);

        BModel b = new BModel();

        b.setA(model);
        model.setB(b);

        ADao dao = mapper.map(model);

        assertEquals(model.getX(), dao.getX());
        assertEquals(model.getB().getA().getB().getA().getX(), dao.getB().getA().getB().getA().getX());
    }

    @Test
    public void mapAModelToADao() {
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

        ADao dao = mapper.map(model);

        assertEquals(model.getX(), dao.getX());
        assertEquals(model.getY(), dao.getY());
        assertEquals(model.getB().getZ(), dao.getB().getZ());
        assertEquals(model.getB().getA().getX(), dao.getB().getA().getX());
        assertEquals(model.getB().getA().getY(), dao.getB().getA().getY());
        assertEquals(model.getB().getD().getA(), dao.getB().getD().getA());

        assertNull(dao.getB().getA().getB());
    }

    @Test
    public void mapCModelToCDao() {
        CModel model = new CModel();
        model.setX(1);
        model.setY(2);

        CDao dao = mapper.map(model);

        assertEquals(model.getX(), dao.getX());
        assertEquals(model.getY(), dao.getY());
    }

    @Test
    public void mapCModelToCDaoRecursive() {
        CModel c = new CModel();
        c.setY(3);
        c.setX(4);

        CModel model = new CModel();
        model.setX(1);
        model.setY(2);
        model.setC(c);

        CDao dao = mapper.map(model);

        assertEquals(model.getX(), dao.getX());
        assertEquals(model.getY(), dao.getY());
        assertEquals(model.getC().getX(), dao.getC().getX());
        assertEquals(model.getC().getY(), dao.getC().getY());
    }

    @Test
    public void mapEModelToEDaoWithoutObject() {
        EModel model = new EModel();
        model.setX(1);
        model.setY("test");

        EDao dao = mapper.map(model);

        assertEquals(model.getX(), dao.getX());
        assertEquals(model.getY(), dao.getY());
    }

    @Test
    public void mapEModelToEDaoWithObject() {
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

        EDao dao = mapper.map(model);

        assertEquals(model.getX(), dao.getX());
        assertEquals(model.getY(), dao.getY());
        assertEquals(model.getD().getA(), dao.getD().getA());
        assertEquals(model.getA().getX(), dao.getA().getX());
        assertEquals(model.getA().getY(), dao.getA().getY());
        assertEquals(model.getA().getB().getZ(), dao.getA().getB().getZ());
    }

    @Test
    public void mapEModelToEDaoUsingSuperReferences() {
        FModel model = new EModel();
        model.setX(1);
        ((EModel) model).setY("test");

        FDao dao = mapper.map(model);

        assertEquals(model.getX(), dao.getX());
        assertEquals(((EModel) model).getY(), ((EDao) dao).getY());
    }
}
