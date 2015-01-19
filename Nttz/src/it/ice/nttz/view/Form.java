package it.ice.nttz.view;

import it.ice.nttz.model.Entity;
import it.ice.nttz.model.Relationship;

public abstract class Form {
	private Relationship relationship;

	public Entity getEntity() {
		return relationship.getSource();
	}

	public Relationship getRelationship() {
		return relationship;
	}

	public void setRelationship(Relationship relationship) {
		this.relationship = relationship;
	}

	public abstract void update();

	public abstract void render(Object args);
}
