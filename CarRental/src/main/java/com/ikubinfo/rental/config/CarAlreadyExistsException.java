package com.ikubinfo.rental.config;

public class CarAlreadyExistsException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CarAlreadyExistsException(String errorMessage) {
		super(errorMessage);
	}
}
