package com.converter.exceptions;

public class UndefinedMethodException extends RuntimeException{
	private static final long serialVersionUID = -328458591059728071L;

	public UndefinedMethodException() {
		super("The getter/setter was not found.");
	}
}
