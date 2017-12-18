package com.yash.shop.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.yash.shop.exception.CartNotFoundException;

@ControllerAdvice
public class RestErrorHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestErrorHandler.class);
	
	@ExceptionHandler(CartNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleCartNotFoundException(CartNotFoundException ex) {
        LOGGER.debug("handling 404 error on a cart entry");
    }
	
}
