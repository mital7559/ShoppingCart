package com.yash.shop.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.yash.shop.exception.CartNotFoundException;

@ControllerAdvice
public class RestErrorHandler {

	
	@ExceptionHandler(CartNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleCartNotFoundException(CartNotFoundException ex) {
		System.out.println("In Exception");
	}
	
}
