package it.ice.nttz.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import it.ice.nttz.persistence.PropertyReader;

import org.junit.Before;
import org.junit.Test;

public class PropertyReaderTest {
	private PropertyReader propertyReader;

	@Before
	public void setUp() {
		propertyReader = new PropertyReader("relations.properties");
	}

	@Test
	public void testGetProp() {
		String prop = propertyReader.getProp("containing");
		assertNotNull(prop);
		assertEquals("containing", prop);
		System.out.println(prop);
	}
}
