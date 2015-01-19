package it.ice.tabularasa.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import it.ice.nttz.Nttz;
import it.ice.nttz.model.Entity;

import java.net.MalformedURLException;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TabulaRasaLoadTest {
	private Nttz nttz;

	@Before
	public void setUp() throws Exception {
		nttz = new Nttz();
	}

	@Test
	public void testLoad() throws MalformedURLException {
		nttz.loadEntityDefinitions("file:///home/ice/Documenti/Progetti/tabularasa/TabulaRasa/res/entities.xml");

		Collection<Entity> entities = nttz.getEntities();
		for (Entity entity : entities) {
			assertNotNull(entity);
			if (entity.getDefinition().equals("apple")) {
				assertEquals("apple", entity.getDefinition());
				assertEquals(2, entity.getCategories().size());
				assertTrue(entity.isA("food"));
				assertEquals(0.7f, entity.getPropertyValue("taste"), 0);
				assertEquals(0.2f, entity.getPropertyValue("nutrition factor"), 0);
			} else if (entity.getDefinition().equals("blob")) {
				assertEquals("blob", entity.getDefinition());
				assertEquals(3, entity.getCategories().size());
				assertTrue(entity.isA("creature"));
				assertTrue(entity.isA("agent"));
				assertTrue(entity.isARootEntity());
				assertEquals(2, entity.getChildren().size());
				for (Entity child : entity.getChildren()) {
					assertTrue(!child.isARootEntity());
					if (child.isA("eyes")) {
						assertTrue(child.getChildren().isEmpty());
					} else if (child.isA("mouth")) {
						assertNotNull(child.getSensor("nutrition factor"));
						assertEquals(1, child.getChildren().size());
						Entity tongue = child.s("tongue");
						assertNotNull(tongue);
						assertNotNull(tongue.getSensor("taste"));
					}
				}
			}
		}
	}

	@After
	public void tearDown() throws Exception {
	}

}
