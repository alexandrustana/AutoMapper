package autoMapper.exceptions;

public class CommonSuperClassException extends RuntimeException{
	private static final long serialVersionUID = -3748202949012539532L;

	public CommonSuperClassException() {
		super("No common super class was found for the two classes.");
	}
}
