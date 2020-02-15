package com.ikubinfo.rental.config;

public class CategoryAlreadyExistsException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CategoryAlreadyExistsException(String errMessage) {
		super(errMessage);
	}
}
