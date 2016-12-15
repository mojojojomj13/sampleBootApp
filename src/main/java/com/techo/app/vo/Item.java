package com.techo.app.vo;

import com.techo.app.services.SatisfactionService;

/**
 * This is the Item class that Represents one row in the data file.
 * 
 * @see SatisfactionService#getResource() for more details
 *
 * @author Prithvish Mukherjee
 *
 */
public class Item implements Comparable<Item> {

	private Long time;
	private Long satisfaction;
	private Double satisfactionPerMin;

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Double getSatisfactionPerMin() {
		return satisfactionPerMin;
	}

	public void setSatisfactionPerMin(Double satisfactionPerMin) {
		this.satisfactionPerMin = satisfactionPerMin;
	}

	public Item() {
	}

	public Item(Long time, Long satisfaction, Double satisfactionPerMin) {
		this.time = time;
		this.satisfaction = satisfaction;
		this.satisfactionPerMin = satisfactionPerMin;
	}

	public Long getSatisfaction() {
		return satisfaction;
	}

	public void setSatisfaction(Long satisfaction) {
		this.satisfaction = satisfaction;
	}

	@Override
	public int compareTo(Item other) {
		return other.satisfactionPerMin.compareTo(this.satisfactionPerMin);
	}

}
