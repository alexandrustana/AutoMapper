package factory;

import com.clone.Factory;
import factory.model.*;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Alexandru Stana, alexandru.stana@busymachines.com
 * @since 17/07/2018
 */
public class FactoryTest {
    private static Factory factory;

    @BeforeClass
    public static void setup() {
        factory = Factory.instance();
    }

    @Test
    public void copyDModel() {
        DModel model = new DModel();
        model.setA(1);

        DModel copy = factory.copy(model);

        assertEquals(model.getA(), copy.getA());
    }

    @Test
    public void copyAModelIgnoreB() {
        AModel model = new AModel();
        model.setX(1);
        model.setY(2);

        AModel copy = factory.copy(model);

        assertEquals(model.getX(), copy.getX());
        assertEquals(model.getY(), copy.getY());
    }

    @Test
    public void copyAModelWithB() {
        BModel b = new BModel();
        b.setZ(3);

        AModel model = new AModel();
        model.setX(1);
        model.setY(2);
        model.setB(b);

        AModel copy = factory.copy(model);

        assertEquals(model.getX(), copy.getX());
        assertEquals(model.getY(), copy.getY());
        assertEquals(model.getB().getZ(), copy.getB().getZ());
    }

    @Test
    public void copyInfiniteRecursion() {
        AModel model = new AModel();
        model.setX(0);

        BModel b = new BModel();

        b.setA(model);
        model.setB(b);

        AModel copy = factory.copy(model);

        assertEquals(model.getX(), copy.getX());
        assertEquals(model.getB().getA().getB().getA().getX(), copy.getB().getA().getB().getA().getX());
    }

    @Test
    public void copyAModel() {
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

        AModel copy = factory.copy(model);

        assertEquals(model.getX(), copy.getX());
        assertEquals(model.getY(), copy.getY());
        assertEquals(model.getB().getZ(), copy.getB().getZ());
        assertEquals(model.getB().getA().getX(), copy.getB().getA().getX());
        assertEquals(model.getB().getA().getY(), copy.getB().getA().getY());
        assertEquals(model.getB().getD().getA(), copy.getB().getD().getA());

        assertNull(copy.getB().getA().getB());
    }

    @Test
    public void copyCModel() {
        CModel model = new CModel();
        model.setX(1);
        model.setY(2);

        CModel copy = factory.copy(model);

        assertEquals(model.getX(), copy.getX());
        assertEquals(model.getY(), copy.getY());
    }

    @Test
    public void copyCModelRecursive() {
        CModel c = new CModel();
        c.setY(3);
        c.setX(4);

        CModel model = new CModel();
        model.setX(1);
        model.setY(2);
        model.setC(c);

        CModel copy = factory.copy(model);

        assertEquals(model.getX(), copy.getX());
        assertEquals(model.getY(), copy.getY());
        assertEquals(model.getC().getX(), copy.getC().getX());
        assertEquals(model.getC().getY(), copy.getC().getY());
    }

    @Test
    public void copyEModelWithoutObject() {
        EModel model = new EModel();
        model.setX(1);
        model.setY("test");

        EModel copy = factory.copy(model);

        assertEquals(model.getX(), copy.getX());
        assertEquals(model.getY(), copy.getY());
    }

    @Test
    public void copyEModelWithObject() {
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

        EModel copy = factory.copy(model);

        assertEquals(model.getX(), copy.getX());
        assertEquals(model.getY(), copy.getY());
        assertEquals(model.getD().getA(), copy.getD().getA());
        assertEquals(model.getA().getX(), copy.getA().getX());
        assertEquals(model.getA().getY(), copy.getA().getY());
        assertEquals(model.getA().getB().getZ(), copy.getA().getB().getZ());
    }

    @Test
    public void copyEModelUsingSuperReferences() {
        FModel model = new EModel();
        model.setX(1);
        ((EModel) model).setY("test");

        FModel copy = factory.copy(model);

        assertEquals(model.getX(), copy.getX());
        assertEquals(((EModel) model).getY(), ((EModel) copy).getY());
    }
}
