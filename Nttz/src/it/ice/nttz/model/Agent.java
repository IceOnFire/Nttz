package it.ice.nttz.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Agent extends Entity {
	private Set<Action> knownActions;
	private Set<Action> forbiddenActions;
	private List<Action> actionQueue;

	public Agent() {
		super("agent");
		knownActions = new HashSet<Action>();
		forbiddenActions = new HashSet<Action>();
		actionQueue = new ArrayList<Action>();
	}

	public Agent(String definition) {
		this();
		setDefinition(definition);
	}

	public boolean knows(Action action) {
		return getKnownAction(action) != null;
	}

	protected Action getKnownAction(Action sample) {
		for (Action knownAction : knownActions) {
			if (knownAction.equals(sample)) {
				return knownAction;
			}
		}
		return null;
	}

	public Agent learn(Action action) {
		action.setAgent(this);
		knownActions.add(action);
		return this;
	}

	public void forget(Action action) {
		Action knownAction = getKnownAction(action);
		knownActions.remove(knownAction);
	}

	public boolean isAllowedToDo(Action action) {
		Action knownAction = getKnownAction(action);
		return !forbiddenActions.contains(knownAction);
	}

	public void forbidAction(Action action) {
		Action knownAction = getKnownAction(action);
		forbiddenActions.add(knownAction);
	}

	public void allowAction(Action action) {
		Action knownAction = getKnownAction(action);
		forbiddenActions.remove(knownAction);
	}

	public Action getPerformingAction(Action sample) {
		for (Action action : actionQueue) {
			if (action.equals(sample)) {
				return action;
			}
		}
		return null;
	}

	public void engage(Action action) {
		if (knows(action) && !isDoing(action) && isAllowedToDo(action)) {
			Action clone = getKnownAction(action).clone();
			clone.setAgent(this);
			clone.setTarget(action.getTarget());
			clone.setTools(action.getTools());
			clone.setArguments(action.getArguments());
			clone.setPermanent(action.isPermanent());
			clone.reset();
			actionQueue.add(clone);
		}
	}

	public void disengage(String action) {
		disengage(new Action(action));
	}

	public void disengage(Action action) {
		if (knows(action) && isDoing(action)) {
			Action performingAction = getPerformingAction(action);
			performingAction.done();
			actionQueue.remove(performingAction);
		}
	}

	private void performActions() {
		List<Action> runningActions = new ArrayList<Action>(actionQueue);
		for (Action action : runningActions) {
			performAction(action);
			if (!action.isPermanent()) {
				disengage(action);
			}
		}
	}

	private void performAction(Action action) {
		action.perform();
	}

	public boolean isDoing(Action what) {
		for (Action action : actionQueue) {
			if (action.equals(what)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void act() {
		super.act();
		plan();
		performActions();
	}

	protected void plan() {
		// do nothing
	}

	@Override
	public Agent clone() {
		Agent clone = (Agent) super.clone();
		for (Action knownAction : knownActions) {
			clone.learn(knownAction.clone());
		}

		for (Action forbiddenAction : forbiddenActions) {
			clone.forbidAction(forbiddenAction.clone());
		}
		return clone;
	}

	@Override
	public String toString() {
		if (name != null) {
			return name;
		}
		return super.toString();
	}

	@Override
	public void destroy() {
		super.destroy();
		knownActions.clear();
		forbiddenActions.clear();
		actionQueue.clear();
	}
}
