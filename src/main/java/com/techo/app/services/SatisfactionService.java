package com.techo.app.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.techo.app.exceptions.ServiceException;

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

	public void setDataFile(String dataFile) {
		this.dataFile = dataFile;
	}

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

	private long[] timeArray;

	private long[] satisfactionArray;

	private long[][] timeSatisfactionArray;

	private int totalTime = 0;

	private int noOfItems = 0;

	/**
	 * This method gets the Maximum Satisfaction value out of a List of Items
	 * ({@link #noOfItems}) which has to be consumed in {@link #totalTime} to
	 * get maximum Satisfaction value
	 * 
	 * @return the maximum Satisfaction value
	 * @throws ServiceException
	 *             may throw {@link ServiceException} in case any service layer
	 */
	public long getMaxSatisfaction() throws ServiceException {
		long result = 0L;
		long startTime = System.currentTimeMillis();
		result = getMaxSatisfaction(noOfItems, totalTime);
		long endTime = System.currentTimeMillis();
		long timeInSecs = (endTime - startTime);
		LOGGER.info("result  :: " + result + " in " + timeInSecs + " milli seconds");
		return result;
	}

	/**
	 * This method is an init method that reads the File from the classpath and
	 * initializes the arrays based on the contents of the file. The values it
	 * initializes are {@link #totalTime}, {@link #noOfItems}
	 * ,{@link #timeArray}, {@link #satisfactionArray} and
	 * {@link #timeSatisfactionArray}
	 * 
	 * @throws ServiceException
	 *             may throw {@link ServiceException} in case the file is not
	 *             available
	 */
	@PostConstruct
	public void initializeArrays() throws ServiceException {
		BufferedReader br = null;
		try {
			Resource resource = getResource();
			br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
			String s = "";
			s = br.readLine();
			totalTime = Integer.valueOf(s.split(" ")[0]);
			noOfItems = Integer.valueOf(s.split(" ")[1]);
			int n = noOfItems;
			timeArray = new long[n + 1];
			satisfactionArray = new long[n + 1];
			// initialize the values in the arrays
			while ((s = br.readLine()) != null) {
				satisfactionArray[n] = Long.valueOf(s.split(" ")[0]);
				timeArray[n] = Long.valueOf(s.split(" ")[1]);
				n--;
			}
			timeSatisfactionArray = new long[noOfItems + 1][totalTime + 1];
			// initialize all to -1L
			for (long[] arr : timeSatisfactionArray)
				Arrays.fill(arr, -1L);
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
	}

	/**
	 * This is Knapsack method Implementation using recursive algorithm
	 * 
	 * @param noOfItems
	 *            the number of Items
	 * @param totalTime
	 *            the total Time in which to consume/eat the Max Items
	 * @return the Max Satisfaction value for consuming the Items in
	 *         {@link #totalTime} with the given {@link #noOfItems}
	 */
	private Long getMaxSatisfaction(int noOfItems, int totalTime) {
		if (timeSatisfactionArray[noOfItems][totalTime] != -1)
			return timeSatisfactionArray[noOfItems][totalTime];
		Long result = 0L;
		if (noOfItems == 0 || totalTime == 0)
			result = 0L;
		else if (timeAt(noOfItems) > totalTime)
			result = getMaxSatisfaction(noOfItems - 1, totalTime);
		else {
			long val1 = getMaxSatisfaction(noOfItems - 1, totalTime);
			long val2 = valueAt(noOfItems) + getMaxSatisfaction(noOfItems - 1, (int) (totalTime - timeAt(noOfItems)));
			result = Math.max(val1, val2);
		}
		timeSatisfactionArray[noOfItems][totalTime] = result;
		return result;
	}

	/**
	 * This method gets the Time a particular item takes at that Index
	 * 
	 * @param index
	 *            the index Item whose time is to be retrieved
	 * @return the Time taken by this item to be completed
	 */
	private long timeAt(int index) {
		if (timeArray != null)
			return timeArray[index];
		return 0L;
	}

	/**
	 * This method gets the Satisfaction value a particular item takes at that
	 * Index
	 * 
	 * @param index
	 *            the index Item whose satisfaction value is to be retrieved
	 * @return the Time Satisfaction value given by this item to be completed
	 */
	private long valueAt(int index) {
		if (satisfactionArray != null)
			return satisfactionArray[index];
		return 0L;
	}
}
