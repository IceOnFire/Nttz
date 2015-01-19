package it.ice.nttz.model;

import it.ice.nttz.view.Form;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;


public class Relationship {
	public final static String DEFAULT = "default";
	protected static Logger logger = Logger.getAnonymousLogger();

	private String name;
	private Entity source;
	private Set<Entity> targets;
	private Form form;

	public Relationship(String name) {
		this.name = name;
		targets = new HashSet<Entity>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Entity getSource() {
		return source;
	}

	public void setSource(Entity source) {
		this.source = source;
	}

	public Set<Entity> getTargets() {
		return targets;
	}

	public boolean hasTarget(Entity entity) {
		return targets.contains(entity);
	}

	public void addTarget(Entity entity) {
		Set<Entity> entities = new HashSet<Entity>();
		entities.add(entity);
		addTargets(entities);
	}

	public void addTargets(Set<Entity> entities) {
		for (Entity entity : entities) {
			targets.add(entity);
			entity.addPassiveRelationship(this);
		}
	}

	public void removeTarget(Entity entity) {
		targets.remove(entity);
		entity.removePassiveRelationship(this);
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
		form.setRelationship(this);
	}

	public void update() {
		if (form != null) {
			form.update();
		}
	}

	public void render(Object args) {
		if (form != null) {
			form.render(args);
		}
	}

	public void destroy() {
		source = null;
		targets.clear();
	}

	@Override
	public String toString() {
		return "(" + source + " " + name + " " + targets + ")";
	}
}
