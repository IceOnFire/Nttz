package it.ice.nttz.model;

public class Category {
	private String name;
	private float weight;

	public Category(String name) {
		this(name, 1);
	}

	public Category(String name, float weight) {
		this.name = name;
		this.weight = weight;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	@Override
	protected Category clone() {
		Category clone = new Category(name);
		clone.setWeight(weight);
		return clone;
	}

	@Override
	public String toString() {
		return name + " (" + (int) (weight * 100) + "%)";
	}
}
