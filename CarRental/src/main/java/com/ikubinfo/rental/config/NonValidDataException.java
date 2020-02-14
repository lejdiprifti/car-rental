package com.ikubinfo.rental.config;

public class NonValidDataException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NonValidDataException(String errMessage) {
		super(errMessage);
	}
}
