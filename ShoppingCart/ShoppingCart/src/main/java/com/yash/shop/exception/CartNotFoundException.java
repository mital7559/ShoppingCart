package com.yash.shop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class CartNotFoundException extends Exception{
	
	private static final long serialVersionUID = -2581975292273282583L;
	 

	public CartNotFoundException(String errorMessage) {
        super(errorMessage);
    }
	
	
}
