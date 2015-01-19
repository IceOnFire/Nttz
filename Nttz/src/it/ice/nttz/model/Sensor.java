package it.ice.nttz.model;

public class Sensor {
	private String property;
	private Entity source;

	public Sensor(Entity source) {
		this.source = source;
	}

	public Sensor(Entity source, String property) {
		this(source);
		listen(property);
	}

	public String getProperty() {
		return property;
	}

	void listen(String property) {
		this.property = property;
	}

	public void sense(Entity entity) {
		Property property = entity.getProperty(this.property);
		if (property != null) {
			notifyPropertyAcquired(entity, property);
		}
	}

	private void notifyPropertyAcquired(Entity target, Property property) {
		source.getRoot().onPropertyAcquired(source, target, property);
	}

	public void destroy() {
		source = null;
	}

	@Override
	public String toString() {
		return source + "'s sensor for " + property;
	}
}
