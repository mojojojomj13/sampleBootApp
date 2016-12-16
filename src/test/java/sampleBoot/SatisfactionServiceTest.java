package sampleBoot;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.techo.app.Application;
import com.techo.app.exceptions.ServiceException;
import com.techo.app.services.SatisfactionService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@IntegrationTest("server.port:8090")
public class SatisfactionServiceTest {

	@Autowired
	private SatisfactionService service;

	@Test
	public void testGetMaxSatisfaction() {
		try {
			long max = service.getMaxSatisfaction();
			assertTrue(max >= 0);
			assertEquals(2493893L, max);
		} catch (ServiceException e) {
			assertEquals(ServiceException.class, e.getClass());
		}
	}

	@Test
	public void testGetMaxSatisfactionNegative() {
		service.setDataFile("abcd.txt");
		try {
			service.initializeArrays();
			service.getMaxSatisfaction();
		} catch (ServiceException e) {
			assertEquals(ServiceException.class, e.getClass());
		}
	}

	@Test
	public void testNullDataFile() {
		service.setDataFile(null);
		try {
			service.initializeArrays();
		} catch (ServiceException e) {
			assertEquals(ServiceException.class, e.getClass());
		}
	}

}
