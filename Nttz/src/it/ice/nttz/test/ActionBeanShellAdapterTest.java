package it.ice.nttz.test;

import static org.junit.Assert.assertEquals;
import it.ice.nttz.Nttz;
import it.ice.nttz.model.Action;

import org.junit.Before;
import org.junit.Test;

public class ActionBeanShellAdapterTest {
	private String actionName;
	private Nttz nttz;

	@Before
	public void setUp() throws Exception {
		actionName = "Grab";
		nttz = new Nttz();
	}

	@Test
	public void testLoad() throws SecurityException, NoSuchMethodException {
		Action action = nttz.loadActionDefinition(actionName);
		assertEquals(actionName, action.getName());
		assertEquals("setAgent", action.getClass().getMethods()[0].getName());
	}
}
