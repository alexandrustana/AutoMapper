import autoMapper.TypeMap;


public class Client {

	public static void main(String[] args) {
		TypeMap tp = TypeMap.getInstance();
		tp.addType(B.class, A.class, false);
	}

}

class A{
	public Object get() {
		return this;
	}
	public Integer getInt() {
		return new Integer(0);
	}
}
class B extends A{}

