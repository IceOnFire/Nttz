package it.ice.tabularasa.core.model.creature;

import it.ice.nttz.Nttz;
import it.ice.nttz.model.Action;
import it.ice.nttz.model.Agent;
import it.ice.nttz.model.Entity;
import it.ice.nttz.model.Property;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class Creature extends Agent {
	public final static String PROPERTY_NOT_HUNGRY = "not hungry";
	public final static String STATE_LOOKING_AROUND = "looking around",
			STATE_EATING = "eating", STATE_TASTING = "tasting";

	private Set<Entity> beliefs;
	private Set<Action> needs;
	private Set<Action> wishes;

	public Creature() {
		super("creature");
		beliefs = new HashSet<Entity>();
		needs = new HashSet<Action>();
		wishes = new HashSet<Action>();
	}

	public Creature(String definition) {
		this();
		setDefinition(definition);
	}

	public Entity recallBelief(String definition) {
		Entity belief = null;
		Iterator<Entity> it = beliefs.iterator();
		while (it.hasNext() && belief == null) {
			Entity t = it.next();
			if (t.isA(definition)) {
				belief = t;
			} else {
				belief = t.s(definition);
			}
		}
		return belief;
	}

	public Entity recallBelief(Entity entity) {
		Entity belief = null;
		Iterator<Entity> it = beliefs.iterator();
		while (it.hasNext() && belief == null) {
			Entity ntt = it.next();
			if (ntt.equals(entity)) {
				belief = ntt;
			} else {
				belief = ntt.s(entity.getDefinition());
			}
		}
		return belief;
	}

	public Set<Entity> recallBeliefsHavingProperty(String propertyName,
			boolean greaterThan, float threshold) {
		Set<Entity> entities = new HashSet<Entity>();
		for (Entity belief : beliefs) {
			entities.addAll(belief.findChildrenHavingProperty(propertyName,
					greaterThan, threshold));
		}
		return entities;
	}

	public Set<Entity> recallBeliefsNotHavingProperty(String propertyName) {
		Set<Entity> entities = new HashSet<Entity>();
		for (Entity belief : beliefs) {
			entities.addAll(belief.findChildrenNotHavingProperty(propertyName));
		}
		return entities;
	}

	public Entity findSomethingNew() {
		Entity something = null;
		Entity container = getRoot().getSourceInRelationship(Nttz.CONTAINING);
		Set<Entity> entities = container.getTargetsInRelationship(Nttz.CONTAINING);
		Iterator<Entity> it = entities.iterator();
		while (it.hasNext() && something == null) {
			Entity entity = it.next();
			if (!entity.equals(this)) {
				Entity belief = recallBelief(entity.getDefinition());
				if (belief == null) {
					something = entity;
				}
			}
		}
		return something;
	}

	public void addBelief(Entity belief) {
		beliefs.add(belief);
	}

	public void addNeed(Action need) {
		Action knownAction = getKnownAction(need);
		needs.add(knownAction);
	}

	public void removeNeed(Action need) {
		Action knownAction = getKnownAction(need);
		needs.remove(knownAction);
	}

	public void addWish(Action wish) {
		Action knownAction = getKnownAction(wish);
		wishes.add(knownAction);
	}

	public void removeWish(Action wish) {
		Action knownAction = getKnownAction(wish);
		wishes.remove(knownAction);
	}

	public void learn(Entity entity, Property property) {
		Entity belief = recallBelief(entity.getDefinition());
		if (belief == null) {
			belief = entity.clone();
			addBelief(belief);
		}
		belief.addProperty(property);
	}

	@Override
	public void onPropertyAcquired(Entity source, Entity target, Property property) {
		super.onPropertyAcquired(source, target, property);
		learn(target, property);
		if (property.getName().equals("taste")) {
			logger.info("[" + this + "] " + target + " is "
					+ (property.getValue() < Property.AVG_VALUE ? "not " : "") + "tasty");
		} else if (property.getName().equals("nutrition factor")) {
			getProperty(PROPERTY_NOT_HUNGRY).increaseValue(property.getValue());
			logger.info("[" + this + "] " + target + " filled me by "
					+ (int) (property.getValue() * 100) + "%");
		}
	}

	@Override
	public void update() {
		super.update();
		if (getPropertyValue(PROPERTY_NOT_HUNGRY) == Property.MIN_VALUE) {
			logger.info("[" + this + "] I'm hungry");
			addNeed(new Action("HaveSomethingToEat"));
		} else {
			logger.info("[" + this + "] " + getProperty(PROPERTY_NOT_HUNGRY));
			removeNeed(new Action("HaveSomethingToEat"));
		}
	}

	@Override
	public void plan() {
		List<Action> availableActions = findSomethingToDo();

		boolean actionEnabled = false;
		Iterator<Action> it = availableActions.iterator();
		while (it.hasNext() && !actionEnabled) {
			Action action = it.next();
			// try {
			engage(action);
			actionEnabled = isDoing(action);
			// } catch (ActionNotSupportedException e) {
			// logger.warning("[" + this + "] I don't know how to " + action);
			// }
		}
	}

	private List<Action> findSomethingToDo() {
		List<Action> actions = new Vector<Action>();
		actions.addAll(needs);
		actions.addAll(wishes);
		return actions;
	}

	@Override
	public void destroy() {
		super.destroy();
		beliefs.clear();
		needs.clear();
		wishes.clear();
	}
}
