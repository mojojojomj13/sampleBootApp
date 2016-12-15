package com.techo.app.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.JsonObject;
import com.techo.app.constants.RequestMappingConstants;
import com.techo.app.constants.ResponseConstants;
import com.techo.app.exceptions.ServiceException;
import com.techo.app.helper.ResponseUtil;
import com.techo.app.services.SatisfactionService;

/**
 * 
 * @author Prithvish Mukherjee
 * 
 *         This is the main Controller that serves the Client Request
 */
@Controller
public class HomeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private SatisfactionService service;

	/**
	 * This method handles the Exception in case of any {@link ServiceException}
	 * from the service methods back to the controller
	 * 
	 * @param se
	 *            The {@link ServiceException} that may be thrown back from the
	 *            service layer
	 * @return A {@link ResponseEntity} object that wraps then Response (success
	 *         / failure)
	 */
	private ResponseEntity<?> handleException(ServiceException se) {
		JsonObject obj = ResponseUtil.getDefaultErrorJsonResponse();
		obj.addProperty(ResponseConstants.CODE, se.getStatus().value());
		return new ResponseEntity<String>(obj.toString(), se.getStatus());
	}

	/**
	 * This method gets the maximum Satisfaction data.
	 * 
	 * @return A {@link ResponseEntity} object that wraps a succesfull Json
	 *         response in case the service method is executed without any
	 *         errors, in case of errors it wraps the Error object in
	 *         {@link ResponseEntity}
	 */
	@RequestMapping(value = RequestMappingConstants.GETSCORE, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getMaxSatisfactionScore() {
		JsonObject obj = ResponseUtil.getDefaultJsonResponse();
		ResponseEntity<?> res = new ResponseEntity<String>(obj.toString(), HttpStatus.OK);
		try {
			JsonObject jsonObj = service.getMaximumSatisfaction();
			System.err.println(jsonObj.toString());
			obj.add(ResponseConstants.SATISFACTION, jsonObj);
			res = new ResponseEntity<String>(obj.toString(), HttpStatus.OK);
		} catch (ServiceException se) {
			LOGGER.error("Error in HomeController  :: getMaxSatisfactionScore( ) :" + se.toString());
			return handleException(se);
		}
		return res;
	}
}
