import autoMapper.annotations.Ignore;

public class A {
	public B getB() {
		return b;
	}

	public void setB(B b) {
		this.b = b;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void draw() {
	}

	@Ignore
	private int x;

	private int y;
	
	private B b;
}