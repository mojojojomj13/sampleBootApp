package com.techo.app.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This class is a DTO that Represnets the Response Object to be returned to the
 * caller.
 * 
 * @author Prithvish Mukherjee
 *
 */
@JsonInclude(Include.NON_NULL)
public class SatisfactionDTO {

	private Long timeLeft = 0L;

	private String details = "";

	private Long maxSatisfaction = 0L;

	public Long getTimeLeft() {
		return timeLeft;
	}

	public void setTimeLeft(Long timeLeft) {
		this.timeLeft = timeLeft;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Long getMaxSatisfaction() {
		return maxSatisfaction;
	}

	public void setMaxSatisfaction(Long maxSatisfaction) {
		this.maxSatisfaction = maxSatisfaction;
	}

	@Override
	public String toString() {
		return "{timeLeft:" + timeLeft + ", details:" + details + ", maxSatisfaction:" + maxSatisfaction + "}";
	}

}
