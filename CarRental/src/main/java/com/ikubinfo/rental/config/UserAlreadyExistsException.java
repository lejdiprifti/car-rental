package com.ikubinfo.rental.config;

public class UserAlreadyExistsException extends Exception {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserAlreadyExistsException(String errMessage) {
		super(errMessage);
	}
}
