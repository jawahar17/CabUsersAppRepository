package com.example.demo.exception.handler;

import java.net.MalformedURLException;
import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	private ErrorDetails logException(Exception ex, WebRequest request) {
		//log here
		return new ErrorDetails(LocalDateTime.now(),ex.getMessage(),request.getDescription(false));
	}
	
	@ExceptionHandler(value = MalformedURLException.class)
	public ErrorDetails handleMalformedUrlEx(Exception ex, WebRequest request) {
		return logException(ex, request);
	}
	
	@ExceptionHandler(value = InterruptedException.class)
	public ErrorDetails handleInterruptedEx(Exception ex, WebRequest request) {
		return logException(ex, request);
	}
	
	@ExceptionHandler(value = Exception.class)
	public ErrorDetails exceptionHandling(Exception ex, WebRequest request) {
		return logException(ex, request);
	}
}

