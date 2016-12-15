package com.techo.app.exceptions;

import org.springframework.http.HttpStatus;

/**
 * This is a User defined Exception for creating custom Service Exceptions in
 * the Service Layer
 * 
 * @author Prithvish Mukherjee
 *
 */
public class ServiceException extends Exception {

	private static final long serialVersionUID = -8152482082071853701L;

	private String message;

	private Throwable throwable;

	private HttpStatus status = HttpStatus.OK; // default

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public ServiceException(String message, Throwable throwable, HttpStatus status) {
		super(message, throwable);
		this.message = message;
		this.throwable = throwable;
		this.status = status;
	}

	public ServiceException() {
	}

	public ServiceException(String message, Throwable throwable) {
		super(message, throwable);
		this.message = message;
		this.throwable = throwable;
	}

}
