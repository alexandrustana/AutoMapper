package com.convert.exceptions;

public class UnmappedType extends RuntimeException{

	private static final long serialVersionUID = 593299258126223292L;
	
	public UnmappedType() {
		super("You are trying to convert a type which type was not added");
	}
}	
