package com.techo.app.helper;

import org.springframework.http.HttpStatus;

import com.google.gson.JsonObject;
import com.techo.app.constants.ResponseConstants;

/**
 * This is a Utility class that has some Helper methods for creating some
 * default Json Response Objects.
 * 
 * @author Prithvish Mukherjee
 *
 */
public class ResponseUtil {

	/**
	 * This method gives you a default vanilla Response with success, status
	 * code 200 OK in its Json Format.
	 * 
	 * @return The {@link JsonObject} that represents the response
	 */
	public static JsonObject getDefaultJsonResponse() {
		JsonObject obj = new JsonObject();
		obj.addProperty(ResponseConstants.STATUS, ResponseConstants.SUCCESS);
		obj.addProperty(ResponseConstants.SATISFACTION, ResponseConstants.DEFAULT_SATISFACTION);
		obj.addProperty(ResponseConstants.CODE, HttpStatus.OK.value());
		return obj;
	}

	/**
	 * 
	 * This method gives you a default Error Response with Fail, status code 501
	 * , Service Unavailable. in its Json Format.
	 * 
	 * @return The {@link JsonObject} that represents the error response
	 */
	public static JsonObject getDefaultErrorJsonResponse() {
		JsonObject obj = new JsonObject();
		obj.addProperty(ResponseConstants.STATUS, ResponseConstants.FAIL);
		obj.addProperty(ResponseConstants.CODE, HttpStatus.SERVICE_UNAVAILABLE.value());
		return obj;
	}
}
