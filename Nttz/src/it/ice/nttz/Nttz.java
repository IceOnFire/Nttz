package it.ice.nttz;

import it.ice.nttz.model.Action;
import it.ice.nttz.model.ActionDefinition;
import it.ice.nttz.model.Entity;
import it.ice.nttz.persistence.XMLEntityLoader;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.dom4j.DocumentException;

public class Nttz {
	/* hardcoded relationships */
	public static String CONTAINING = "containing",
			CONTENT_VISIBLE = "content visible", TARGETING = "targeting",
			HOLDING = "holding";

	private static int round = 1;

	private Map<String, Entity> entityDefinitions;
	private Entity world;

	public Nttz() {
		entityDefinitions = new HashMap<String, Entity>();
		world = new Entity("world");
	}

	public void loadEntityDefinitions(String url) {
		try {
			new XMLEntityLoader(this).load(new URL(url));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Map<String, Entity> getEntityDefinitions() {
		return entityDefinitions;
	}

	public void define(String definition, Entity entity) {
		entityDefinitions.put(definition, entity);
	}

	public Entity create(String definition) {
		return entityDefinitions.get(definition).clone();
	}

	public Entity summon(String definition) {
		Entity entity = create(definition);
		summon(entity);
		return entity;
	}

	public void summon(Entity entity) {
		world.addRelationship(CONTAINING, entity);
	}

	public Set<Entity> getEntities() {
		return world.getTargetsInRelationship(CONTAINING);
	}

	public Entity getEntity(String definition) {
		Set<Entity> entities = getEntities();
		for (Entity entity : entities) {
			if (entity.getName().equals(definition) || entity.isA(definition)) {
				return entity;
			}
		}
		return null;
	}

	public Entity getWorld() {
		return world;
	}

	public Action loadActionDefinition(String name) {
		return new ActionDefinition(name);
	}

	public void act() {
		System.out.println("[ROUND " + round + "]");
		world.act();
		round++;
	}

	public void render(Object args) {
		world.render(args);
	}
}
