package sampleBoot;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ SatisfactionServiceTest.class })
public class SuiteTest {

	@Test
	public void temp() {
		assertEquals("A", "A");
	}

}
