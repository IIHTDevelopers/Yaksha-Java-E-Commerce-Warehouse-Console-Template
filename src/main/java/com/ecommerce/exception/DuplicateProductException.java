package com.ecommerce.exception;

public class DuplicateProductException extends RuntimeException {
	public DuplicateProductException() {
		super();
	}

	public DuplicateProductException(String message) {
		super(message);
	}
}
