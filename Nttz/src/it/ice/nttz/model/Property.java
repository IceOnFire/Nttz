package it.ice.nttz.model;

public class Property {
	public static float MIN_VALUE = 0;
	public static float MAX_VALUE = 1;
	public static float AVG_VALUE = (MAX_VALUE - MIN_VALUE) / 2;

	private String name;
	private float value;
	private String unit;
	private float decreaseValue;

	public Property(String name) {
		this.name = name;
		this.value = MAX_VALUE;
		this.decreaseValue = 0;
	}

	public Property(String name, float value) {
		this(name);
		this.value = value;
	}

	public Property(String name, float value, String unit) {
		this(name, value);
		this.unit = unit;
	}

	public Property(String name, float initialValue, float decreaseValue) {
		this(name, initialValue);
		this.decreaseValue = decreaseValue;
	}

	public String getName() {
		return name;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public void increaseValue(float amount) {
		value += amount;
		// if (value > MAX_VALUE) {
		// value = MAX_VALUE;
		// }
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public float getDecreaseValue() {
		return decreaseValue;
	}

	public void setDecreaseValue(float decreaseValue) {
		this.decreaseValue = decreaseValue;
	}

	public void update() {
		value -= decreaseValue;
		// if (value < MIN_VALUE) {
		// value = MIN_VALUE;
		// }
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Property)) {
			return false;
		}
		Property property = (Property) object;
		if (!property.name.equals(name)) {
			return false;
		}
		return true;
	}

	@Override
	protected Property clone() {
		Property clone = new Property(name);
		clone.setValue(value);
		clone.setUnit(unit);
		clone.setDecreaseValue(decreaseValue);
		return clone;
	}

	@Override
	public String toString() {
		return name + " is " + (int) (value * 100) + "%";
	}
}
