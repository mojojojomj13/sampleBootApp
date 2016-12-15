package sampleBoot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.http.HttpStatus;

import com.google.gson.JsonObject;
import com.techo.app.exceptions.ServiceException;
import com.techo.app.services.SatisfactionService;
import com.techo.app.vo.Item;

@RunWith(MockitoJUnitRunner.class)
public class SatisfactionServiceTest {

	private static final long MAX_SATISFACTION_VALUE = 10000L;

	private static final String MAX_SATISFACTION_KEY = "maxSatisfaction";

	@Mock
	private SatisfactionService service;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws ServiceException {
		Mockito.when(service.getTotalTime()).then(invoc -> {
			return 10000L;
		});
		Mockito.doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				List<Item> list = (List<Item>) invocation.getArguments()[0];
				Item item = new Item(1000L, 1000L, 1.00000);
				Item item2 = new Item(1001L, 2000L, 0.50000);
				list.add(item);
				list.add(item2);
				return null;
			}
		}).when(service).createItemList(Mockito.anyList());
		Mockito.when(service.processItemList(Mockito.anyList(), Mockito.anyLong())).thenCallRealMethod();
		Mockito.when(service.getMaximumSatisfaction()).thenCallRealMethod().thenThrow(
				new ServiceException("Some Error", new Exception("Some Error"), HttpStatus.INTERNAL_SERVER_ERROR));

	}

	@Test
	public void testGetMaximumSatisfaction() {
		try {
			JsonObject obj = service.getMaximumSatisfaction();
			assertNotNull(obj);
			assertTrue(obj.has(MAX_SATISFACTION_KEY));
			assertEquals(MAX_SATISFACTION_VALUE, obj.get(MAX_SATISFACTION_KEY).getAsLong());
		} catch (ServiceException e) {
		}
	}

	@Test
	public void testGetMaximumSatisfactionException() {
		try {
			service.getMaximumSatisfaction();
			service.getMaximumSatisfaction();
		} catch (Exception e) {
			assertEquals(e.getClass(), ServiceException.class);
		}

	}

	@After
	public void tearDown() {
		Mockito.reset(service);
	}

}
