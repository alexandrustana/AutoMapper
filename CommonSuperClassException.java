package Utilities;

public class CommonSuperClassException extends RuntimeException{
	public CommonSuperClassException() {
		super("No common super class was found for the two classes.");
	}
}
