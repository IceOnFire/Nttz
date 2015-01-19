package it.ice.nttz.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import it.ice.nttz.Nttz;
import it.ice.nttz.model.Entity;

import java.net.MalformedURLException;
import java.util.Map;

import org.dom4j.DocumentException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class XMLEntityLoaderTest {
	private Nttz nttz;

	@Before
	public void setUp() throws Exception {
		nttz = new Nttz();
	}

	@Test
	public void testLoad() throws MalformedURLException, DocumentException {
		nttz.loadEntityDefinitions("file:///home/ice/Progetti/tabularasa/Nttz/res/entities.xml");

		Map<String, Entity> entityDefinitions = nttz.getEntityDefinitions();
		Entity entity = entityDefinitions.get("apple");
		assertNotNull(entity);
		assertEquals("apple", entity.getDefinition());
		assertEquals(2, entity.getCategories().size());
		assertTrue(entity.isA("food"));
		assertEquals(0.7f, entity.getPropertyValue("taste"), 0);
		assertEquals(0.2f, entity.getPropertyValue("nutrition factor"), 0);
	}

	@After
	public void tearDown() throws Exception {
		// do nothing
	}
}
