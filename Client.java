import autoMapper.mapperInterface.AutoMapper;

public class Client {

	public static void main(String[] args) {
		AutoMapper mapper = AutoMapper.getInstance();
		mapper.addMapping(A.class, C.class, true, false);
		mapper.addMapping(B.class, B.class, false, false);
		mapper.addMapping(D.class, D.class, false, false);
		
		A a = new A();
		a.setX(3);
		a.setY(10);
		
		B b = new B();
		b.setA(a);
		b.setZ(20);
		
		D d = new D();
		d.setA(30);
		
		b.setD(d);
		a.setB(b);
		
		C c = mapper.map(a);
		
		System.out.println("NEW");
		System.out.println(c.getX());
		System.out.println(c.getY());
		System.out.println(c.getB().getZ());
		System.out.println(c.getB().getD().getA());
		
//		C c1 = new C();
//		c1.setX(11);
//		c1.setY(12);
//		
//		A a1 = mapper.map(c1);
		

//		System.out.println("NEW1");
//		System.out.println(a1.getX());
//		System.out.println(a1.getY());
	}

}


