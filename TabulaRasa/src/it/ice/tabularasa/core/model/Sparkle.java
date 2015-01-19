package it.ice.tabularasa.core.model;

import it.ice.nttz.model.Entity;
import it.ice.nttz.model.Property;

public class Sparkle extends Entity {
	public Sparkle() {
		super("sparkle");
		addProperty(new Property("light", 1f, 0.05f));
	}

	@Override
	public void update() {
		super.update();
		if (getPropertyValue("light") == Property.MIN_VALUE) {
			destroy();
			logger.info("[" + this + "] *fizz*");
		}
	}
}
