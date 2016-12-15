package com.techo.app.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.techo.app.controllers.HomeController;
import com.techo.app.exceptions.ServiceException;
import com.techo.app.vo.Item;
import com.techo.app.vo.SatisfactionDTO;

/**
 * This is the main Service class that provides service to the
 * {@link HomeController}
 * 
 * @author Prithvish Mukherjee
 *
 */
@Service(value = "satisfactionService")
public class SatisfactionService {
	private static final Logger LOGGER = LoggerFactory.getLogger(SatisfactionService.class);

	@Autowired
	private ResourceLoader resLoader;

	@Value("${item.file}")
	private String dataFile;

	/**
	 * This method returns the data file as a Resource to be used as an
	 * InputStream
	 * 
	 * @return The {@link Resource} that points to the data file in the
	 *         classpath
	 */
	private Resource getResource() throws ServiceException {
		if (null == dataFile)
			throw new ServiceException("Could not read the data File ", new Exception("Could not read the data file"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		Resource resource = resLoader.getResource("classpath:" + dataFile);
		return resource;
	}

	/**
	 * This method returns the {@link JsonObject} that represents the
	 * Satisfaction details like timeLeft, details and maxSatisfaction
	 * 
	 * @see SatisfactionDTO for more details
	 * @return The {@link JsonObject} that Represents the
	 *         {@link SatisfactionDTO} which includes the timeLeft, details and
	 *         maxSatisfaction
	 * @throws ServiceException
	 * 
	 *             this may throw {@link ServiceException} in case of any error
	 *             in the Service
	 */
	public JsonObject getMaximumSatisfaction() throws ServiceException {
		LOGGER.info("SatisfactionService :: getMaximumSatisfaction() : started");
		ArrayList<Item> dataList = new ArrayList<Item>();
		long totalTime = getTotalTime();
		createItemList(dataList);
		SatisfactionDTO satisfactionDTO = processItemList(dataList, totalTime);
		LOGGER.info("Max Satisfaction :: " + satisfactionDTO.getMaxSatisfaction());
		JsonObject obj = new JsonObject();
		try {
			String jsonStr = new ObjectMapper().writeValueAsString(satisfactionDTO);
			obj = (JsonObject) new JsonParser().parse(jsonStr);
		} catch (JsonProcessingException e) {
			throw new ServiceException("Some error while parsing Json ::" + e.toString(), e,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		LOGGER.info("SatisfactionService :: getMaximumSatisfaction() : ended");
		return obj;
	}

	/**
	 * This method reads the data file and gets the Total Time in the data file.
	 * 
	 * @return the time left obtained from the data file's first line
	 * @throws ServiceException
	 *             may throw {@link ServiceException} in case of any Service
	 *             layer error
	 */
	public long getTotalTime() throws ServiceException {
		LOGGER.info("SatisfactionService::getTotalTime( ): started");
		long totalTimeLeft = 0L;
		BufferedReader br = null;
		try {
			Resource resource = getResource();
			br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
			String s = "";
			s = br.readLine();
			totalTimeLeft = Integer.valueOf(s.split(" ")[0]);
		} catch (NumberFormatException | IOException e) {
			LOGGER.error("Some error occurred in App :: " + e.toString());
			throw new ServiceException("Some error occurred in App :: " + e.toString(), e,
					HttpStatus.SERVICE_UNAVAILABLE);
		} finally {
			if (null != br)
				try {
					br.close();
				} catch (IOException e) {
					LOGGER.error("Some error while closing the BufferedReader");
				}
		}
		LOGGER.info("SatisfactionService::getTotalTime( ): ended");
		return totalTimeLeft;
	}

	/**
	 * This method creates a List of {@link Item} from the data file. The
	 * reading starts from the second line onwards becasue the first line is for
	 * total Time and no Of Items
	 * 
	 * @param list
	 *            The created {@link List} of {@link Item} based on the data
	 *            file provided
	 * @throws ServiceException
	 *             may throw {@link ServiceException} in case of any Service
	 *             Layer error
	 */
	public void createItemList(List<Item> list) throws ServiceException {
		LOGGER.info("SatisfactionService::createItemList( ): started");
		BufferedReader br = null;
		try {
			Resource resource = getResource();
			br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
			String s = "";
			s = br.readLine();// skip the first Line
			while ((s = br.readLine()) != null) {
				double factor = Double.valueOf(s.split(" ")[0]) / Double.valueOf(s.split(" ")[1]);
				Item obj = new Item(Long.valueOf(s.split(" ")[1]), Long.valueOf(s.split(" ")[0]), factor);
				list.add(obj);
			}
			// sort according to the highest factor/rate
			Collections.sort(list);
		} catch (IOException e) {
			LOGGER.error("Some error occurred in App :: " + e.toString());
			throw new ServiceException("Some error occurred in App :: " + e.toString(), e,
					HttpStatus.SERVICE_UNAVAILABLE);
		} finally {
			if (null != br)
				try {
					br.close();
				} catch (IOException e) {
					LOGGER.error("Some error while closing the BufferedReader");
				}
		}
		LOGGER.info("SatisfactionService::createItemList( ): ended");
	}

	/**
	 * This method is called once the List is created from the data file. It
	 * processes the {@link List} of {@link Item} provided to create a
	 * {@link SatisfactionDTO} object that contains details like timeLeft,
	 * details and maxSatisfaction.
	 * 
	 * @param dataList
	 *            The {@link List} of {@link Item} that is to be processed to
	 *            create the {@link SatisfactionDTO}
	 * @param totalTime
	 *            The total Time which decided how many Items can be consumed
	 * @return A {@link SatisfactionDTO} that represents the result of
	 *         processing the List. It contains data like timeLeft, details and
	 *         maxSatisfaction.
	 */
	public SatisfactionDTO processItemList(List<Item> dataList, long totalTime) {
		LOGGER.info("SatisfactionService::processItemList( ): started");
		SatisfactionDTO satisfactionDTO = new SatisfactionDTO();
		long timeLeft = totalTime;
		long maxSatisfaction = 0L;
		for (Item item : dataList) {
			if (timeLeft <= 0) {
				LOGGER.info("TIME OVER...");
				break;
			}
			if (timeLeft >= item.getTime()) {
				long noOfPlates = timeLeft / item.getTime();
				maxSatisfaction += item.getSatisfaction() * noOfPlates;
				if (noOfPlates >= 1) {
					LOGGER.info("Gordon Ramsey had " + noOfPlates + " plates of " + item.getSatisfaction()
							+ " , which added " + item.getSatisfaction() * noOfPlates + " amount to the Satisfaction");
					satisfactionDTO.setDetails(satisfactionDTO.getDetails() + "; Gordon Ramsey had " + noOfPlates
							+ " plates of " + item.getSatisfaction() + " , which added "
							+ item.getSatisfaction() * noOfPlates + " amount to the Satisfaction");
					timeLeft -= (item.getTime() * noOfPlates);
				}
			}
		}
		satisfactionDTO.setMaxSatisfaction(maxSatisfaction);
		satisfactionDTO.setTimeLeft(timeLeft);
		LOGGER.info("SatisfactionDTO :: " + satisfactionDTO.toString());
		LOGGER.info("SatisfactionService::processItemList( ): ended");
		return satisfactionDTO;
	}
}
