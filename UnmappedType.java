package autoMapper.exceptions;

public class UnmappedType extends RuntimeException{

	private static final long serialVersionUID = 593299258126223292L;
	
	public<F,T> UnmappedType() {
		super("You are trying to map a type which type was not added");
	}
}	
