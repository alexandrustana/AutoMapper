import autoMapper.AutoMapper;
import autoMapper.Mapper;
import autoMapper.TypeMap;

public class Client {

	public static void main(String[] args) {
		AutoMapper mapper = AutoMapper.getInstance();
		mapper.addMapping(A.class, C.class);
		
		A a = new A();
		a.setX(3);
		a.setY(10);
		
		C c = new C();
		c.setX(2);
		c.setY(1);
		
		c = mapper.map(a);
		
		System.out.println(c.getX());
		System.out.println(c.getY());
	}

}



class B extends A {
}

