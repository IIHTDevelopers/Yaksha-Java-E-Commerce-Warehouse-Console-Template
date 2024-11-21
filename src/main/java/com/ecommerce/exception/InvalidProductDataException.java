package com.ecommerce.exception;

public class InvalidProductDataException extends RuntimeException {
	public InvalidProductDataException() {
		super();
	}

	public InvalidProductDataException(String message) {
		super(message);
	}
}
