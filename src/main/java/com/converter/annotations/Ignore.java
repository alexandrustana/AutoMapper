package com.converter.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate the field of the class you wish to ignore
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)  
public @interface Ignore {
	
}