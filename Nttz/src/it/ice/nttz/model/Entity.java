package it.ice.nttz.model;

import it.ice.nttz.Nttz;
import it.ice.nttz.view.Form;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;


public class Entity {
	protected static Logger logger = Logger.getAnonymousLogger();

	protected Entity parent;
	protected String name;
	protected String definition;
	protected Set<Category> categories;
	protected Set<Property> properties;
	protected Set<Sensor> sensors;
	protected Set<Entity> children;
	private Set<Relationship> activeRelationships;
	private Set<Relationship> passiveRelationships;

	public Entity() {
		categories = new HashSet<Category>();
		properties = new HashSet<Property>();
		sensors = new HashSet<Sensor>();
		children = new HashSet<Entity>();
		activeRelationships = new HashSet<Relationship>();
		passiveRelationships = new HashSet<Relationship>();
		addState(Relationship.DEFAULT);
	}

	public Entity(String definition) {
		this();
		setDefinition(definition);
	}

	public String getName() {
		return name;
	}

	public Entity setName(String name) {
		this.name = name;
		return this;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
		addCategory(definition);
	}

	public boolean isA(String categoryName) {
		return isA(categoryName, 1);
	}

	public boolean isA(String categoryName, float threshold) {
		Category category = null;
		Iterator<Category> it = categories.iterator();
		while (it.hasNext() && category == null) {
			Category c = it.next();
			if (c.getName().equals(categoryName)) {
				category = c;
			}
		}

		if (category == null) {
			return false;
		}
		if (category.getWeight() < threshold) {
			return false;
		}
		return true;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	public Category getCategory(String name) {
		Category category = null;
		Iterator<Category> it = categories.iterator();
		while (it.hasNext() && category == null) {
			Category c = it.next();
			if (c.getName().equals(name)) {
				category = c;
			}
		}
		return category;
	}

	public Entity addCategory(String name) {
		if (getCategory(name) == null) {
			categories.add(new Category(name));
		}
		return this;
	}

	public void addCategory(String name, float weight) {
		if (getCategory(name) == null) {
			Category category = new Category(name);
			category.setWeight(weight);
			categories.add(category);
		}
	}

	private void addCategory(Category category) {
		if (getCategory(category.getName()) == null) {
			categories.add(category);
		}
	}

	public void removeCategory(String name) {
		Category category = getCategory(name);
		if (category != null) {
			removeCategory(category);
		}
	}

	private void removeCategory(Category category) {
		categories.remove(category);
	}

	public Set<Property> getProperties() {
		return properties;
	}

	public void setProperties(Set<Property> properties) {
		this.properties = properties;
	}

	public boolean hasProperty(String name) {
		boolean hasProperty = false;
		Iterator<Property> it = properties.iterator();
		while (it.hasNext() && !hasProperty) {
			Property p = it.next();
			if (p.getName().equals(name)) {
				hasProperty = true;
			}
		}
		return hasProperty;
	}

	public Property getProperty(String name) {
		Property property = null;
		Iterator<Property> it = properties.iterator();
		while (it.hasNext() && property == null) {
			Property p = it.next();
			if (p.getName().equals(name)) {
				property = p;
			}
		}
		return property;
	}

	public float getPropertyValue(String name) {
		return getProperty(name).getValue();
	}

	public void addProperty(Property property) {
		if (getProperty(property.getName()) == null) {
			properties.add(property);
		}
	}

	public void removeProperty(String name) {
		Property property = getProperty(name);
		if (property != null) {
			properties.remove(property);
		}
	}

	public void onPropertyAcquired(Entity source, Entity target, Property property) {
		// logger.info("[" + this + "] " + source + " notified me that " + target
		// + " " + property);
	}

	public Set<Sensor> getSensors() {
		return sensors;
	}

	public void setSensors(Set<Sensor> sensors) {
		this.sensors = sensors;
	}

	public Sensor getSensor(String property) {
		Sensor sensor = null;
		Iterator<Sensor> it = sensors.iterator();
		while (it.hasNext() && sensor == null) {
			Sensor s = it.next();
			if (s.getProperty().equals(property)) {
				sensor = s;
			}
		}
		return sensor;
	}

	public void addSensor(String property) {
		if (getSensor(property) == null) {
			sensors.add(new Sensor(this, property));
		}
	}

	public void removeSensor(String property) {
		Sensor sensor = getSensor(property);
		if (sensor != null) {
			removeSensor(sensor);
		}
	}

	private void removeSensor(Sensor sensor) {
		sensor.destroy();
		sensors.remove(sensor);
	}

	public void sense(Entity entity) {
		for (Sensor sensor : sensors) {
			sensor.sense(entity);
		}
	}

	public Entity getRoot() {
		if (parent != null) {
			return parent.getRoot();
		}
		return this;
	}

	public boolean isARootEntity() {
		return this == getRoot();
	}

	public Entity getParent() {
		return parent;
	}

	public void setParent(Entity parent) {
		this.parent = parent;
	}

	public Set<Entity> getChildren() {
		return children;
	}

	public void setChildren(Set<Entity> children) {
		this.children = children;
		for (Entity child : children) {
			child.setParent(this);
		}
	}

	public Entity s(String category) {
		return getChild(category);
	}

	protected Entity getChild(String category) {
		for (Entity child : children) {
			if (child.isA(category)) {
				return child;
			}
			Entity c = child.getChild(category);
			if (c != null) {
				return c;
			}
		}
		return null;
	}

	public Entity findChildEqualTo(Entity mirror) {
		if (equals(mirror)) {
			return this;
		}
		Entity child = null;
		Iterator<Entity> it = children.iterator();
		while (it.hasNext() && child == null) {
			Entity c = it.next();
			child = c.findChildEqualTo(mirror);
		}
		return child;
	}

	public Set<Entity> findChildrenHavingProperty(String propertyName,
			boolean greaterThan, float threshold) {
		Set<Entity> entities = new HashSet<Entity>();
		Property property = getProperty(propertyName);
		if (property != null
				&& (greaterThan && property.getValue() >= threshold || !greaterThan
						&& property.getValue() < threshold)) {
			entities.add(this);
		}
		for (Entity child : children) {
			entities.addAll(child.findChildrenHavingProperty(propertyName,
					greaterThan, threshold));
		}
		return entities;
	}

	public Set<Entity> findChildrenNotHavingProperty(String propertyName) {
		Set<Entity> entities = new HashSet<Entity>();
		Property property = getProperty(propertyName);
		if (property == null) {
			entities.add(this);
		}
		for (Entity child : children) {
			entities.addAll(child.findChildrenNotHavingProperty(propertyName));
		}
		return entities;
	}

	public void addChild(Entity child) {
		if (getChild(child.getDefinition()) == null) {
			children.add(child);
			child.setParent(this);
		}
	}

	public void detachChild(Entity child) {
		child.setParent(null);
		children.remove(child);
	}

	public void destroyChild(Entity child) {
		child.destroy();
		children.remove(child);
	}

	private Relationship getRelationship(String name) {
		for (Relationship relationship : activeRelationships) {
			if (relationship.getName().equals(name)) {
				return relationship;
			}
		}
		return null;
	}

	public void addPassiveRelationship(Relationship relationship) {
		passiveRelationships.add(relationship);
	}

	public void removePassiveRelationship(Relationship relationship) {
		passiveRelationships.remove(relationship);
	}

	public boolean isInState(String state) {
		return getRelationship(state) != null;
	}

	public void addState(String state) {
		Relationship relationship = new Relationship(state);
		addState(relationship);
	}

	public void addState(Relationship relationship) {
		relationship.setSource(this);
		activeRelationships.add(relationship);
	}

	public void removeState(String state) {
		Relationship relationship = getRelationship(state);
		if (relationship != null) {
			removeState(relationship);
		}
	}

	public void removeState(Relationship relationship) {
		relationship.destroy();
		activeRelationships.remove(relationship);
	}

	public Set<Entity> getSourcesInRelationship(String relation) {
		Set<Entity> sources = new HashSet<Entity>();
		for (Relationship relationship : passiveRelationships) {
			if (relationship.getName().equals(relation)) {
				sources.add(relationship.getSource());
			}
		}
		return sources;
	}

	public Entity getSourceInRelationship(String relation) {
		Set<Entity> sources = getSourcesInRelationship(relation);
		Entity source = null;
		if (sources != null) {
			Object[] array = sources.toArray();
			source = (Entity) array[0];
		}
		return source;
	}

	public Set<Entity> getTargetsInRelationship(String relation) {
		Set<Entity> targets = new HashSet<Entity>();
		Relationship relationship = getRelationship(relation);
		if (relationship != null) {
			targets = relationship.getTargets();
		}
		return targets;
	}

	public Entity getTargetInRelationship(String relation) {
		Set<Entity> targets = getTargetsInRelationship(relation);
		Entity target = null;
		if (!targets.isEmpty()) {
			Object[] array = targets.toArray();
			target = (Entity) array[0];
		}
		return target;
	}

	public void addRelationship(String relation, Entity entity) {
		Set<Entity> entities = new HashSet<Entity>();
		entities.add(entity);
		addRelationship(relation, entities);
	}

	public void addRelationship(String relation, Set<Entity> entities) {
		Relationship relationship = getRelationship(relation);
		if (relationship == null) {
			relationship = new Relationship(relation);
			relationship.setSource(this);
			activeRelationships.add(relationship);
		}
		relationship.addTargets(entities);
	}

	public void removeRelationship(String relation, Entity entity) {
		Relationship relationship = getRelationship(relation);
		if (relationship != null) {
			removeRelationship(relationship, entity);
		}
	}

	private void removeRelationship(Relationship relationship, Entity entity) {
		relationship.removeTarget(entity);
		if (relationship.getTargets().isEmpty()) {
			relationship.destroy();
			activeRelationships.remove(relationship);
		}
	}

	public void removeRelationships(Entity entity) {
		for (Relationship relationship : activeRelationships) {
			removeRelationship(relationship, entity);
		}
	}

	public void removeAllRelationships() {
		for (Relationship relationship : new HashSet<Relationship>(
				activeRelationships)) {
			removeState(relationship);
		}

		for (Relationship relationship : new HashSet<Relationship>(
				passiveRelationships)) {
			Entity source = relationship.getSource();
			if (source != null) {
				source.removeRelationship(relationship, this); // should be doomed
			}
		}
		passiveRelationships.clear();
	}

	public void setForm(String relation, Form form) {
		Relationship relationship = getRelationship(relation);
		relationship.setForm(form);
	}

	public void act() {
		for (Property property : properties) {
			property.update();
		}

		for (Entity child : children) {
			child.act();
		}

		Set<Entity> containedEntities = getTargetsInRelationship(Nttz.CONTAINING);
		for (Entity containedEntity : new HashSet<Entity>(containedEntities)) {
			containedEntity.act();
		}

		for (Relationship relationship : activeRelationships) {
			relationship.update();
		}

		update();
	}

	public void update() {
		// do nothing
	}

	public void render(Object args) {
		for (Relationship relationship : activeRelationships) {
			relationship.render(args);
		}

		Set<Entity> containedEntities = getTargetsInRelationship(Nttz.CONTAINING);
		for (Entity containedEntity : new HashSet<Entity>(containedEntities)) {
			containedEntity.render(args);
		}
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Entity)) {
			return false;
		}
		Entity entity = (Entity) object;
		for (Category category : categories) {
			if (!entity.isA(category.getName())) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Entity clone() {
		Entity clone = null;
		try {
			clone = this.getClass().newInstance();
			clone.setName(name);
			clone.setDefinition(definition);
			for (Category category : categories) {
				clone.addCategory(category.clone());
			}
			for (Property property : properties) {
				clone.addProperty(property.clone());
			}
			for (Sensor sensor : sensors) {
				clone.addSensor(sensor.getProperty());
			}
			for (Entity child : children) {
				clone.addChild(child.clone());
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clone;
	}

	@Override
	public String toString() {
		String string = definition;
		if (!isARootEntity()) {
			string = getRoot().toString() + "'s " + string;
		}
		return string;
	}

	public void destroy() {
		parent = null;
		categories.clear();
		properties.clear();

		for (Sensor sensor : new HashSet<Sensor>(sensors)) {
			removeSensor(sensor);
		}

		for (Entity child : new HashSet<Entity>(children)) {
			destroyChild(child);
		}

		removeAllRelationships();
	}
}
