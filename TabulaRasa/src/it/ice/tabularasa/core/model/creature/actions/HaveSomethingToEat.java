package it.ice.tabularasa.core.model.creature.actions;

import it.ice.nttz.model.Action;
import it.ice.nttz.model.Entity;
import it.ice.tabularasa.core.model.creature.Creature;

public class HaveSomethingToEat extends Action {
	@Override
	public void reset() {
		if (!agent.isInState(Creature.STATE_LOOKING_AROUND)
				&& !agent.isInState(Creature.STATE_EATING)
				&& !agent.isInState(Creature.STATE_TASTING)) {
			agent.addState(Creature.STATE_LOOKING_AROUND);
		}
	}

	@Override
	public void perform() {
		if (agent.isInState(Creature.STATE_LOOKING_AROUND)) {
			Action lookAround = new Action("LookAround");
			agent.engage(lookAround);
			return;
		}

		if (agent.isInState(Creature.STATE_EATING)) {
			Entity target = agent.getTargetInRelationship(Creature.STATE_EATING);
			Action eat = new Action("Eat");
			eat.setTarget(target);
			agent.engage(eat);
			return;
		}

		if (agent.isInState(Creature.STATE_TASTING)) {
			Entity target = agent.getTargetInRelationship(Creature.STATE_TASTING);
			Action taste = new Action("Taste");
			taste.setTarget(target);
			agent.engage(taste);
			return;
		}
	}
}
